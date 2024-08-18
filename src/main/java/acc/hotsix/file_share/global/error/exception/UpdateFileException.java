package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class UpdateFileException extends BusinessException{
    public UpdateFileException() {
        super(ExceptionCode.UPDATE_ERROR);
    }
}
