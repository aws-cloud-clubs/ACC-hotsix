package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class FileDuplicateException extends BusinessException {
    public FileDuplicateException() {
        super(ExceptionCode.FILE_DUPLICATE);
    }
}
