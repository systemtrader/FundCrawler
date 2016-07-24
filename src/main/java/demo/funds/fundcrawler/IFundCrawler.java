package demo.funds.fundcrawler;

import demo.funds.fundcrawler.vo.FundBounsVo;
import demo.funds.fundcrawler.vo.FundNetVo;

import java.util.Date;
import java.util.List;

/**
 * Created by Joshua on 2016/7/24.
 */
public interface IFundCrawler {

    /**
     * 查看某一个工作日某只基金的净值，
     * 若该日期不是工作日，则返回该天所属的工作日的净值信息
     *
     * @param dateStr 日期
     * @param fundCode 基金代码
     *
     * @return 基金净值vo
     */
    FundNetVo getFundNet(String dateStr, String fundCode);

    /**
     * 查看某只基金的分红情况
     * @param fundCode
     *
     * @return 分红列表
     */
    List<FundBounsVo> getBonusInfo(String fundCode);

}
