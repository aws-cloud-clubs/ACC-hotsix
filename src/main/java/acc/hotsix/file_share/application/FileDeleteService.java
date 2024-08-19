package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
@RequiredArgsConstructor
public class FileDeleteService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final FileRepository fileRepository;

    private final S3Client s3Client;

    @Transactional
    public void deleteFile(Long fileId) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileId.toString())
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        fileRepository.deleteById(fileId);
    }
}
