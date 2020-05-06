package org.weaxsey.traslation;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.weaxsey.traslation.api.ITranslateService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TranslateServiceImpl implements ITranslateService {


    private static String youdaoTranslateUrl = "https://aidemo.youdao.com/trans";


    @Override
    public String translate(String message) {
        String ans = null;
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 50, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue(10000));
        Callable<String> baidu = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return baidu(message);
            }
        };
        Callable<String> google = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return google(message);
            }
        };
        Callable<String> youdao = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return youdao(message);
            }
        };
        List<Callable<String>> callables = new ArrayList<>();
        callables.add(baidu);callables.add(google);callables.add(youdao);
        try {
            //invokeAny方法会对线程进行提交
            ans = poolExecutor.invokeAny(callables);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ans;
    }


    private String baidu(String content) {

        return "result";
    }

    private String google(String content) {

        return "result";
    }

    private String youdao(String content) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("q", content);
        requestBody.put("from", "Auto");
        requestBody.put("to", "Auto");
        String result = null;
        try {
            String tmp =   Request.Post(youdaoTranslateUrl).bodyString(requestBody.toJSONString(), ContentType.create("application/x-www-form-urlencoded", "UTF-8"))
                    .execute().returnContent().asString();
            String[] results = (String[])JSONObject.parseObject(tmp).get("translation");
            result = results[0];
        } catch (IOException e) {

        }
        return result;
    }






}
