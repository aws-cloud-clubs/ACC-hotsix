package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class UploadFileException extends BusinessException {
    public UploadFileException() {
        super(ExceptionCode.UPLOAD_ERROR);
    }
}
