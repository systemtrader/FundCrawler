package demo.funds.fundcrawler.vo;

/**
 * Created by Joshua on 2016/7/24.
 */
public class FundBounsVo {
    /*
    权益登记日
     */
    private String EquityRegistrationDateStr;

    /*
    除息日
     */
    private String ExDividendDateStr;

    /*
    分红
     */
    private String bouns;

    /*
    红利发放日
     */
    private String DividendPaymentDateStr;

    public String getEquityRegistrationDateStr() {
        return EquityRegistrationDateStr;
    }

    public void setEquityRegistrationDateStr(String equityRegistrationDateStr) {
        EquityRegistrationDateStr = equityRegistrationDateStr;
    }

    public String getExDividendDateStr() {
        return ExDividendDateStr;
    }

    public void setExDividendDateStr(String exDividendDateStr) {
        ExDividendDateStr = exDividendDateStr;
    }

    public String getBouns() {
        return bouns;
    }

    public void setBouns(String bouns) {
        this.bouns = bouns;
    }

    public String getDividendPaymentDateStr() {
        return DividendPaymentDateStr;
    }

    public void setDividendPaymentDateStr(String dividendPaymentDateStr) {
        DividendPaymentDateStr = dividendPaymentDateStr;
    }
}
