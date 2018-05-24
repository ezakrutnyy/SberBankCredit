package service;

import dao.TAccountDAO;

import dto.AccountDTO;
import exception.TransferException;
import util.Util;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Евгений on 24.05.2018.
 */
public class AccountService extends Util implements TAccountDAO {

    private static final String ID = "ID";
    private static final String ACCNUMBER = "ACCNUMBER";
    private static final String BALANCE = "BALANCE";

    private static final int BATCH_SIZE = 100;

    @Override
    public void insert(AccountDTO account) throws SQLException {
        final Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        final String sql = "INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES (?,?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,account.getNumber());
            preparedStatement.setBigDecimal(2, account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public List<AccountDTO> getResult() throws SQLException {
        final Connection connection = getConnection();
        final List<AccountDTO> resList = new ArrayList<AccountDTO>();
        PreparedStatement preparedStatement = null;
        final String sql = "SELECT ID, ACCNUMBER, BALANCE FROM TACCOUNT ORDER BY ID LIMIT ? OFFSET ?";
        boolean flagEndCursor = false;
        int offset = 0;
        try {
            while (!flagEndCursor) {
                // split batch
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, BATCH_SIZE);
                preparedStatement.setInt(2, offset);
                final ResultSet resultSet = preparedStatement.executeQuery();

                final List<AccountDTO> resInnerList = new ArrayList<AccountDTO>();
                while(resultSet.next()) {
                    AccountDTO account = new AccountDTO();
                    account.setId(resultSet.getLong(ID));
                    account.setNumber(resultSet.getString(ACCNUMBER));
                    account.setBalance(resultSet.getBigDecimal(BALANCE));
                    resInnerList.add(account);

                }
                resList.addAll(resInnerList);
                if (resInnerList.size()!=BATCH_SIZE) {
                    flagEndCursor = true;
                }
                offset += BATCH_SIZE;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return resList;
    }

    @Override
    public AccountDTO getByAccNumber(final String number) throws SQLException {
        final Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        final String sql = "SELECT ID, ACCNUMBER, BALANCE FROM TACCOUNT WHERE ACCNUMBER = ?";
        final AccountDTO account = new AccountDTO();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, number);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                account.setId(resultSet.getLong(ID));
                account.setNumber(resultSet.getString(ACCNUMBER));
                account.setBalance(resultSet.getBigDecimal(BALANCE));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return account;
    }

    @Override
    public void transferAcount(final AccountDTO accountFrom,
                               final AccountDTO accountTo,
                               final BigDecimal amount) throws SQLException, TransferException {
        final Connection connection = getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = null;
        try {

            // Начитаем актуальные данные по счету источнику
            final AccountDTO actualAccountFrom = getByAccNumber(accountFrom.getNumber());
            final AccountDTO actualAccountTo = getByAccNumber(accountTo.getNumber());

            // Insufficient fund on dto.Account
            if (actualAccountFrom.getBalance().compareTo(amount) < 0) {
                throw new TransferException("Ошибка операции. Недостаточно средств на балансе счета.");
            }

            final String sql = "UPDATE TACCOUNT SET BALANCE = ? WHERE ID = ?";
            preparedStatement = connection.prepareStatement(sql);

            // first substring amount from account1
            preparedStatement.setBigDecimal(1, actualAccountFrom.getBalance().subtract(amount));
            preparedStatement.setLong(2, accountFrom.getId());
            preparedStatement.executeUpdate();

            // second additional amount to account2
            preparedStatement.setBigDecimal(1, actualAccountTo.getBalance().add(amount));
            preparedStatement.setLong(2, accountTo.getId());
            preparedStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void delete(AccountDTO account) throws SQLException {
        final Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        final String sql = "DELETE FROM TACCOUNT WHERE ID = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,account.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


}
