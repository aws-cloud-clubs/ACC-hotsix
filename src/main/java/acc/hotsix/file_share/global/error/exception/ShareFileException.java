package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class ShareFileException extends BusinessException {
    public ShareFileException() {
        super(ExceptionCode.SHARE_ERROR);
    }
}
