package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import acc.hotsix.file_share.dao.LogRepository;
import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.domain.Log;
import acc.hotsix.file_share.dto.FileDownloadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

   // private final AmazonS3Client amazonS3Client;
    private final FileRepository fileRepository;
    private final LogRepository logRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public FileDownloadDto downloadFile(Long fileId) {
        File file = fileRepository.findById(fileId).get();
        file.updateDownloadCount();

        Log log = Log.builder()
                .type(Log.Type.DOWNLOAD)
                .file(file)
                .build();

        logRepository.save(log);

     // S3Object object = amazonS3Client.getObject(bucketName, fileId.toString());
        String filename = fileRepository.findNameById(fileId);
        return new FileDownloadDto(InputStream.nullInputStream(), filename); // TODO: 수정
    }

}
