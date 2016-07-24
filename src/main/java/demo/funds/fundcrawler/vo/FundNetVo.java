package demo.funds.fundcrawler.vo;

/**
 * Created by Joshua on 2016/7/24.
 */
public class FundNetVo {

    /*
    当天
     */
    private String dateStr;

    /*
    基金当前净值
     */
    private String currentNet;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getCurrentNet() {
        return currentNet;
    }

    public void setCurrentNet(String currentNet) {
        this.currentNet = currentNet;
    }

}
