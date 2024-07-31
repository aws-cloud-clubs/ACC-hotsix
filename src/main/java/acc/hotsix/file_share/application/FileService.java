package acc.hotsix.file_share.application;

import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.global.error.FileNotFoundException;

import java.util.HashMap;
import java.util.List;

public interface FileService {
    File getFileById(Long fileId) throws FileNotFoundException;

    File saveMetaData(File file);

    void removeFileMetaData(File file);

    List<File> getSameNameAndPathFileList(String name, String directory);

    boolean validateFileAccess(Long fileId, String password) throws FileNotFoundException;

    HashMap getAllUrlMap();
}
