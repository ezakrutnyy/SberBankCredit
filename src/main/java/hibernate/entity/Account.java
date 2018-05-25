package hibernate.entity;



import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Евгений on 24.05.2018.
 */

@Entity
@Table( name = "TACCOUNT")
public class Account {

    public Account() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @Column(name = "ACCNUMBER", unique = true, nullable = false)
    private String accNumber;

    public void setId(Long id) {
        this.id = id;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }



    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccNumber() {
        return accNumber;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", accNumber='" + accNumber + '\'' +
                '}';
    }
}
