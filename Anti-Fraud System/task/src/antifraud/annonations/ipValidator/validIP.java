package antifraud.annonations.ipValidator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Pattern(regexp = "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}", message = "Invalid IP format")
@NotBlank(message = "IP cannot be blank")
@NotNull(message = "IP cannot be null")
@Constraint(validatedBy = {})
public @interface validIP {

    String message() default "";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
}
