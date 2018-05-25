package hibernate.domain;

import hibernate.entity.Account;
import hibernate.service.AccountService;
import hibernate.util.HibernateUtil;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by Евгений on 24.05.2018.
 */
public class Domain {
    public static void main(String[] args) throws SQLException {

        AccountService service = new AccountService();

        Account account1 = new Account();
        account1.setAccNumber("40817810340000701301");
        account1.setBalance(new BigDecimal(100000));

        Account account2 = new Account();
        account2.setAccNumber("40817810340000701302");
        account2.setBalance(new BigDecimal(120000));

        Account account3 = new Account();
        account3.setAccNumber("40817810340000701303");
        account3.setBalance(new BigDecimal(7500.45));

        Account account4 = new Account();
        account4.setAccNumber("40817810340000701304");
        account4.setBalance(new BigDecimal(9200));

        Account account5 = new Account();
        account5.setAccNumber("40817810340000701305");
        account5.setBalance(new BigDecimal(50000));

        Account account6 = new Account();
        account6.setAccNumber("40817810340000701306");
        account6.setBalance(new BigDecimal(10000));

        Account account7 = new Account();
        account7.setAccNumber("40817810340000701307");
        account7.setBalance(new BigDecimal(80000));

        Account account8 = new Account();
        account8.setAccNumber("40817810340000701308");
        account8.setBalance(new BigDecimal(90000));

        Account account9 = new Account();
        account9.setAccNumber("40817810340000701309");
        account9.setBalance(new BigDecimal(50000));

        service.insert(account1);
        service.insert(account2);
        service.insert(account3);
        service.insert(account4);
        service.insert(account5);
        service.insert(account6);
        service.insert(account7);
        service.insert(account8);
        service.insert(account9);

        HibernateUtil.shutdown();

    }
}
