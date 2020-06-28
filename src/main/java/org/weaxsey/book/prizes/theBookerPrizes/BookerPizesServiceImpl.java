package org.weaxsey.book.prizes.theBookerPrizes;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.weaxsey.book.domain.BookMessage;
import org.weaxsey.book.prizes.IPrizesService;
import org.weaxsey.redis.RedisClient;
import org.weaxsey.remotecall.api.IRemoteCallService;
import org.weaxsey.remotecall.domain.RemoteMsg;

import java.util.*;

@Component("theBookerPizes")
public class BookerPizesServiceImpl implements IPrizesService {

    private static String url = "https://thebookerprizes.com/fiction/";

    private static final Logger logger = LoggerFactory.getLogger(BookerPizesServiceImpl.class);

    @Autowired
    private IRemoteCallService remoteCallService;
    @Autowired
    private RedisClient<String> redisClient;

    @Override
    public List<BookMessage> getWinner(Calendar calendar) {
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

        List<List<BookMessage>> prizeMsgs = redisClient.<String, List<BookMessage>>getMultiHash("theBookerPizes", years);
        if (prizeMsgs.get(0) != null && prizeMsgs.get(0).size() > 0)
            return prizeMsgs.get(0);
        if (isThisYear && prizeMsgs.get(1) != null && prizeMsgs.get(1).size() > 0)
            return prizeMsgs.get(1);

        logger.info("The " + year + " Booker Pizes don't store in Redis");
        RemoteMsg remoteMsg = new RemoteMsg();
        remoteMsg.setUrl(url + year);
        String html = remoteCallService.remoteCallByRequestGET(remoteMsg);
        String winnerYear = getWinnerYear(html);
        List<BookMessage> prizeMsg = getPrizeMsg(html);
        for (BookMessage book:prizeMsg) {
            book.setWinnerYear(winnerYear);
        }
        redisClient.addHashStandalone("theBookerPizes", winnerYear, prizeMsg);
        logger.info("The " + year + " Booker Pizes:" + JSONObject.toJSONString(prizeMsg));

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

    private List<BookMessage> getPrizeMsg(String html) {
        List<BookMessage> prizeMsg = new ArrayList<>();
        Document document = Jsoup.parse(html);
        //查询便签<a>的href属性中以/books开头的
        Elements bookNameitems = document.select("a[href~=^/books]");
        Elements authorNameItems = document.select("strong");
        for (int i = 0, j = 0; i < bookNameitems.size() && j < authorNameItems.size();i++) {
            if (!bookNameitems.get(i).text().startsWith("<img") && !StringUtils.isEmpty(bookNameitems.get(i).text())) {
                BookMessage book = new BookMessage();
                book.setBookName(bookNameitems.get(i).text());
                if (!authorNameItems.get(j).text().contains("(")) {
                    j++;
                    continue;
                }
                String[] authorAndPublisher = authorNameItems.get(j).text().split("\\(");
                book.setAuthor(authorAndPublisher[0]);
                book.setPublisher(authorAndPublisher[1].substring(0, authorAndPublisher[1].length() -2));
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
