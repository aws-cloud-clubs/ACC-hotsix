package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class FileTypeMismatchException extends BusinessException {
    public FileTypeMismatchException() {
        super(ExceptionCode.FILE_TYPE_MISMATCH);
    }
}
