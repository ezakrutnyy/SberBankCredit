package jdbc.dao;

import jdbc.entity.AccountDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Евгений on 24.05.2018.
 */
public interface TAccountDAO {

    // insert row
    void insert(AccountDTO account) throws SQLException;

    // getResult
    List<AccountDTO> getResult() throws SQLException;

    //  get row by params();
    AccountDTO getByAccNumber(String number) throws SQLException;

    //  update row
    void transferAcount(AccountDTO accountFrom, AccountDTO accountTo, BigDecimal amount) throws SQLException;

    //  delete row
    void delete(AccountDTO account) throws SQLException;
}
