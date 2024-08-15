package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.LogRepository;
import acc.hotsix.file_share.domain.Log;
import acc.hotsix.file_share.global.error.FileDuplicateException;
import acc.hotsix.file_share.global.error.UploadFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import acc.hotsix.file_share.domain.File;
import software.amazon.awssdk.core.internal.sync.FileContentStreamProvider;
import software.amazon.awssdk.http.*;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    private final LogRepository logRepository;

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public void uploadFileToS3(MultipartFile file, String directory, String password) throws UploadFileException, FileDuplicateException {
        // 파일 메타데이터 등록
        List<File> duplicateFiles = fileService.getSameNameAndPathFileList(file.getOriginalFilename(), directory);
        if (!duplicateFiles.isEmpty()) {    // 중복 파일 처리
            throw new FileDuplicateException("File duplicate.");
        }

        // 파일 메타 데이터 - 파일 사이즈
        double sizeInMegabytes = file.getSize() / (1024.0 * 1024.0);
        BigDecimal sizeRounded = BigDecimal.valueOf(sizeInMegabytes).setScale(2, RoundingMode.HALF_UP);

        // 파일 메타 데이터 - 파일 타입
        String fileType = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileType == null) {
            fileType = file.getContentType();
        }

        // 파일 메타데이터 생성
        File fileMetaData = File.builder()
                .name(file.getOriginalFilename())
                .fileType(fileType)
                .lastModifiedAt(LocalDateTime.now())
                .fileSize(sizeRounded.doubleValue())
                .createdAt(LocalDateTime.now())
                .path(directory)
                .download(0L)
                .view(0L)
                .password(passwordEncoder.encode(password))
                .build();

        // 메타데이터 저장
        File savedFile = fileService.saveMetaData(fileMetaData);
        Long fileId = savedFile.getFileId();

        try {
            // presigned URL 생성 및 업로드 요청
            uploadPresignedURL(fileId, file);

            // 메타데이터 업데이트 - 업로드 완료
            savedFile.setUploaded(true);
            fileService.saveMetaData(savedFile);
        } catch (Exception e) {
            e.printStackTrace();
            fileService.removeFileMetaData(savedFile);
            throw new UploadFileException();
        }

        // 파일 등록 로그 생성
        Log log = Log.builder()
                .file(savedFile)
                .type(Log.Type.CREATE)
                .build();

        logRepository.save(log);
    }

    // 업로드 presignedURL 요청
    public void uploadPresignedURL(Long fileId, MultipartFile file) throws Exception {
        // presigned URL 생성
        String presignedURL = createUploadPresignedUrl(fileId.toString(), file.getContentType(), file.getSize());
        System.out.println(presignedURL);

        // 파일 업로드 요청 전송
        useSdkHttpClientToPut(presignedURL, file);
    }

    // 업로드 presignedURL 생성
    private String createUploadPresignedUrl(String key, String contentType, Long size) throws Exception {
        try {
            System.out.println(contentType);

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .contentLength(size)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5))  // 유효 시간 5분
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toExternalForm();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(); // TODO: 오류처리
        }
    }

    // S3에 파일 업로드 요청
    private void useSdkHttpClientToPut(String presignedUrlString, MultipartFile file) {
        // 멀티 파트 변환용 파일 객체 생성
        java.io.File fileToPut = new java.io.File(System.getProperty("java.io.tmpdir") + file.getOriginalFilename());

        try {
            URL presignedUrl = new URL(presignedUrlString);

            SdkHttpRequest.Builder requestBuilder = SdkHttpRequest.builder()
                    .method(SdkHttpMethod.PUT) // PUT 메소드 설정
                    .uri(presignedUrl.toURI()); // URI 설정

            // 헤더 추가
            requestBuilder.putHeader("Content-Type", file.getContentType());
            requestBuilder.putHeader("Content-Length", String.valueOf(file.getSize()));

            // SdkHttpRequest 객체를 최종 생성
            SdkHttpRequest request = requestBuilder.build();

            // 멀티파트 -> 파일 객체 변환
            file.transferTo(fileToPut);

            // 실제 HTTP 요청 실행
            HttpExecuteRequest executeRequest = HttpExecuteRequest.builder()
                    .request(request)
                    .contentStreamProvider(new FileContentStreamProvider(fileToPut.toPath()))
                    .build();

            try (SdkHttpClient sdkHttpClient = ApacheHttpClient.create()) {
                HttpExecuteResponse response = sdkHttpClient.prepareRequest(executeRequest).call();
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        } finally {
            if(fileToPut.exists()) {
                fileToPut.delete();
            }
        }
    }


}
