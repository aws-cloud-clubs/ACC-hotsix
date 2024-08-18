package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import acc.hotsix.file_share.dao.LogRepository;
import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.domain.Log;
import acc.hotsix.file_share.dto.FileDownloadDto;
import acc.hotsix.file_share.global.error.exception.DownloadFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.http.*;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.utils.IoUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final FileRepository fileRepository;

    private final LogRepository logRepository;

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public FileDownloadDto downloadFile(Long fileId) throws Exception {
        File file = fileRepository.findById(fileId).get();
        file.updateDownloadCount();

        // 다운로드 로그 작성
        Log log = Log.builder()
                .type(Log.Type.DOWNLOAD)
                .file(file)
                .build();

        logRepository.save(log);

        // presigned URL 생성
        String presignedURL = createPresignedGetUrl(bucketName, fileId.toString());

        // 파일 다운로드 요청 전송
        ByteArrayOutputStream byteArrayOutputStream = useSdkHttpClientToPut(presignedURL);

        String filename = fileRepository.findNameById(fileId);
        return new FileDownloadDto(byteArrayOutputStream, filename);
    }

    // 다운로드 presignedURL 생성
    public String createPresignedGetUrl(String bucketName, String keyName) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))  // 유효 시간 5분
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toExternalForm();
    }

    // 파일 다운로드 요청
    private ByteArrayOutputStream useSdkHttpClientToPut(String presignedUrlString) throws URISyntaxException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // Capture the response body to a byte array.
        try {
            URL presignedUrl = new URL(presignedUrlString);
            SdkHttpRequest request = SdkHttpRequest.builder()
                    .method(SdkHttpMethod.GET)
                    .uri(presignedUrl.toURI())
                    .build();

            HttpExecuteRequest executeRequest = HttpExecuteRequest.builder()
                    .request(request)
                    .build();

            try (SdkHttpClient sdkHttpClient = ApacheHttpClient.create()) {
                HttpExecuteResponse response = sdkHttpClient.prepareRequest(executeRequest).call();
                response.responseBody().ifPresentOrElse(
                        abortableInputStream -> {
                            try {
                                IoUtils.copy(abortableInputStream, byteArrayOutputStream);
                            } catch (IOException e) {
                                throw new DownloadFileException();
                            }
                        },
                        () -> {
                            throw new DownloadFileException();
                        });
            }
        } catch (URISyntaxException e) {
            throw new URISyntaxException(e.getInput(), e.getReason());
        } catch (IOException e) {
            throw new IOException();
        }
        return byteArrayOutputStream;
    }
}
