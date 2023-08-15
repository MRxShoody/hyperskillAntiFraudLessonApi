package antifraud.annonations.phoneValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
//@Pattern(regexp = "^\\d{16}$", message = "Card Number must be 16 digits")
@NotBlank(message = "Card Number cannot be blank")
@NotNull(message = "Card Number cannot be null")
@Constraint(validatedBy = {phoneNumberValidator.class})
public @interface validCardNumber {

    String message() default "";
    String regexp() default "^\\d{16}$";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
}

