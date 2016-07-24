package demo.funds.fundcrawler;

import demo.funds.fundcrawler.vo.FundBounsVo;
import demo.funds.fundcrawler.vo.FundNetVo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.oro.text.regex.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Joshua on 2016/7/24.
 */
public class FundCrawler implements IFundCrawler {

    static Logger LOGGER = Logger.getLogger(FundCrawler.class);
    static HttpClient client = new HttpClient();

    public FundNetVo getFundNet(String dateStr, String fundCode) {

        FundNetVo fundNetVo = new FundNetVo();
        fundNetVo.setDateStr(dateStr);

        try {
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //Date date = simpleDateFormat.parse(dateStr);
            // 偏移20天
            //String endDateStr = simpleDateFormat.format( new Date(date.getTime()+1000L*60L*60L*24L*20L) );

            // URL先写死
            StringBuilder urlStr = new StringBuilder("http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=")
                    .append(fundCode)
                    .append("&sdate=").append(dateStr)
                    .append("&edate=").append(dateStr);

            GetMethod getMethod = new GetMethod(urlStr.toString());

            client.executeMethod(getMethod);
            String resp = new String(getMethod.getResponseBody(), getMethod.getResponseCharSet());

            LOGGER.debug("请求数据源返回：" + resp);

            PatternCompiler compiler = new Perl5Compiler();
            Pattern pattern = null;

            pattern = compiler.compile("([0-9]+\\.[0-9]+)");

            PatternMatcher matcher = new Perl5Matcher();
            PatternMatcherInput matcherInput = new PatternMatcherInput(resp);
            if (matcher.contains(matcherInput, pattern)) {
                MatchResult result = matcher.getMatch();
                fundNetVo.setCurrentNet(result.group(0));
            }

            getMethod.releaseConnection();
        //} catch (ParseException e) {
        //    LOGGER.error("日期错误！" + dateStr , e);
        } catch (IOException e) {
            LOGGER.error("URL数据源异常！" , e);
        } catch (MalformedPatternException e) {
            LOGGER.error("正则表达式异常！", e);
        }

        return fundNetVo;

    }

    public List<FundBounsVo> getBonusInfo(String fundCode) {

        // 先写死
        StringBuilder urlStr = new StringBuilder("http://fund.eastmoney.com/f10/fhsp_")
                                   .append(fundCode).append(".html");

        GetMethod getMethod = new GetMethod(urlStr.toString());
        List<FundBounsVo> list = new ArrayList<FundBounsVo>();

        try {
            client.executeMethod(getMethod);
            String resp = new String(getMethod.getResponseBody(), getMethod.getResponseCharSet());

            //LOGGER.debug("请求数据源返回：" + resp);

            Document document = Jsoup.parse(resp);
            Element table = document.getElementsByAttributeValue("class", "w782 comm cfxq").get(0);
            Element tbody = table.getElementsByTag("tbody").get(0);

            LOGGER.debug("tbody:" + tbody.html());

            Elements trs = tbody.getElementsByTag("tr");
            Iterator<Element> it = trs.iterator();
            while (it.hasNext()) {
                Element e = it.next();
                Elements tds = e.getElementsByTag("td");
                FundBounsVo fundBounsVo = new FundBounsVo();
                fundBounsVo.setEquityRegistrationDateStr(tds.get(1).text());// 权益登记日
                fundBounsVo.setExDividendDateStr(tds.get(2).text());// 除息日
                String bouns = tds.get(3).text();

                PatternCompiler compiler = new Perl5Compiler();
                Pattern pattern = null;

                pattern = compiler.compile("([0-9]+\\.[0-9]+)");

                PatternMatcher matcher = new Perl5Matcher();
                PatternMatcherInput matcherInput = new PatternMatcherInput(bouns);
                if( matcher.contains(matcherInput, pattern) ) {
                    MatchResult result = matcher.getMatch();
                    fundBounsVo.setBouns(result.group(0));
                }
                fundBounsVo.setDividendPaymentDateStr(tds.get(4).text());// 红利发放日

                list.add(fundBounsVo);
            }



        } catch (IOException e) {
            LOGGER.error("数据源异常！" + urlStr.toString(), e);
        } catch (MalformedPatternException e) {
                LOGGER.error("正则表达式异常！", e);
        } catch (Exception e) {
            LOGGER.error("解析xml异常！", e);
        }

        return list;
    }


}
