package org.weaxsey.book.allsagesBooks;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weaxsey.book.allsagesBooks.api.IAllsagesbook;
import org.weaxsey.book.domain.BookMessage;
import org.weaxsey.redis.RedisClient;
import org.weaxsey.remotecall.api.IRemoteCallService;
import org.weaxsey.remotecall.domain.RemoteMsg;
import org.weaxsey.utils.RequestSpliceUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AllsagesbookService implements IAllsagesbook {

    @Autowired
    private IRemoteCallService remoteCallService;
    @Autowired
    private RedisClient<String> redisClient;

    private static SimpleDateFormat rankDateFormat = new SimpleDateFormat("yyyy/MM");

    public JSONObject getBookMessageByRequest(BookMessage book) {

        Map<String, String> params = new HashMap<>();
        params.put("pageNum", "1");
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("book", book.getBookName());
        bodyMap.put("author", book.getAuthor());
        bodyMap.put("publisher", book.getPublisher());
        bodyMap.put("publishDate", book.getPublishDate());
        bodyMap.put("publishPrefix", ">=");
        bodyMap.put("isHave", "all");
        String url =  RequestSpliceUtils.getUrlWithHeadParamsNoEncode("http://www.allsagesbooks.com/search/searchResult.asp", params);
        String body = RequestSpliceUtils.getBodyWithParam(bodyMap);

        RemoteMsg remoteMsg = new RemoteMsg();
        remoteMsg.setCharset("GBK");
        remoteMsg.setUrl(url);
        remoteMsg.setRequestBody(body);
        remoteMsg.setContentType("application/x-www-form-urlencoded");
        return analysisHtml(remoteCallService.remoteCallByRequestPOST(remoteMsg));
    }

    public JSONObject getBookMessageByHttpClient(BookMessage book) {

        Map<String, String> headParams = new HashMap<>();
        headParams.put("pageNum", "1");
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("book", book.getBookName());
        bodyMap.put("author", book.getAuthor());
        bodyMap.put("publisher", book.getPublisher());
        bodyMap.put("publishDate", book.getPublishDate());
        bodyMap.put("publishPrefix", ">=");
        bodyMap.put("isHave", "all");
        String body = RequestSpliceUtils.getBodyWithParam(bodyMap);

        RemoteMsg remoteMsg = new RemoteMsg();
        remoteMsg.setHost("www.allsagesbooks.com");
        remoteMsg.setPath("/search/searchResult.asp");
        remoteMsg.setScheme("http");
        remoteMsg.setHeadParamMap(headParams);
        remoteMsg.setContentType("application/x-www-form-urlencoded");
        remoteMsg.setCharset("GBK");
        remoteMsg.setRequestBody(body);

        return analysisHtml(remoteCallService.remoteCallByHttpClientPOST(remoteMsg));

    }

    @Override
    public Map<Double, String> getRank() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
        String rankDate = rankDateFormat.format(calendar.getTime());
        Map<Double, String> redisRank = redisClient.getZSetBySocre("allsagesbook_rank_" + rankDate, 1D, 10D);
        if (redisRank == null || redisRank.size() <= 0) {
            RemoteMsg remoteMsg = new RemoteMsg();
            remoteMsg.setUrl("http://www.allsagesbooks.com/top10/index.asp");
            remoteMsg.setCharset("GBK");
            redisRank = analysisRankHtml(remoteCallService.remoteCallByRequestGET(remoteMsg));
            //存到缓存
            Long zset = redisClient.addZSetConllection("allsagesbook_rank_" + rankDate, redisRank, 90L, TimeUnit.DAYS);
        }
        return redisRank;
    }

    /**
     * 解析返回的html
     */
    private JSONObject analysisHtml(String html) {

        Document document = Jsoup.parse(html);

        //选择height属性为23，width属性为528，且只含有/没有div子标签
        Elements items = document.select("[height=23],[width=528].content > div");
//        Map<String, String> isbnMess = new IdentityHashMap<String, String>();
        JSONObject isbnMess = new JSONObject();
        for (int i = 0;i < items.size() ; i = i + 2) {
            isbnMess.put(items.get(i + 1).text(), items.get(i).text());
        }

        return isbnMess;
    }

    /**
     * 解析返回排行榜的的html
     */
    private Map<Double, String> analysisRankHtml(String html) {
        Document document = Jsoup.parse(html);

        //选择color属性为#000080，标签为font
        Elements items = document.select("font[color=#000080]");
        Map<Double, String> isbnMess = new HashMap<>();
        for (int i = 0;i < items.size() ; i = i + 2) {
            isbnMess.put(Double.valueOf(items.get(i).text()), items.get(i + 1).text());
        }

        return isbnMess;
    }
}
