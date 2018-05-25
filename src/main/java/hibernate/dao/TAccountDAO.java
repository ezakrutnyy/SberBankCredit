package hibernate.dao;

import hibernate.entity.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Евгений on 25.05.2018.
 */
public interface TAccountDAO {

    // insert row
    void insert(Account account) throws SQLException;

    // getResult
    List<Account> getResult() throws SQLException;

    //  get row by params();
    Account getByAccNumber(String number) throws SQLException;

    //  update row
    void transferAcount(Account accountFrom, Account accountTo, BigDecimal amount) throws SQLException;

    //  delete row
    void delete(Account account) throws SQLException;
}
