package acc.hotsix.file_share.application;

import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.dto.FileMetadataResponseDto;
import acc.hotsix.file_share.global.error.exception.FileNotFoundException;
import acc.hotsix.file_share.global.error.exception.InvalidShareLinkException;

import java.util.HashMap;
import java.util.List;

public interface FileService {
    File getFileById(Long fileId) throws FileNotFoundException;

    File saveMetaData(File file);

    void removeFileMetaData(File file);

    List<File> getSameNameAndPathFileList(String name, String directory);

    boolean validateFileAccess(Long fileId, String password) throws FileNotFoundException;

    HashMap getAllUrlMap();

    String getResourceByLink(String link) throws InvalidShareLinkException;

    FileMetadataResponseDto getMetadataById(Long fileId);
}
