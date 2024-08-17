package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class DeleteFileException extends BusinessException{
    public DeleteFileException() {
        super(ExceptionCode.DELETE_ERROR);
    }
}
