package org.weaxsey.book.prizes.theBookerPrizes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.weaxsey.book.prizes.IPrizesService;
import org.weaxsey.redis.RedisClient;
import org.weaxsey.remotecall.api.IRemoteCallService;
import org.weaxsey.remotecall.domain.RemoteMsg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component("theBookerPizes")
public class BookerPizesServiceImpl implements IPrizesService {

    private static String url = "https://thebookerprizes.com/fiction/";

    @Autowired
    private IRemoteCallService remoteCallService;
    @Autowired
    private RedisClient<String> redisClient;

    @Override
    public JSONArray getWinner(Calendar calendar) {
        String year = yearDateFormat.format(calendar.getTime());
        List<String> years = new ArrayList<>();
        Boolean isThisYear = isThisYear(year);
        if (isThisYear) {//Maybe the winners have not been announced this year
            int tmp = Integer.parseInt(year);
            years.add(year);
            years.add(String.valueOf(tmp - 1));
        } else {
            years.add(year);
        }

        List<JSONArray> prizeMsgs = redisClient.<String, JSONArray>getMultiHash("theBookerPizes", years);
        if (prizeMsgs.get(0) != null && prizeMsgs.get(0).size() > 0)
            return prizeMsgs.get(0);
        if (isThisYear && prizeMsgs.get(1) != null && prizeMsgs.get(1).size() > 0)
            return prizeMsgs.get(1);

        RemoteMsg remoteMsg = new RemoteMsg();
        remoteMsg.setUrl(url + year);
        String html = remoteCallService.remoteCallByRequestGET(remoteMsg);
        String winnerYear = getWinnerYear(html);
        JSONArray prizeMsg = getPrizeMsg(html);
        redisClient.addHashStandalone("theBookerPizes", winnerYear, prizeMsg);

        return prizeMsg;
    }

    private String getWinnerYear(String html) {
        Document document = Jsoup.parse(html);
        Elements items = document.select("h2[class=field field-name-field-winning-book-heading field-type-text field-label-hidden title]");

        String winnerYear = null;
        for (Element element:items) {
            if (element.text().contains("Winners") || element.text().contains("Winner")) {
                winnerYear = element.text().split(" ")[1];
                break;
            }
        }
        return winnerYear;
    }

    private JSONArray getPrizeMsg(String html) {
        JSONArray prizeMsg = new JSONArray();
        Document document = Jsoup.parse(html);
        //查询便签<a>的href属性中以/books开头的
        Elements bookNameitems = document.select("a[href~=^/books]");
        Elements authorNameItems = document.select("strong");
        for (int i = 0, j = 0; i < bookNameitems.size() && j < authorNameItems.size();i++) {
            if (!bookNameitems.get(i).text().startsWith("<img") && !StringUtils.isEmpty(bookNameitems.get(i).text())) {
                JSONObject book = new JSONObject();
                book.put("book", bookNameitems.get(i).text());
                if (!authorNameItems.get(j).text().contains("\\(")) {
                    j++;
                    continue;
                }
                String[] authorAndPublisher = authorNameItems.get(j).text().split("\\(");
                book.put("author", authorAndPublisher[0]);
                book.put("publisher", authorAndPublisher[1].substring(0, authorAndPublisher[1].length() -2));
                prizeMsg.add(book);
                j++;
            }
        }
        return prizeMsg;
    }

    private Boolean isThisYear(String year) {
        return year.equals(yearDateFormat.format(Calendar.getInstance().getTime()));
    }



}
