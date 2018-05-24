package dto;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Евгений on 23.05.2018.
 */
public class AccountDTO {


    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private BigDecimal balance;
    private String number;

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getNumber() {
        return number;
    }


    @Override
    public String toString() {
        return "dto.Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", number='" + number + '\'' +
                '}';
    }

}