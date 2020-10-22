package org.weaxsey.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Handle Request Param
 *
 * @author Weaxs
 */
public class RequestSpliceUtils {
    private static final String QUESTION_MASK = "?";
    private static final String AND_MASK = "&";
    private static final String EQUAL_MASK = "=";
    private static final String ENCODE = "utf-8";

    public static String getUrlWithHeadParamsEncode(String url, Map<String, String> params) {
        return getStringWithParams(url, params, true, true);
    }
    public static String getUrlWithHeadParamsNoEncode(String url, Map<String, String> params) {
        return getStringWithParams(url, params, true, false);
    }
    public static String getBodyWithParam(Map<String, String> params) {
        return getStringWithParams(null, params, false, false);
    }

    private static String getStringWithParams(String url, Map<String, String> params, boolean isHead, boolean encode) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder();
        if (isHead) {
            builder.append(url);
            if (url.contains(QUESTION_MASK)) {
                builder.append(AND_MASK);
            } else {
                builder.append(QUESTION_MASK);
            }
        }

        int i = 0;
        for (Map.Entry<String, String> entry: params.entrySet()) {
            String value = entry.getValue();
            // 过滤空的key
            if (value == null) {
                continue;
            }
            if (i != 0) {
                builder.append(AND_MASK);
            }
            builder.append(entry.getKey());
            builder.append(EQUAL_MASK);
            if (encode) {
                builder.append(encode(value));
            } else {
                builder.append(value);
            }

            i++;
        }

        return builder.toString();
    }

    /**
     * 对输入的字符串进行URL编码, 即转换为%20这种形式
     *
     * @param input 原文
     * @return URL编码. 如果编码失败, 则返回原文
     */
    private static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

}
