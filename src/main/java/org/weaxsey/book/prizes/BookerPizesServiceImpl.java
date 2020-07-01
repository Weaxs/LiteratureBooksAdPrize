package org.weaxsey.book.prizes;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.weaxsey.book.domain.BookMessage;
import org.weaxsey.book.prizes.api.AbstractPrizesServiceImpl;
import org.weaxsey.remotecall.domain.RemoteMsg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("theBookerPizes")
public class BookerPizesServiceImpl extends AbstractPrizesServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(BookerPizesServiceImpl.class);

    @Override
    public List<BookMessage> getWinner(Calendar calendar) {
        String year = yearDateFormat.format(calendar.getTime());

        if (Integer.parseInt(year) < BOOKER_PRIZES_START_YEAR) {
            throw new RuntimeException("The first Booker pize began in 1969.");
        }
        if (calendar.getTime().after(new Date())) {
            throw new RuntimeException("The " + year + " Booker pize hasn't awarded.");
        }

        List<String> years = new ArrayList<>();
        Boolean isThisYear = isThisYear(year);
        //Maybe the winners have not been announced this year
        if (isThisYear) {
            int tmp = Integer.parseInt(year);
            years.add(year);
            years.add(String.valueOf(tmp - 1));
        } else {
            years.add(year);
        }

        List<List<BookMessage>> prizeMsgs = redisClient.<String, List<BookMessage>>getMultiHash("theBookerPizes", years);
        if (prizeMsgs.get(0) != null && prizeMsgs.get(0).size() > 0) {
            return prizeMsgs.get(0);
        }
        if (isThisYear && prizeMsgs.get(1) != null && prizeMsgs.get(1).size() > 0) {
            return prizeMsgs.get(1);
        }

        logger.info("The " + year + " Booker Pizes don't store in Redis");
        RemoteMsg remoteMsg = new RemoteMsg();
        remoteMsg.setUrl(bookerUrl + year);
        String html = remoteCallService.remoteCallByRequestGet(remoteMsg);
        String winnerYear = getWinnerYear(html);
        List<BookMessage> prizeMsg = getPrizeMsg(html);
        for (BookMessage book:prizeMsg) {
            book.setWinnerYear(winnerYear);
        }
        redisClient.addHashStandalone("theBookerPizes", winnerYear, prizeMsg);
        logger.info("The " + winnerYear + " Booker Pizes:" + JSONObject.toJSONString(prizeMsg));

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
        Elements authorNameItems = document.select("strong,b");
        for (int i = 0, j = 0; i < bookNameitems.size() && j < authorNameItems.size();i++) {
            if (!bookNameitems.get(i).text().startsWith("<img") && !StringUtils.isEmpty(bookNameitems.get(i).text())) {
                BookMessage book = new BookMessage();
                book.setBookName(bookNameitems.get(i).text());
                if (!authorNameItems.get(j).text().contains("(") && authorNameItems.get(j).text().contains("By")) {
                    book.setAuthor(authorNameItems.get(j++).text().substring(3));
                    book.setPublisher(authorNameItems.get(j).text().substring(13));
                    prizeMsg.add(book);
                    continue;
                } else if (authorNameItems.get(j).text().contains("(")) {
                    String[] authorAndPublisher = authorNameItems.get(j).text().split("\\(");
                    book.setAuthor(authorAndPublisher[0]);
                    book.setPublisher(authorAndPublisher[1].substring(0, authorAndPublisher[1].length() -2));
                    prizeMsg.add(book);
                }
                j++;
            }
        }
        return prizeMsg;
    }

}
