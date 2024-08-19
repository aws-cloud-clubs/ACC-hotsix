package acc.hotsix.file_share.global.error;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final String message;
    private final String code;

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.message = exceptionCode.getMessage();
        this.code = exceptionCode.getCode();
    }
}
