package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileDeleteService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final FileRepository fileRepository;
    //private final AmazonS3Client amazonS3Client;

    public void deleteFile(Long fileId) {
       // amazonS3Client.deleteObject(bucketName, fileId.toString());
        fileRepository.deleteById(fileId);
    }
}
