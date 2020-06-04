package org.weaxsey.traslation;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.weaxsey.traslation.api.ITranslateService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@Service
public class TranslateServiceImpl implements ITranslateService {

    private static final Logger logger = LoggerFactory.getLogger(TranslateServiceImpl.class);

    private static String youdaoTranslateUrl = "https://openapi.youdao.com/api";

    @Autowired
    private BaiduTranslateService baiduService;

    private static TranslateServiceImpl translateService;

    public static ITranslateService getInstance() {
        if (translateService == null)
            translateService = new TranslateServiceImpl();
        return translateService;
    }

    @Override
    public String translate(String message) {
        String ans = null;
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 50, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue(10000));
        Callable<String> baiduCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return baiduService.translate(message);
            }
        };
        Callable<String> googleCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return google(message);
            }
        };
        Callable<String> youdaoCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return youdao(message);
            }
        };
        List<Callable<String>> callables = new ArrayList<>();
        callables.add(baiduCallable);
//        callables.add(google);callables.add(youdao);
        try {
            //invokeAny方法会对线程进行提交
            ans = poolExecutor.invokeAny(callables);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ans;
    }

    private String google(String content) {

        return "result";
    }

    private String youdao(String content) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("q", content);
        requestBody.put("from", "auto");
        requestBody.put("to", "auto");
        requestBody.put("appKey", "");
        requestBody.put("salt", "");
        requestBody.put("sign", "sha256()");
        requestBody.put("signType", "v3");
        requestBody.put("curtime", new Date());
        requestBody.put("strict", false);
        String result = null;
        try {
            String tmp =   Request.Post(youdaoTranslateUrl).bodyString(requestBody.toJSONString(), ContentType.create("application/x-www-form-urlencoded", "UTF-8"))
                    .execute().returnContent().asString();
            String[] results = (String[])JSONObject.parseObject(tmp).get("translation");
            result = results[0];
        } catch (IOException e) {
            logger.error("查询有道翻译报错：" + e.getMessage(), e);
        }
        return result;
    }


}
