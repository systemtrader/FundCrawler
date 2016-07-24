package demo.funds.fundcrawler.vo;

/**
 * Created by Joshua on 2016/7/24.
 */
public class FundNetVo {

    /**
     * 当前工作日
     */
    private String dateStr;

    /**
     * 基金当前净值
     */
    private String currentNet;

    /**
     * 累计净值
     */
    private String accumulatedNet;

    /**
     * 日增长率
     */
    private String dailyGrowthRate;

    /**
     * 能否申购
     */
    private boolean canPurchase;

    /**
     * 能否赎回
     */
    private boolean canRedeem;


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

    public boolean isCanPurchase() {
        return canPurchase;
    }

    public void setCanPurchase(boolean canPurchase) {
        this.canPurchase = canPurchase;
    }

    public boolean isCanRedeem() {
        return canRedeem;
    }

    public void setCanRedeem(boolean canRedeem) {
        this.canRedeem = canRedeem;
    }

    public String getDailyGrowthRate() {
        return dailyGrowthRate;
    }

    public void setDailyGrowthRate(String dailyGrowthRate) {
        this.dailyGrowthRate = dailyGrowthRate;
    }

    public String getAccumulatedNet() {
        return accumulatedNet;
    }

    public void setAccumulatedNet(String accumulatedNet) {
        this.accumulatedNet = accumulatedNet;
    }
}
