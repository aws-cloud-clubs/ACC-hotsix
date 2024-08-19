package acc.hotsix.file_share.application;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpdateService {
    void updateFile(MultipartFile file, String fileId, String newDirectory) throws Exception;
}
