package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import acc.hotsix.file_share.dto.FileDownloadDto;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final AmazonS3Client amazonS3Client;
    private final FileRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public FileDownloadDto downloadFile(Long fileId) {
        S3Object object = amazonS3Client.getObject(bucketName, fileId.toString());
        String filename = fileRepository.findNameById(fileId);
        return new FileDownloadDto(object.getObjectContent(), filename);
    }

}
