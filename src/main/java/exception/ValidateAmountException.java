package exception;

import java.math.BigDecimal;

/**
 * Created by Евгений on 18.05.2018.
 */
public class ValidateAmountException extends  RuntimeException {

    final private static BigDecimal AMOUNT_DEFAULT = new BigDecimal("500000");

    public ValidateAmountException(String errorText) {
        super(errorText);
    }

    public BigDecimal getDefault() {
        return AMOUNT_DEFAULT;
    }
}
