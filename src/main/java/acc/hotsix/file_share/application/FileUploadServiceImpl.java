package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.LogRepository;
import acc.hotsix.file_share.domain.Log;
import acc.hotsix.file_share.global.error.FileDuplicateException;
import acc.hotsix.file_share.global.error.UploadFileException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import acc.hotsix.file_share.domain.File;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final AmazonS3Client amazonS3Client;

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    private final LogRepository logRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // S3에 파일 업로드
    public String uploadFileToS3(MultipartFile file, String key) throws UploadFileException {
        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(amazonS3Client)
                .build();

        try {
            // 파일 메타데이터 설정 (S3용)
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // 업로드 작업 수행
            Upload upload = tm.upload(bucketName, key, file.getInputStream(), metadata);
            upload.waitForCompletion();

            // 업로드 완료 후 정적 URL 반환
            return amazonS3Client.getUrl(bucketName, key).toString();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UploadFileException();
        } catch (IOException e) {
            throw new UploadFileException();
        } finally {
            tm.shutdownNow(false);
        }
    }

    // 파일 업로드 프로세스
    @Transactional
    public void uploadFile(MultipartFile file, String directory, String password) throws UploadFileException, FileDuplicateException {
        List<File> duplicateFiles = fileService.getSameNameAndPathFileList(file.getOriginalFilename(), directory);
        if (duplicateFiles.size() > 0) {    // 중복 파일 처리
            throw new FileDuplicateException("File duplicate.");
        }

        // 파일 메타 데이터 - 파일 사이즈
        double sizeInMegabytes = file.getSize() / (1024.0 * 1024.0);
        BigDecimal sizeRounded = new BigDecimal(sizeInMegabytes).setScale(2, RoundingMode.HALF_UP);

        // 파일 메타 데이터 - 파일 타입
        String fileType = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileType == null) {
            fileType = file.getContentType();
        }

        // 파일 메타데이터 생성
        File fileMetaData = File.builder()
                .name(file.getOriginalFilename())
                .fileType( fileType)
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
            // 파일 업로드
            String url = uploadFileToS3(file, fileId.toString());

            savedFile.setResource(url);     // 메타 데이터 업데이트 - URL
            savedFile.setUploaded(true);    // 메타 데이터 업데이트 - 업로드 완료

            // 메타데이터 업데이트
            fileService.saveMetaData(savedFile);
        } catch (Exception e) {
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
}
