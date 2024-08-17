package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class DetailFileException extends BusinessException{
    public DetailFileException() {
        super(ExceptionCode.DETAIL_ERROR);
    }
}
