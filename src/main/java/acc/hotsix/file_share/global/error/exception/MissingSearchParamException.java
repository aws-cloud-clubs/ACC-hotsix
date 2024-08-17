package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class MissingSearchParamException extends BusinessException{
    public MissingSearchParamException() {
        super(ExceptionCode.MISSING_SEARCH_PARAM);
    }
}
