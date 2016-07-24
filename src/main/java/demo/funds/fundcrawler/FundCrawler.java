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
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
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

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(dateStr);
            // 偏移20天
            String endDateStr = simpleDateFormat.format( new Date(date.getTime()+1000L*60L*60L*24L*20L) );

            // URL先写死
            StringBuilder urlStr = new StringBuilder("http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=")
                                       .append(fundCode)
                                       .append("&page=1&per=25")// 必须传per，否则默认per=10，数据丢失
                                       .append("&sdate=").append(dateStr)
                                       .append("&edate=").append(endDateStr);

            // 通过get方法获取原始信息
            GetMethod getMethod = new GetMethod(urlStr.toString());
            client.executeMethod(getMethod);
            String resp = new String(getMethod.getResponseBody(), getMethod.getResponseCharSet());

            // 提取净值信息
            PatternCompiler compiler = new Perl5Compiler();
            Pattern pattern = null;
            pattern = compiler.compile("<tbody>.+</tbody>");
            PatternMatcher matcher = new Perl5Matcher();
            PatternMatcherInput matcherInput = new PatternMatcherInput(resp);
            if (matcher.contains(matcherInput, pattern)) {
                MatchResult result = matcher.getMatch();
                Document document = Jsoup.parse(result.group(0), "", new Parser(new XmlTreeBuilder()));

                LOGGER.debug(result.group(0));

                // 只需要净值信息列表中的最后一项
                Elements trs = document.getElementsByTag("tr");
                Element tr = trs.get(trs.size()-1);
                Elements tds = tr.getElementsByTag("td");

                // 装载到vo中
                fundNetVo.setDateStr(tds.get(0).text());// 当前工作日
                fundNetVo.setCurrentNet(tds.get(1).text());// 当前净值
                fundNetVo.setAccumulatedNet(tds.get(2).text());// 累计净值
                fundNetVo.setDailyGrowthRate(tds.get(3).text());// 日增长率
                fundNetVo.setCanPurchase("开放申购".equals(tds.get(4).text()));// 能否申购
                fundNetVo.setCanRedeem("开放赎回".equals(tds.get(5).text()));// 能否赎回

            } else {
                throw new Exception();
            }

            getMethod.releaseConnection();
        } catch (ParseException e) {
            LOGGER.error("日期错误！" + dateStr , e);
        } catch (IOException e) {
            LOGGER.error("URL数据源异常！" , e);
        } catch (MalformedPatternException e) {
            LOGGER.error("正则表达式异常！", e);
        } catch (Exception e) {
            LOGGER.error("通用异常！", e);
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

            Document document = Jsoup.parse(resp);
            Element table = document.getElementsByAttributeValue("class", "w782 comm cfxq").get(0);
            Element tbody = table.getElementsByTag("tbody").get(0);

            Elements trs = tbody.getElementsByTag("tr");
            Iterator<Element> it = trs.iterator();

            // 分红信息列表在tr中
            while (it.hasNext()) {
                Element e = it.next();
                Elements tds = e.getElementsByTag("td");
                FundBounsVo fundBounsVo = new FundBounsVo();
                fundBounsVo.setEquityRegistrationDateStr(tds.get(1).text());// 权益登记日
                fundBounsVo.setExDividendDateStr(tds.get(2).text());// 除息日
                String bouns = tds.get(3).text();

                // 提取分红信息
                PatternCompiler compiler = new Perl5Compiler();
                Pattern pattern = compiler.compile("([0-9]+\\.[0-9]+)");
                PatternMatcher matcher = new Perl5Matcher();
                PatternMatcherInput matcherInput = new PatternMatcherInput(bouns);
                if( matcher.contains(matcherInput, pattern) ) {
                    MatchResult result = matcher.getMatch();
                    fundBounsVo.setBouns(result.group(0));
                }
                fundBounsVo.setDividendPaymentDateStr(tds.get(4).text());// 红利发放日

                list.add(fundBounsVo);
            }

            getMethod.releaseConnection();
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
