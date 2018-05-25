package payments;

import enums.TypeSortEnum;

import java.text.ParseException;

/**
 * Created by Евгений on 18.05.2018.
 */
public interface PaymentsOperaions {
    void calculate() throws ParseException;
    void sorting(TypeSortEnum sort);
    void checkInputParams();
    void checkInputParamsAndGetDefault();
    void buildExcel();
}
