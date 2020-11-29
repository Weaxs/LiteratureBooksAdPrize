package org.weaxsey.traslation;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.weaxsey.remotecall.api.IRemoteCallService;
import org.weaxsey.traslation.api.ITranslateService;
import org.weaxsey.traslation.model.TranslateLanguage;
import org.weaxsey.traslation.model.TranslateResponse;
import org.weaxsey.traslation.model.TranslateType;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Multi Translate
 *
 * @author Weaxs
 */
@Service
public class TranslateServiceImpl implements ITranslateService {

    private static final String REQUEST_ID_HEADER = "X-Bce-Request-Id";
    private static final String SIGNATURE_HEADER = "X-Bce-Signature";
    private static final String APP_CODE_PREFIX = "AppCode/";

    @Value("${baidu.api.appCode:}")
    private String appCode;
    @Value("${baidu.api.translate.url:}")
    private String host;
    @Autowired
    private IRemoteCallService remoteCallService;

    private static final Logger logger = LoggerFactory.getLogger(TranslateServiceImpl.class);

    @Override
    public String translate(String message) {
        return translate(message, TranslateLanguage.zh_cn, TranslateLanguage.en);
    }

    @Override
    public String translate(String message, TranslateLanguage from, TranslateLanguage to) {

        String url = host + "?" + "from=" + from.getCode()
                + "&" + "type=" + TranslateType.google
                + "&" + "to=" + to.getCode()
                + "&" + "text=" + message;
        URI uri = URI.create(url);
        Map<String, String> headers = new HashMap<>();
        headers.put(REQUEST_ID_HEADER, String.valueOf(UUID.randomUUID()));
        headers.put(SIGNATURE_HEADER, APP_CODE_PREFIX + appCode);

        Request request = Request.Post(uri).bodyString("", ContentType.APPLICATION_JSON);
        headers.forEach(request::addHeader);
        TranslateResponse response = null;
        try {
            response = JSONObject.parseObject(request.execute().returnContent().asString(), TranslateResponse.class);
            if (response.getStatus() != 0) {
                throw new RuntimeException(response.getMsg());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getResult();
    }

}
