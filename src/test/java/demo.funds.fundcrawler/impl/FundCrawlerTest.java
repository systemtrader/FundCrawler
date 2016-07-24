package demo.funds.fundcrawler.impl;

import demo.funds.fundcrawler.FundCrawler;
import demo.funds.fundcrawler.IFundCrawler;
import demo.funds.fundcrawler.vo.FundBounsVo;
import demo.funds.fundcrawler.vo.FundNetVo;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Joshua on 2016/7/24.
 */
public class FundCrawlerTest {

    @Test
    public void testFundNet() {
        IFundCrawler fundCrawler = new FundCrawler();
        FundNetVo fundNetVo = fundCrawler.getFundNet("2016-07-06", "270005");
        FundNetVo fundNetVoExpect = new FundNetVo();
        fundNetVoExpect.setDateStr("2016-07-06");
        fundNetVoExpect.setCurrentNet("0.9736");

        // 测试净值是否一致
        Assert.assertEquals(fundNetVo.getCurrentNet(), fundNetVoExpect.getCurrentNet());
    }

    @Test
    public void testFundBounsVos() {
        IFundCrawler fundCrawler = new FundCrawler();
        List<FundBounsVo> list = fundCrawler.getBonusInfo("110001");

    }
}
