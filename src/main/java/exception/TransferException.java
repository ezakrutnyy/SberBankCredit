package exception;

/**
 * Created by Евгений on 23.05.2018.
 */
public class TransferException  extends  RuntimeException {

    public TransferException(String error) {
        super(error);
    }
}