package acc.hotsix.file_share.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionResponse {
    private final String message;
    private final String code;
}
