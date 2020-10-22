package org.weaxsey.traslation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.weaxsey.remotecall.api.IRemoteCallService;
import org.weaxsey.remotecall.domain.RequestParam;
import org.weaxsey.traslation.api.ITranslateService;
import org.weaxsey.utils.MD5Utils;
import org.weaxsey.utils.RequestSpliceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Baidu Translate
 *
 * @author Weaxs
 */
@Service("baidu")
@PropertySource("classpath:translation.properties")
public class BaiduTranslateServiceImpl implements ITranslateService {

    private static final Logger logger = LoggerFactory.getLogger(BaiduTranslateServiceImpl.class);

    private static String BAIDU_TRANSLATE_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    private static Long BAIDU_RESPONSE_SUCCESS = 52000L;
    private static String BAIDU_RESPONSE_ERRORCODE = "error_code";

    @Value("${baidu.translate.secret.appid}")
    private String baiduTranslateAppId;
    @Value("${baidu.translate.secret.key}")
    private String baiduKey;

    @Autowired
    private IRemoteCallService remoteCallService;

    @Override
    public String translate(String content) {
        String salt = String.valueOf(System.currentTimeMillis());;
        String sign = MD5Utils.md5(baiduTranslateAppId + content + salt + baiduKey);
        Map<String, String> requestBody = new HashMap<>(7);
        requestBody.put("q", content);
        requestBody.put("from", "auto");
        requestBody.put("to", "auto");
        requestBody.put("appid", baiduTranslateAppId);
        requestBody.put("salt", salt);
        requestBody.put("sign", sign);
        requestBody.put("dict", "0");
        String body = RequestSpliceUtils.getBodyWithParam(requestBody);

        RequestParam requestParam = new RequestParam();
        requestParam.setRequestBody(body);
        requestParam.setUrl(BAIDU_TRANSLATE_URL);
        requestParam.setContentType("application/x-www-form-urlencoded");
        requestParam.setCharset("UTF-8");

        String tmp = remoteCallService.remoteCallByHttpClientPost(requestParam);
        JSONObject   result = JSONObject.parseObject(tmp);
        if (result.containsKey(BAIDU_RESPONSE_ERRORCODE) &&  BAIDU_RESPONSE_SUCCESS.equals(result.getLong(BAIDU_RESPONSE_ERRORCODE))) {
            throw new RuntimeException(result.getString("error_msg"));
        }

        JSONArray transResult = result.getJSONArray("trans_result");
        JSONObject ans = JSON.parseObject(JSON.toJSONString(transResult.get(0)), JSONObject.class);

        return ans.getString("dst");
    }

}
