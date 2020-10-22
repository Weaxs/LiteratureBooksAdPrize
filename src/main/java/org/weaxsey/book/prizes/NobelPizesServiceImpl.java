package org.weaxsey.book.prizes;

import org.apache.http.client.utils.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.weaxsey.book.domain.BookMessage;
import org.weaxsey.book.prizes.api.AbstractPrizesServiceImpl;
import org.weaxsey.remotecall.domain.RequestParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.weaxsey.book.constant.CommonConstant.*;
import static org.weaxsey.book.constant.ErrorMsg.NOBEL_YEAR_SMALL_MSG;
import static org.weaxsey.book.constant.ErrorMsg.hasNotAwarded;
import static org.weaxsey.config.UserDefThreadPoolExecutor.POOL_EXECUTOR;

/**
 * Nobel prize
 *
 * @author Weaxs
 */
@Service("theNobelPizes")
public class NobelPizesServiceImpl extends AbstractPrizesServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(NobelPizesServiceImpl.class);
    private static final String NOBEL = "Nobel";

    @Override
    public List<BookMessage> getWinner(Calendar calendar) {

        String year = DateUtils.formatDate(calendar.getTime(), YEAR_DATE_FORMAT);

        if (Integer.parseInt(year) < Nobel_PRIZES_START_YEAR) {
            throw new RuntimeException(NOBEL_YEAR_SMALL_MSG);
        }
        if (calendar.getTime().after(new Date())) {
            throw new RuntimeException(hasNotAwarded(Long.parseLong(year), NOBEL));
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

//        List<List<BookMessage>> prizeMsgs = redisClient.<String, List<BookMessage>>getMultiHash("theNobelPizes", years);
//        if (prizeMsgs.get(0) != null && prizeMsgs.get(0).size() > 0) {
//            return prizeMsgs.get(0);
//        }
//        if (isThisYear && prizeMsgs.get(1) != null && prizeMsgs.get(1).size() > 0) {
//            return prizeMsgs.get(1);
//        }
//        logger.info("The " + year + " Nobel Pizes don't store in Redis");

        List<Callable<List<BookMessage>>> callables = new ArrayList<>();
        for (String y:years) {
            callables.add(() -> {
                RequestParam requestParam = new RequestParam();
                requestParam.setUrl(nobelUrl + y + "/summary/");
                String html = remoteCallService.remoteCallByRequestGet(requestParam);
                List<BookMessage> prizeMsg = getPrizeMsg(html);
                for (BookMessage bookMessage:prizeMsg) {
                    bookMessage.setWinnerYear(y);
                }
                return prizeMsg;
            });
        }
        List<BookMessage> prizeMsg = new ArrayList<>();
        try {
            List<Future<List<BookMessage>>> results = POOL_EXECUTOR.invokeAll(callables);
            for (Future<List<BookMessage>> result : results) {
                if (result.toString().contains("Completed normally")) {
                    prizeMsg = result.get();
//                    redisClient.addHashStandalone("theNobelPizes", years.get(i), prizeMsg);
                }
            }

        } catch (InterruptedException | ExecutionException e) {
            logger.error("");
        }
        return prizeMsg;
    }

    private List<BookMessage> getPrizeMsg(String html) {
        List<BookMessage> prizeMsg = new ArrayList<>();
        Document document = Jsoup.parse(html);
        //查询便签<a>的href属性中以https://www.nobelprize.org/prizes/literature/开头的
        Elements authorNameItems = document.select("a[href~=^https://www.nobelprize.org/prizes/literature/][title=Title text]");
        for (Element author:authorNameItems) {
            BookMessage book = new BookMessage();
            book.setAuthor(author.text());
            prizeMsg.add(book);
        }
        return prizeMsg;
    }


}
