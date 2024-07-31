package acc.hotsix.file_share.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DirectoryPathValidator implements ConstraintValidator<DirectoryPath, String> {
    // 경로가 '/'로 시작하고 알파벳, 숫자, 밑줄만 포함되어야 한다.
    private static final String DIRECTORY_PATH_PATTERN = "^(/([a-zA-Z0-9_]+)*)*$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(DIRECTORY_PATH_PATTERN);
    }
}
