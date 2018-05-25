package payments;

import dto.PaymentInitialDTO;
import exception.ValidateAmountException;
import exception.ValidateDateException;
import exception.ValidatePercentException;
import exception.ValidateTermException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import enums.TypeSortEnum;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Евгений on 18.05.2018.
 */
public class PaymentInitial implements PaymentsOperaions {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    private PaymentInitialDTO dto;

    private List<Payment> paymentList = new ArrayList<Payment>();

    PaymentInitial(PaymentInitialDTO dto) {
        this.dto = dto;
    }

    public PaymentInitial(String filename) {
        readandInitialInputParams(filename);
    }


    private void readandInitialInputParams(String filename) {
        FileInputStream inp;
        try {
            final String dir = System.getProperty("user.dir").concat("\\src\\main\\resources\\input\\");
            inp = new FileInputStream(dir.concat(filename));
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден");
            return;
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(inp, Charset.forName("UTF-8")));
        String line;
        final Set<String> inputs = new HashSet<String>();
        try {
            while((line = reader.readLine()) != null) {
                inputs.add(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла");
            return;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Ошибка закрытия файла");
                return;
            }
        }

        Map<String,String> inpDTO = new HashMap<String, String>();

        for (String inpSplit : inputs) {
            String[] spl = inpSplit.split(":");
            inpDTO.put(spl[0].toUpperCase(), spl[1]);
        }

        dto = new PaymentInitialDTO(inpDTO);
    }

    @Override
    public String toString() {
        return "payments.PaymentInitial{" +
                "dto=" + dto +
                '}';
    }

    public void checkInputParamsAndGetDefault() {
        try {
            /**
             * По всем 4 параметрам зададим значения по умолчанию
             * если они заданы некорректно
             */
            Class cl = Class.forName("dto.PaymentInitialDTO");
            int i = cl.getDeclaredFields().length;
            while (i-->0) {

                try {
                    checkInputParams();
                } catch (ValidateDateException e) {
                    System.err.println(e.getMessage());
                    final Date dateDefault = e.getDefault();
                    System.err.println("Дата по умолчанию: "+formatter.format(dateDefault));
                    dto.setDate(dateDefault);
                } catch (ValidateTermException e) {
                    System.err.println(e.getMessage());
                    final Integer termDefault = e.getDefault();
                    System.err.println("Срок по умолчанию: "+termDefault);
                    dto.setTerm(termDefault);
                } catch (ValidateAmountException e ) {
                    System.err.println(e.getMessage());
                    final BigDecimal amountDefault = e.getDefault();
                    System.err.println("Сумма по умолчанию: "+amountDefault);
                    dto.setAmount(amountDefault);
                } catch (ValidatePercentException e ) {
                    System.err.println(e.getMessage());
                    final BigDecimal percentDefault = e.getDefault();
                    System.err.println("Процент по умолчанию: "+percentDefault);
                    dto.setPercent(percentDefault);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void buildExcel() {
        final Workbook wb = new HSSFWorkbook();
        final Sheet sheet = wb.createSheet("График плановых платежей");
        final String dir = System.getProperty("user.dir").concat("\\src\\main\\resources\\output\\");
        try {
            FileOutputStream outp  = new FileOutputStream(dir.concat("payments.xls"));
            try {
                CellStyle styleGray = wb.createCellStyle();
                styleGray.setFillPattern(CellStyle.SOLID_FOREGROUND);
                styleGray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

                final Font fontTitle = wb.createFont();
                fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
                fontTitle.setFontName("Cambria");
                fontTitle.setFontHeightInPoints((short) 12);
                fontTitle.setColor(IndexedColors.BLACK.getIndex());

                styleGray.setAlignment(CellStyle.ALIGN_CENTER);
                styleGray.setVerticalAlignment(CellStyle.ALIGN_CENTER);
                styleGray.setFont(fontTitle);

                CellStyle styleYellow = wb.createCellStyle();
                styleYellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
                styleYellow.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                styleYellow.setAlignment(CellStyle.ALIGN_CENTER);
                styleYellow.setVerticalAlignment(CellStyle.ALIGN_CENTER);


                Row titleRow_1  = sheet.createRow(0);
                Cell titleCell_1 = titleRow_1.createCell(2);
                titleCell_1.setCellValue("График плановых платежей");
                titleCell_1.setCellStyle(styleGray);

                Row rowParam1  = sheet.createRow(3);
                Cell cellParams1_1 = rowParam1.createCell(2);
                cellParams1_1.setCellValue("Сумма заема");
                cellParams1_1.setCellStyle(styleGray);
                Cell cellParams1_2 = rowParam1.createCell(3);
                cellParams1_2.setCellValue(dto.getAmount().toString());

                Row rowParam2  = sheet.createRow(4);
                Cell cellParams2_1 = rowParam2.createCell(2);
                cellParams2_1.setCellValue("Процентная ставка");
                cellParams2_1.setCellStyle(styleGray);
                Cell cellParams2_2 = rowParam2.createCell(3);
                cellParams2_2.setCellValue(dto.getPercent().toString());

                Row rowParam3  = sheet.createRow(5);
                Cell cellParams3_1 = rowParam3.createCell(2);
                cellParams3_1.setCellValue("Срок(мес)");
                cellParams3_1.setCellStyle(styleGray);
                Cell cellParams3_2 = rowParam3.createCell(3);
                cellParams3_2.setCellValue(dto.getTerm().toString());

                final Row rowParam4  = sheet.createRow(6);
                final Cell cellParams4_1 = rowParam4.createCell(2);
                cellParams4_1.setCellValue("Дата заема");
                cellParams4_1.setCellStyle(styleGray);
                final Cell cellParams4_2 = rowParam4.createCell(3);
                cellParams4_2.setCellValue(formatter.format(dto.getDate()));


                final Row rowTitleGPP  = sheet.createRow(9);
                final Cell cellTitleGPP_1 = rowTitleGPP.createCell(2);
                cellTitleGPP_1.setCellValue("Дата");
                cellTitleGPP_1.setCellStyle(styleGray);
                final Cell cellTitleGPP_2 = rowTitleGPP.createCell(3);
                cellTitleGPP_2.setCellValue("Основной долг");
                cellTitleGPP_2.setCellStyle(styleGray);
                final Cell cellTitleGPP_3 = rowTitleGPP.createCell(4);
                cellTitleGPP_3.setCellValue("Проценты");
                cellTitleGPP_3.setCellStyle(styleGray);
                final Cell cellTitleGPP_4 = rowTitleGPP.createCell(5);
                cellTitleGPP_4.setCellStyle(styleGray);
                cellTitleGPP_4.setCellValue("Сумма платежа");

                int start_row = 10;
                for (Payment payment : paymentList) {
                    final Row itemRow = sheet.createRow(start_row++);
                    final Cell itemCellDate = itemRow.createCell(2);
                    itemCellDate.setCellValue(formatter.format(payment.datePay));
                    itemCellDate.setCellStyle(styleYellow);
                    final Cell itemCellOD = itemRow.createCell(3);
                    itemCellOD.setCellValue(payment.od.doubleValue());
                    itemCellOD.setCellStyle(styleYellow);
                    itemCellOD.setCellType(Cell.CELL_TYPE_NUMERIC);
                    final Cell itemCellPercent = itemRow.createCell(4);
                    itemCellPercent.setCellValue(payment.percent.doubleValue());
                    itemCellPercent.setCellType(Cell.CELL_TYPE_NUMERIC);
                    itemCellPercent.setCellStyle(styleYellow);
                    final Cell itemCellAmount = itemRow.createCell(5);
                    itemCellAmount.setCellValue(payment.amount.doubleValue());
                    itemCellAmount.setCellStyle(styleYellow);
                    itemCellAmount.setCellType(Cell.CELL_TYPE_NUMERIC);
                }

                sheet.setColumnWidth(2, 10000);
                sheet.setColumnWidth(3, 5000);
                sheet.setColumnWidth(4, 5000);
                sheet.setColumnWidth(5, 5000);

                wb.write(outp);
            } catch (IOException e) {
                System.err.println("Ошибка записи в файл");
                return;
            } finally {
                try {
                    outp.close();
                } catch (IOException e) {
                    System.err.println("Ошибка закрытия потока вывода!!!");
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Ошибка создания файла Excel");
            return;
        }


    }


    private static class Payment {

        private Date datePay;
        private BigDecimal od;
        private BigDecimal percent;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        private BigDecimal amount;


        public void setDatePay(Date datePay) {
            this.datePay = datePay;
        }

        @Override
        public String toString() {
            return "Payment{" +
                    "datePay=" + datePay +
                    ", od=" + od +
                    ", percent=" + percent +
                    ", amount=" + amount +
                    '}';
        }

        public void setOd(BigDecimal od) {
            this.od = od;
        }


        public void setPercent(BigDecimal percent) {
            this.percent = percent;
        }



    }

    public void checkInputParams() throws RuntimeException {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        if (dto.getDate()==null || dto.getDate().compareTo(calendar.getTime())<0) {
            throw new ValidateDateException("Не прошла валидация по дате.");
        }

        if (dto.getTerm()==null || dto.getTerm()<=0 ||  dto.getTerm()>36 ) {
            throw new ValidateTermException("Не прошла валидация по сроку.");
        }

        if (dto.getPercent()==null ||
                dto.getPercent().compareTo(BigDecimal.ZERO) <= 0 ||
                dto.getPercent().compareTo(new BigDecimal("20")) > 0 ) {
            throw new ValidatePercentException("Не прошла валидация по проценту.");
        }

        if (dto.getAmount()==null ||
                dto.getAmount().compareTo(BigDecimal.ZERO) <= 0 ||
                dto.getAmount().compareTo(new BigDecimal("500000")) > 0 ) {
            throw new ValidateAmountException("Не прошла валидация по сумме.");
        }
    }

    Comparator<Payment> sort_date_asc() {
        return new Comparator<Payment>() {
            public int compare(Payment o1, Payment o2) {
                final Date date1 = o1.datePay;
                final Date date2 = o2.datePay;
                return date1.compareTo(date2);
            }
        };
    }

    Comparator<Payment> sort_date_desc() {
        return new Comparator<Payment>() {
            public int compare(Payment o1, Payment o2) {
                final Date date1 = o1.datePay;
                final Date date2 = o2.datePay;
                return date2.compareTo(date1);
            }
        };
    }

    public void sorting(TypeSortEnum sort) {
        switch (sort) {
            case DATE_ASC:
                Collections.sort(paymentList, sort_date_asc());
                break;
            case DATE_DESC:
                Collections.sort(paymentList, sort_date_desc());
                break;
        }
    }

    public void calculate() throws ParseException {

        BigDecimal percent = dto.getPercent().divide(new BigDecimal(1200), 10, RoundingMode.HALF_UP);
        BigDecimal div = BigDecimal.ONE.add(percent);
        BigDecimal divPow = div.pow(dto.getTerm());
        BigDecimal diffDiv = divPow.subtract(BigDecimal.ONE);
        BigDecimal fract = percent.divide(diffDiv, 10,  RoundingMode.HALF_UP);
        BigDecimal fractDiff = fract.add(percent);
        BigDecimal paymentAnnuitet = dto.getAmount().multiply(fractDiff).setScale(2, RoundingMode.HALF_UP);

        Date lastDate = dto.getDate();
        Calendar calendar = Calendar.getInstance();

        BigDecimal rest = dto.getAmount();

        for (int i = 0; i < dto.getTerm(); i++) {
            calendar.setTime(lastDate);
            calendar.add(Calendar.MONTH, 1);
            lastDate = calendar.getTime();

            final BigDecimal percentNew = rest.multiply(dto.getPercent())
                    .divide(new BigDecimal("100"))
                    .divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            final BigDecimal odNew = paymentAnnuitet.subtract(percentNew)
                    .setScale(2, RoundingMode.HALF_UP);

            rest = rest.subtract(odNew);

            final Payment payment = new Payment();
            payment.setDatePay(lastDate);
            payment.setPercent(percentNew);
            payment.setOd(odNew);
            payment.setAmount(percentNew.add(odNew));
            paymentList.add(payment);
        }

    }

}
