package acc.hotsix.file_share.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class FileTypeValidator implements ConstraintValidator<ValidFileType, MultipartFile> {
    private static final Set<String> INVALID_CONTENT_TYPES = Set.of(
            "application/x-msdownload",     // for .exe, .dll
            "application/x-sh",             // for shell scripts
            "application/x-csh",            // for C shell scripts
            "application/x-php",            // for PHP
            "application/java-archive",     // for JAR
            "application/x-rar-compressed", // for RAR
            "application/zip"               // for ZIP
    );

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Validation for null or empty file can be handled by @NotNull or @NotEmpty
        }

        String contentType = file.getContentType();
        return contentType == null || !INVALID_CONTENT_TYPES.contains(contentType);
    }
}
