import dto.AccountDTO;
import org.junit.Test;
import service.AccountService;
import transfer.TransferOperation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Евгений on 24.05.2018.
 */
public class TransferTest {

    // input params
    final private String accountNumberFrom = "40817810340000701301";
    final private String accountNumberTo = "40817810340000701302";
    final private BigDecimal amount = new BigDecimal(15000);

    @Test
    public void transferSingleThreadTest() throws SQLException {
        final AccountService service = new AccountService();
        List<AccountDTO> lst = service.getResult();
        assertEquals(lst.size(),9);

        AccountDTO accountFrom = service.getByAccNumber(accountNumberFrom);
        AccountDTO accountTo =  service.getByAccNumber(accountNumberTo);

        assertEquals(accountFrom.getBalance().compareTo(new BigDecimal(100000)), 0);
        assertEquals(accountTo.getBalance().compareTo(new BigDecimal(120000)), 0);

        TransferOperation transfer = new TransferOperation();

        int i = 3;
        int j = 3;

        while(i-->0) {
            transfer.transferAmount(accountFrom, accountTo, amount);
        }

        accountFrom = service.getByAccNumber(accountNumberFrom);
        accountTo =  service.getByAccNumber(accountNumberTo);

        assertEquals(accountFrom.getBalance().compareTo(new BigDecimal(55000)), 0);
        assertEquals(accountTo.getBalance().compareTo(new BigDecimal(165000)), 0);

        while(j-->0) {
            transfer.transferAmount(accountTo, accountFrom , amount);
        }

        accountFrom = service.getByAccNumber(accountNumberFrom);
        accountTo =  service.getByAccNumber(accountNumberTo);

        assertEquals(accountFrom.getBalance().compareTo(new BigDecimal(100000)), 0);
        assertEquals(accountTo.getBalance().compareTo(new BigDecimal(120000)), 0);
    }


    @Test
    public void transferThreadsTest() throws SQLException, InterruptedException {

        final AccountService service = new AccountService();
        List<AccountDTO> lst = service.getResult();
        assertEquals(lst.size(),9);

        AccountDTO accountFrom = service.getByAccNumber(accountNumberFrom);
        AccountDTO accountTo =  service.getByAccNumber(accountNumberTo);

        MyTransferThread tr1 = new MyTransferThread(accountFrom, accountTo);
        MyTransferThread tr2 = new MyTransferThread(accountTo, accountFrom);
        MyTransferThread tr3 = new MyTransferThread(accountFrom, accountTo);
        MyTransferThread tr4 = new MyTransferThread(accountTo, accountFrom);
        MyTransferThread tr5 = new MyTransferThread(accountFrom, accountTo);
        MyTransferThread tr6 = new MyTransferThread(accountTo, accountFrom);

        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(tr1);
        pool.execute(tr2);
        pool.execute(tr3);
        pool.execute(tr4);
        pool.execute(tr5);
        pool.execute(tr6);

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Threadpool finished");

        accountFrom = service.getByAccNumber(accountNumberFrom);
        accountTo =  service.getByAccNumber(accountNumberTo);

        assertEquals(accountFrom.getBalance().compareTo(new BigDecimal(100000)), 0);
        assertEquals(accountTo.getBalance().compareTo(new BigDecimal(120000)), 0);
    }
}


class MyTransferThread extends  Thread {

    private TransferOperation transfer = new TransferOperation();
    final private BigDecimal amount = new BigDecimal(15000);

    AccountDTO accountFrom;
    AccountDTO accountTo;

    MyTransferThread(AccountDTO accountFrom, AccountDTO accountTo) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    @Override
    public void run() {
        int i = 3;
        while(i-->0) {
            System.out.println(currentThread());
            transfer.transferAmount(accountFrom, accountTo, amount);
        }
    }
}