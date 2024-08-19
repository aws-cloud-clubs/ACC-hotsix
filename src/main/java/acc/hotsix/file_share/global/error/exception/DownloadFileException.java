package acc.hotsix.file_share.global.error.exception;

import acc.hotsix.file_share.global.error.ExceptionCode;

public class DownloadFileException extends BusinessException{
    public DownloadFileException() {
        super(ExceptionCode.DOWNLOAD_ERROR);
    }
}
