package acc.hotsix.file_share.application;

import acc.hotsix.file_share.global.error.FileDuplicateException;
import acc.hotsix.file_share.global.error.UploadFileException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFileToS3(MultipartFile file, String key) throws UploadFileException;

    void uploadFile(MultipartFile file, String directory) throws UploadFileException, FileDuplicateException;
}
