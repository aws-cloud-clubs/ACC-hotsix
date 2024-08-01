package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileDeleteService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final FileRepository fileRepository;
    private final AmazonS3Client amazonS3Client;

    public void deleteFile(Long fileId) {
        try {
            amazonS3Client.deleteObject(bucketName, fileId.toString());
            fileRepository.deleteById(fileId);
        } catch (Exception e) {
            // TODO 에러 처리 추후 수정 예정
        }
    }
}
