import org.junit.Test;
import payments.PaymentInitial;
import util.TypeSortEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Евгений on 24.05.2018.
 */
public class SchesulePaymentsGPPTest {

    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    final static String filename = "input.txt";

    @Test
    public void gppTest() throws ParseException {
        /**
         * payments.PaymentInitial имеет два конструктора
         * для чтения из файла + для создания отдельной DTO
         *
         * Список методов
         * checkInputParamsAndGetDefault - проверка данных + запись default значений
         * checkInputParams - просто проверка данных, и выход из приложения в случае Exception
         * calculate - метод расчета графика аннуитетных платежей по входным данным
         * sorting - сортирует список вывода по дате DATE_ASC - прямая сортировка,  DATE_DESC - обратная
         * buildExcel - построение эксель файла
         *
         * */
        final PaymentInitial payInit = new PaymentInitial(filename);
        payInit.checkInputParamsAndGetDefault();
        payInit.calculate();
        payInit.sorting(TypeSortEnum.DATE_ASC);
        payInit.buildExcel();
    }
}
