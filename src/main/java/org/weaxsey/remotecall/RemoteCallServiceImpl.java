package org.weaxsey.remotecall;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.weaxsey.remotecall.api.IRemoteCallService;
import org.weaxsey.remotecall.domain.RequestParam;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Remote Call Service
 *
 * @author Weaxs
 */
@Service
public class RemoteCallServiceImpl implements IRemoteCallService {

    private static final Logger logger = LoggerFactory.getLogger(RemoteCallServiceImpl.class);

    /**
     * 超时时间
     */
    protected static final int SOCKET_TIMEOUT = 10000;

    @Override
    public String remoteCallByRequestGet(RequestParam remoteMsg) {
        try {
            return Request.Get(remoteMsg.getUrl()).execute().returnContent().asString(Charset.forName(remoteMsg.getCharset()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("请求接口出错！", e);
        }
    }

    @Override
    public String remoteCallByRequestPost(RequestParam remoteMsg) {
        try {
            return Request.Post(remoteMsg.getUrl()).bodyString(remoteMsg.getRequestBody(), ContentType.create(remoteMsg.getContentType(), remoteMsg.getCharset()))
                    .execute().returnContent().asString(Charset.forName(remoteMsg.getCharset()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("请求接口出错！", e);
        }

    }

    @Override
    public String remoteCallByHttpClientPost(RequestParam remoteMsg) {

        //1.组装URI
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry:remoteMsg.getHeadParamMap().entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme(remoteMsg.getScheme()).setHost(remoteMsg.getHost())
                    .setPath(remoteMsg.getPath()).setParameters(params).build();
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        //2.组装HttpPost
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", remoteMsg.getContentType());
        // 组装请求体的参数（编译器告诉我String比StringBuilder效率高，？？
        StringEntity stringEntity = new StringEntity(remoteMsg.getRequestBody(), remoteMsg.getCharset());
        httpPost.setEntity(stringEntity);

        //3.组装HttpClient
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
                rtnMessage = EntityUtils.toString(responseEntity, remoteMsg.getCharset());
            }
        } catch (ParseException |IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }  finally {
            close(httpClient);
            close(response);
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rtnMessage;

    }

    @Override
    public String remoteCallByHttpUrlConnectionPost(RequestParam remoteMsg) {

        try {
            // 创建URL对象
            URL uri = new URL(remoteMsg.getUrl());
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

            if (remoteMsg.getOpenssl()) {
                // 设置SSLContext
                SSLContext sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[] { MY_X509_TRUST_MANAGER }, null);
                if (conn instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sslcontext.getSocketFactory());
                }
            }
            // 设置相应超时
            conn.setConnectTimeout(SOCKET_TIMEOUT);
            conn.setRequestMethod(remoteMsg.getRequestMethod());
            int statusCode = conn.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Http错误码：" + statusCode);
            }

            // 读取服务器的数据
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }

            String text = builder.toString();

            // 关闭数据流
            close(br);
            close(is);
            // 断开连接
            conn.disconnect();

            return text;

        } catch (MalformedURLException | KeyManagementException |NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }

    private final static TrustManager MY_X509_TRUST_MANAGER = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    };

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
