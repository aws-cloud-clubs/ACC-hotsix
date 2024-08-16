package acc.hotsix.file_share.application;

import acc.hotsix.file_share.global.error.exception.FileDuplicateException;
import acc.hotsix.file_share.global.error.exception.UploadFileException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    void uploadFileToS3(MultipartFile file, String directory, String password) throws UploadFileException, FileDuplicateException;
    void uploadPresignedURL(Long fileId, MultipartFile file) throws Exception;
}
