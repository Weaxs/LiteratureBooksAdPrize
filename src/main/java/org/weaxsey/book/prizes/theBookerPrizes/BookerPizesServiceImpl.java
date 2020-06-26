package org.weaxsey.book.prizes.theBookerPrizes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.weaxsey.book.prizes.IPrizesService;
import org.weaxsey.remotecall.api.IRemoteCallService;
import org.weaxsey.remotecall.domain.RemoteMsg;

import java.util.Calendar;

@Component("theBookerPizes")
public class BookerPizesServiceImpl implements IPrizesService {

    private static String url = "https://thebookerprizes.com/fiction/";

    @Autowired
    private IRemoteCallService remoteCallService;

    @Override
    public void getWinner(Calendar calendar) {
        String year = yearDateFormat.format(calendar.getTime());

        RemoteMsg remoteMsg = new RemoteMsg();
        remoteMsg.setUrl(url + year);
        String html = remoteCallService.remoteCallByRequestGET(remoteMsg);
        String winnerYear = getWinnerYear(html);


    }

    private String getWinnerYear(String html) {
        Document document = Jsoup.parse(html);

        //选择height属性为23，width属性为528，且只含有/没有div子标签
        Elements items = document.select("h2[class=field field-name-field-winning-book-heading field-type-text field-label-hidden title]");

        String winnerYear = null;
        for (Element element:items) {
            if (element.text().contains("Winners")) {
                winnerYear = element.text().split(" ")[1];
                break;
            }
        }

        return winnerYear;
    }



}
