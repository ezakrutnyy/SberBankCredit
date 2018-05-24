package exception;

/**
 * Created by Евгений on 18.05.2018.
 */
public class ValidateTermException  extends  RuntimeException {

    final private static Integer TERM_DEFAULT = 36;

    public ValidateTermException(String errorText) {
        super(errorText);
    }

    public Integer getDefault() {
        return TERM_DEFAULT;
    }
}
