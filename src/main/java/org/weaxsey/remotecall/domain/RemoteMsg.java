package org.weaxsey.remotecall.domain;

import java.util.Map;

public class RemoteMsg {

    private String host;
    private String path;
    private String scheme;

    private String url;

    private String headParam;
    private Map<String, String> headParamMap;
    private String requestBody;
    private String contentType;
    private String charset = "utf-8";
    private String requestMethod;
    private boolean openssl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeadParam() {
        return headParam;
    }

    public void setHeadParam(String headParam) {
        this.headParam = headParam;
    }

    public Map<String, String> getHeadParamMap() {
        return headParamMap;
    }

    public void setHeadParamMap(Map<String, String> headParamMap) {
        this.headParamMap = headParamMap;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public boolean getOpenssl() {
        return openssl;
    }

    public void setOpenssl(boolean openssl) {
        this.openssl = openssl;
    }
}
