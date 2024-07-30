package acc.hotsix.file_share.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DirectoryPathValidator.class)
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectoryPath {
    String message() default "Invalid directory path format. Must start with '/' and contain only alphanumeric characters and underscores.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
