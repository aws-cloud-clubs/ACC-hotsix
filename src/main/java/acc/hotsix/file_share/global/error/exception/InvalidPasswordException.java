package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class InvalidPasswordException extends BusinessException{
    public InvalidPasswordException() {
        super(ExceptionCode.INVALID_PASSWORD);
    }
}
