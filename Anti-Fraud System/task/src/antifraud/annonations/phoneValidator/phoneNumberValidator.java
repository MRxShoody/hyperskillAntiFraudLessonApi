package antifraud.annonations.phoneValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.engine.messageinterpolation.util.InterpolationHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class phoneNumberValidator implements ConstraintValidator<validCardNumber, CharSequence> {

    private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );
    private java.util.regex.Pattern pattern;
    private String escapedRegexp;

    @Override
    public void initialize(validCardNumber parameters) {

        try {
            pattern = java.util.regex.Pattern.compile( parameters.regexp());
        }
        catch (PatternSyntaxException e) {
            throw LOG.getInvalidRegularExpressionException( e );
        }

        escapedRegexp = InterpolationHelper.escapeMessageParameter( parameters.regexp() );
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {

        if ( value == null ) {
            return true;
        }

        if ( constraintValidatorContext instanceof HibernateConstraintValidatorContext ) {
            constraintValidatorContext.unwrap( HibernateConstraintValidatorContext.class ).addMessageParameter( "regexp", escapedRegexp );
        }

        Matcher m = pattern.matcher( value );

        return m.matches() && checkLuhn(value.toString());
    }

    boolean checkLuhn(String cardNo)
    {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}
