package acc.hotsix.file_share.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = FileTypeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileType {
    String message() default "Invalid file type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
