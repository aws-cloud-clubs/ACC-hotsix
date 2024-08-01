package acc.hotsix.file_share.application;

import acc.hotsix.file_share.global.error.FileNotFoundException;

import java.security.NoSuchAlgorithmException;

public interface FileShareService {
    String generateShareLink(Long fileId) throws FileNotFoundException, NoSuchAlgorithmException;

}
