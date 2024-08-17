package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class InvalidQueryParamException extends BusinessException{
    public InvalidQueryParamException() {
        super(ExceptionCode.INVALID_QUERY_PARAM);
    }
}
