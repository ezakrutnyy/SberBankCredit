
import hibernate.entity.Account;
import org.junit.Test;
import hibernate.service.AccountService;
import hibernate.impl.TransferOperation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Евгений on 25.05.2018.
 */
public class TransferHibernateTest {

    // input params
    final private String accountNumberFrom = "40817810340000701301";
    final private String accountNumberTo = "40817810340000701302";
    final private BigDecimal amount = new BigDecimal(15000);

    @Test
    public void transferHbnSingleThreadTest() throws SQLException {

        final AccountService service = new AccountService();
        List<Account> lst = service.getResult();
        assertEquals(lst.size(),9);

        Account accountFrom = service.getByAccNumber(accountNumberFrom);
        Account accountTo =  service.getByAccNumber(accountNumberTo);

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
    public void transferHbnThreadsTest() throws SQLException, InterruptedException {

        final AccountService service = new AccountService();
        List<Account> lst = service.getResult();
        assertEquals(lst.size(),9);

        Account accountFrom = service.getByAccNumber(accountNumberFrom);
        Account accountTo =  service.getByAccNumber(accountNumberTo);

        MyTransferHbmThread tr1 = new MyTransferHbmThread(accountFrom, accountTo);
        MyTransferHbmThread tr2 = new MyTransferHbmThread(accountTo, accountFrom);
        MyTransferHbmThread tr3 = new MyTransferHbmThread(accountFrom, accountTo);
        MyTransferHbmThread tr4 = new MyTransferHbmThread(accountTo, accountFrom);
        MyTransferHbmThread tr5 = new MyTransferHbmThread(accountFrom, accountTo);
        MyTransferHbmThread tr6 = new MyTransferHbmThread(accountTo, accountFrom);

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

class MyTransferHbmThread extends  Thread {

    private TransferOperation transfer = new TransferOperation();
    final private BigDecimal amount = new BigDecimal(15000);

    Account accountFrom;
    Account accountTo;

    MyTransferHbmThread(Account accountFrom, Account accountTo) {
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