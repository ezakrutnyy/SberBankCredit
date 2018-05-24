package dto;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Евгений on 18.05.2018.
 */
public class PaymentInitialDTO {

    private final String AMOUNT = "AMOUNT";
    private final String PERCENT = "PERCENT";
    private final String DATE = "DATE";
    private final String TERM = "TERM";

    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public PaymentInitialDTO(Map<String, String> inpDTO) {
        if (inpDTO.get(AMOUNT)!=null) {
            this.amount = new BigDecimal(inpDTO.get(AMOUNT));
        }
        if (inpDTO.get(PERCENT)!=null) {
            this.percent = new BigDecimal(inpDTO.get(PERCENT));
        }
        if (inpDTO.get(DATE)!=null) {
            try {
                this.date = formatter.parse(inpDTO.get(DATE));
            } catch (ParseException e) {
                System.err.println("Ошибка чтения даты, неверный формат");
            }
        }
        if (inpDTO.get(TERM)!=null) {
            this.term = Integer.valueOf(inpDTO.get(TERM));
        }
    }

    @Override
    public String toString() {
        return "dto.PaymentInitialDTO{" +
                "term=" + term +
                ", amount=" + amount +
                ", date=" + formatter.format(date) +
                ", percent=" + percent +
                '}';
    }

    private Integer term;
    private BigDecimal amount;
    private Date date;
    private BigDecimal percent;

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }
}
