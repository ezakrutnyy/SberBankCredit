package exception;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Евгений on 18.05.2018.
 */

public class ValidateDateException extends  RuntimeException {

    final private static Date DATE_DEFAULT = new Date();

    public ValidateDateException(String errorText) {
        super(errorText);
    }

    public Date getDefault() {
        return DATE_DEFAULT;
    }


}

