package hibernate.service;

import hibernate.dao.TAccountDAO;
import hibernate.entity.Account;
import exception.TransferException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import hibernate.util.SessionUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Евгений on 25.05.2018.
 */
public class AccountService extends SessionUtil implements TAccountDAO {

    private static final String ID = "ID";
    private static final String ACCNUMBER = "ACCNUMBER";
    private static final String BALANCE = "BALANCE";

    private static final int BATCH_SIZE = 5;

    @Override
    public void insert(Account account) throws SQLException {
        // Start transaction and open session
        openTransactionSession();

        final Session session = getSession();
        session.save(account);

        // Close transaction and  session
        closeTransactionSession();
    }

    @Override
    public List<Account> getResult() throws SQLException {
        // Start transaction and open session
        openTransactionSession();

        final String sql = "SELECT ID, ACCNUMBER, BALANCE FROM TACCOUNT ORDER BY ID LIMIT :limit OFFSET :offset";
        boolean flagEndCursor = false;
        int offset = 0;

        final Session session = getSession();

        final List<Account> resList = new ArrayList<Account>();
        while (!flagEndCursor) {
            final Query query = session.createNativeQuery(sql).addEntity(Account.class);
            query.setParameter("limit", BATCH_SIZE);
            query.setParameter("offset", offset);
            final List<Account> resInnerList = query.list();
            resList.addAll(resInnerList);
            if (resInnerList.size() != BATCH_SIZE) {
                flagEndCursor = true;
            }
            offset += BATCH_SIZE;
        }

        // Close transaction and  session
        closeTransactionSession();
        return resList;
    }

    @Override
    public Account getByAccNumber(String number) throws SQLException {
        // Start transaction and open session
        openTransactionSession();

        final Session session = getSession();
        final String sql = "SELECT ID, ACCNUMBER, BALANCE FROM TACCOUNT WHERE ACCNUMBER = :accNumber";
        final Query query = session.createNativeQuery(sql).addEntity(Account.class);
        query.setParameter("accNumber",number);
        final Account account = (Account) query.getSingleResult();

        // Close transaction and  session
        closeTransactionSession();
        return account;
    }

    @Override
    public void transferAcount(Account accountFrom, Account accountTo, BigDecimal amount) throws SQLException {

        // Начитаем актуальные данные по счету источнику
        final Account actualAccountFrom = getByAccNumber(accountFrom.getAccNumber());
        final Account actualAccountTo = getByAccNumber(accountTo.getAccNumber());

        // Start transaction and open session
        openTransactionSession();
        final Session session = getSession();

        // Insufficient fund on Account
        if (actualAccountFrom.getBalance().compareTo(amount) < 0) {
            throw new TransferException("Ошибка операции. Недостаточно средств на балансе счета.");
        }
        actualAccountFrom.setBalance(
                actualAccountFrom.getBalance().subtract(amount));
        actualAccountTo.setBalance(
                actualAccountTo.getBalance().add(amount));

        session.update(actualAccountFrom);
        session.update(actualAccountTo);

        // Close transaction and  session
        closeTransactionSession();
    }

    @Override
    public void delete(Account account) throws SQLException {
        // Start transaction and open session
        openTransactionSession();

        final Session session = getSession();
        session.remove(account);

        // Close transaction and  session
        closeTransactionSession();

    }
}
