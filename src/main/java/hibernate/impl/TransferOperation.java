package hibernate.impl;

import hibernate.entity.Account;
import exception.TransferException;
import hibernate.service.AccountService;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by Евгений on 25.05.2018.
 */
public class TransferOperation {

    public void transferAmount(final Account accountFrom,
                               final Account accountTo,
                               final BigDecimal amount) throws TransferException {

        final Long idFrom = accountFrom.getId();
        final Long idTo = accountTo.getId();

        final Account lock1 = idFrom < idTo ? accountFrom : accountTo;
        final Account lock2 = idFrom < idTo ? accountTo : accountFrom;

        /**
         * Same account
         * Если по условию, один и тот же счет  может быть как источником так и приемником,
         * чтобы избежать deadLock нужно ввести еще один объект
         * private static final Object globalLock = new Object();
         * и перед синхронизацией по lock1 и lock2, необходима синхронизация по globalLock
         *
         * */
        if (accountFrom.getAccNumber().equals(accountTo.getAccNumber())) {
            throw new TransferException("Ошибка операции. Источник и приемник имеют общий счет.");
        }

        synchronized (lock1) {
            synchronized (lock2) {
                changeQueryBalance(accountFrom, accountTo, amount);
            }
        }
    }

    private void changeQueryBalance(final Account accountFrom,
                                    final Account accountTo,
                                    final BigDecimal amount) {
        AccountService service = new AccountService();
        try {
            service.transferAcount(accountFrom, accountTo, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
