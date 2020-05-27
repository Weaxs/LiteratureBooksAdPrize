package org.weaxsey.traslation;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@PropertySource("translation.properties")
public class TranslateServiceImpl implements ITranslateService {

    private static final Logger logger = LoggerFactory.getLogger(TranslateServiceImpl.class);

    private static String youdaoTranslateUrl = "https://openapi.youdao.com/api";

    private static String baiduTranslateUrl = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    @Value("${baidu.translate.secret.appid}")
    private String baiduTranslateAppId;
    @Value("${baidu.translate.secret.key}")
    private String baiduKey;

    public static ITranslateService getInstance() {
        return new TranslateServiceImpl();
    }

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
        callables.add(baidu);
//        callables.add(google);callables.add(youdao);
        try {
            //invokeAny方法会对线程进行提交
            ans = poolExecutor.invokeAny(callables);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ans;
    }


    private String baidu(String content) {
        Long salt = new Random().nextLong();
        String sign = stringToMD5(baiduTranslateAppId + content + salt + baiduKey);
        JSONObject requestBody = new JSONObject();
        requestBody.put("q", content);
        requestBody.put("from", "auto");
        requestBody.put("to", "auto");
        requestBody.put("appid", baiduTranslateAppId);
        requestBody.put("salt", salt);
        requestBody.put("sign", sign);
        requestBody.put("dict", 0);
        JSONObject result = null;
        try {
            String tmp =   Request.Post(baiduTranslateUrl).bodyString(requestBody.toJSONString(), ContentType.create("application/x-www-form-urlencoded", "UTF-8"))
                    .execute().returnContent().asString();
            result = JSONObject.parseObject(tmp);
            if (result.getLong("error_code") != 52000L)
                throw new RuntimeException(result.getString("error_msg"));
        } catch (Exception e) {
            logger.error("查询百度翻译报错：" + e.getMessage(), e);
        }
        JSONObject transResult = result.getJSONObject("trans_result");

        return transResult.getString("dst");
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

    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }






}
