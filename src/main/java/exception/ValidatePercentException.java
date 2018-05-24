package exception;

import java.math.BigDecimal;

/**
 * Created by Евгений on 18.05.2018.
 */
public class ValidatePercentException extends RuntimeException {

    final private static BigDecimal PERCENT_DEFAULT = new BigDecimal("20");

    public ValidatePercentException(String errorText) {
        super(errorText);
    }

    public BigDecimal getDefault() {
        return PERCENT_DEFAULT;
    }

}
