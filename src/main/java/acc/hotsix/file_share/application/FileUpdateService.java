package acc.hotsix.file_share.application;

import acc.hotsix.file_share.global.error.FileDuplicateException;
import acc.hotsix.file_share.global.error.FileNotFoundException;
import acc.hotsix.file_share.global.error.FileTypeMismatchException;
import acc.hotsix.file_share.global.error.UploadFileException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUpdateService {

    void updateFile(String fileId, String directory, MultipartFile file)
            throws FileNotFoundException, UploadFileException, FileTypeMismatchException, FileDuplicateException;
}
