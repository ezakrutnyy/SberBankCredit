package jdbc.impl;

import jdbc.entity.AccountDTO;
import exception.TransferException;
import jdbc.service.AccountService;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by Евгений on 23.05.2018.
 */
public class TransferOperation {

    public void transferAmount(final AccountDTO accountFrom,
                               final AccountDTO accountTo,
                               final BigDecimal amount) throws TransferException {

        final Long idFrom = accountFrom.getId();
        final Long idTo = accountTo.getId();

        final AccountDTO lock1 = idFrom < idTo ? accountFrom : accountTo;
        final AccountDTO lock2 = idFrom < idTo ? accountTo : accountFrom;

        /**
         * Same account
         * Если по условию, один и тот же счет  может быть как источником так и приемником,
         * чтобы избежать deadLock нужно ввести еще один объект
         * private static final Object globalLock = new Object();
         * и перед синхронизацией по lock1 и lock2, необходима синхронизация по globalLock
         *
         * */
        if (accountFrom.getNumber().equals(accountTo.getNumber())) {
            throw new TransferException("Ошибка операции. Источник и приемник имеют общий счет.");
        }

        synchronized (lock1) {
            synchronized (lock2) {
                changeQueryBalance(accountFrom, accountTo, amount);
            }
        }
    }

    private void changeQueryBalance(final AccountDTO accountFrom,
                              final AccountDTO accountTo,
                              final BigDecimal amount) {
        AccountService service = new AccountService();
        try {
            service.transferAcount(accountFrom, accountTo, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
