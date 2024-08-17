package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class MissingSearchResultException extends BusinessException{
    public MissingSearchResultException() {
        super(ExceptionCode.MISSING_SEARCH_RESULT);
    }
}
