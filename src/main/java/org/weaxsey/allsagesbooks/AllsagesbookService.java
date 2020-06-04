package org.weaxsey.allsagesbooks;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weaxsey.allsagesbooks.api.IAllsagesbook;
import org.weaxsey.domain.BookMessage;
import org.weaxsey.remotecall.api.IRemoteCallService;
import org.weaxsey.remotecall.domain.RemoteMsg;
import org.weaxsey.utils.ReqHeadSpliceUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AllsagesbookService implements IAllsagesbook {

    @Autowired
    private IRemoteCallService remoteCallService;

    public JSONObject getBookMessageByRequest(BookMessage book) {

        Map<String, String> params = new HashMap<>();
        params.put("pageNum", "1");
        String requestBody = "book=";requestBody += book.getBookName();
        requestBody += "&author=";requestBody += book.getAuthor();
        requestBody += "&publisher=";requestBody += book.getPublisher();
        requestBody += "&publishDate=";requestBody += book.getPublishDate();
        requestBody += "&publishPrefix=";requestBody += ">=";
        requestBody += "&isHave=";requestBody += "all";
        String url =  ReqHeadSpliceUtils.getUrlWithHeadParams("http://www.allsagesbooks.com/search/searchResult.asp", params);


        RemoteMsg remoteMsg = new RemoteMsg();
        remoteMsg.setCharset("GBK");
        remoteMsg.setUrl(url);
        remoteMsg.setRequestBody(requestBody);
        remoteMsg.setContentType("application/x-www-form-urlencoded");
        return analysisHtml(remoteCallService.remoteCallByRequestPOST(remoteMsg));
    }

    public JSONObject getBookMessageByHttpClient(BookMessage book) {

        Map<String, String> headParams = new HashMap<>();
        headParams.put("pageNum", "1");
        String requestBody = "book=";requestBody += book.getBookName();
        requestBody += "&author=";requestBody += book.getAuthor();
        requestBody += "&publisher=";requestBody += book.getPublisher();
        requestBody += "&publishDate=";requestBody += book.getPublishDate();
        requestBody += "&publishPrefix=";requestBody += ">=";
        requestBody += "&isHave=";requestBody += "all";

        RemoteMsg remoteMsg = new RemoteMsg();
        remoteMsg.setHost("www.allsagesbooks.com");
        remoteMsg.setPath("/search/searchResult.asp");
        remoteMsg.setScheme("http");
        remoteMsg.setHeadParamMap(headParams);
        remoteMsg.setContentType("application/x-www-form-urlencoded");
        remoteMsg.setCharset("GBK");
        remoteMsg.setRequestBody(requestBody);

        return analysisHtml(remoteCallService.remoteCallByHttpClientPOST(remoteMsg));

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
}
