package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class FileNotFoundException extends BusinessException {
    public FileNotFoundException() {
        super(ExceptionCode.FILE_NOT_FOUND);
    }
}
