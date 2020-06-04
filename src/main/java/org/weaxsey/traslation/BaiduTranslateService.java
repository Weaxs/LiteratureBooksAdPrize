package org.weaxsey.traslation;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.weaxsey.utils.MD5Utils;

@Service
@PropertySource("classpath:translation.properties")
public class BaiduTranslateService {

    private static final Logger logger = LoggerFactory.getLogger(BaiduTranslateService.class);

    private static String BAIDU_TRANSLATE_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    @Value("${baidu.translate.secret.appid}")
    private String baiduTranslateAppId;
    @Value("${baidu.translate.secret.key}")
    private String baiduKey;

    public String translate(String content) {
        String salt = String.valueOf(System.currentTimeMillis());;
        String sign = MD5Utils.md5(baiduTranslateAppId + content + salt + baiduKey);
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
            String tmp =   Request.Post(BAIDU_TRANSLATE_URL).bodyString(requestBody.toJSONString(), ContentType.create("application/x-www-form-urlencoded", "UTF-8"))
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



}
