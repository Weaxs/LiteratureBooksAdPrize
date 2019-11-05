package org.weaxsey.allsagesbooks;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.weaxsey.allsagesbooks.api.IAllsagesbook;
import org.weaxsey.domain.BookMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AllsagesbookService implements IAllsagesbook {

    public JSONObject getBookMessage(BookMessage book) {

        // 通过URI添加请求头的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pageNum", "1"));
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http").setHost("www.allsagesbooks.com")
                    .setPath("/search/searchResult.asp").setParameters(params).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        // 组装请求体的参数（编译器告诉我String比StringBuilder效率高，？？
        String requestBody = "book=";requestBody += book.getBookName();
        requestBody += "&author=";requestBody += book.getAuthor();
        requestBody += "&publisher=";requestBody += book.getPublisher();
        requestBody += "&publishDate=";requestBody += book.getPublishDate();
        requestBody += "&publishPrefix=";requestBody += ">=";
        requestBody += "&isHave=";requestBody += "all";
        StringEntity stringEntity = new StringEntity(requestBody, "GBK");
        httpPost.setEntity(stringEntity);

        return analysisHtml(postApi(httpPost));

    }

    /**
     * 调用万圣书园查询书的接口
     */
    private String postApi(HttpPost httpPost) {
        String rtnMessage = null;

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                rtnMessage = EntityUtils.toString(responseEntity,"GBK");
            }
        } catch (ParseException |IOException e) {
            e.printStackTrace();
        }  finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rtnMessage;
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
