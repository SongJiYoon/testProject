package com.test.project.service.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
public final class HttpJsonClient {

    private HttpJsonClient() {}

    private static final class HttpJsonClientHolder {
        private HttpJsonClientHolder() {}

        private static final HttpJsonClient INSTANCE = new HttpJsonClient();
    }

    public static HttpJsonClient getInstance() {
        return HttpJsonClientHolder.INSTANCE;
    }

    public static HttpJsonClient getInstance(HttpContentType contentType) {
        HttpJsonClient httpJsonClient = HttpJsonClientHolder.INSTANCE;
        httpJsonClient.setContentType(contentType);
        return httpJsonClient;
    }

    public enum HttpContentType {
        NORMAL,
        JSON
    }

    private static final String CONTENT_TYPE_NORMAL = "application/x-www-form-urlencoded;charset=UTF-8";
    private static final String CONTENT_TYPE_JSON   = "application/json;charset=UTF-8";

    private static final String USER_AGENT          = "Allng Platform API";

    private HttpContentType     contentType         = HttpContentType.JSON;

    public void setContentType(HttpContentType contentType) {
        this.contentType = contentType;
    }

    // Create a custom response handler
    private ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                                                        public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                                                            int status = response.getStatusLine().getStatusCode();
                                                            if (status >= 200 && status < 300) {
                                                                HttpEntity entity = response.getEntity();
                                                                return entity != null ? EntityUtils.toString(entity) : null;
                                                            } else {
                                                                throw new ClientProtocolException("Unexpected response status: " + status);
                                                            }
                                                        }
                                                    };

    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    public class Result {

        private int    resultCode;
        private String resultMessage;
        private String resultData;

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMessage() {
            return resultMessage;
        }

        public void setResultMessage(String resultMessage) {
            this.resultMessage = resultMessage;
        }

        public String getResultData() {
            return resultData;
        }

        public void setResultData(String resultData) {
            this.resultData = resultData;
        }
    }

    /**
     * POST 방식으로 HTTP 통신
     * 
     * @param url
     *            호출주소
     * @param jsonData
     *            데이터
     * @param typeOfT
     *            리턴받을 객체
     * @return
     * @throws Exception
     */
    public <E> E sendPostRequest(String url, String jsonData, Class<E> typeOfT) throws Exception {
        return this.sendPostRequest(url, null, jsonData, typeOfT);
    }

    /**
     * POST 방식으로 HTTP 통신
     * 
     * @param url
     *            호출주소
     * @param serviceKey
     *            전문 헤더의 ServiceKey
     * @param jsonData
     *            데이터
     * @param typeOfT
     *            리턴받을 객체
     * @return
     * @throws Exception
     */
    public <E> E sendPostRequest(String url, String serviceKey, String jsonData, Class<E> typeOfT) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            StringEntity input = new StringEntity(jsonData, "UTF-8");
            input.setContentType(contentType == HttpContentType.JSON ? CONTENT_TYPE_JSON : CONTENT_TYPE_NORMAL);

            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, contentType == HttpContentType.JSON ? CONTENT_TYPE_JSON : CONTENT_TYPE_NORMAL));

            HttpPost request = new HttpPost(url);
            if (serviceKey != null) {
                request.setHeader("serviceKey", serviceKey);
            }
            request.setEntity(input);
            String responseBody = httpclient.execute(request, responseHandler);

            Gson gson = new Gson();
            return gson.fromJson(gson.fromJson(responseBody, Result.class).resultData, typeOfT);

        } finally {
            httpclient.close();
        }
    }

    /**
     * GET 방식으로 HTTP 통신
     * 
     * @param url
     *            호출주소
     * @param typeOfT
     *            리턴받을 객체
     * @return
     * @throws Exception
     */
    public <E> E sendGetRequest(String url, Class<E> typeOfT) throws Exception {
        return this.sendGetRequest(url, null, typeOfT);
    }

    /**
     * GET 방식으로 HTTP 통신
     * 
     * @param url
     *            호출주소
     * @param serviceKey
     *            전문 헤더의 ServiceKey
     * @param typeOfT
     *            리턴받을 객체
     * @return
     * @throws Exception
     */
    public <E> E sendGetRequest(String url, String serviceKey, Class<E> typeOfT) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(url);
            request.addHeader("accept", contentType == HttpContentType.JSON ? CONTENT_TYPE_JSON : CONTENT_TYPE_NORMAL);
            request.addHeader("User-Agent", USER_AGENT);
            if (serviceKey != null) {
                request.setHeader("serviceKey", serviceKey);
            }

            String responseBody = httpclient.execute(request, responseHandler);

            Gson gson = new Gson();
            return gson.fromJson(gson.fromJson(responseBody, Result.class).resultData, typeOfT);
        } finally {
            httpclient.close();
        }
    }

    /**
     * HTTP 통신을 위한 함수
     * 
     * @param method
     *            HTTP Method 종류
     * @param url
     *            호출주소
     * @param serviceKey
     *            서비스
     * @param param
     *            요청할 전문 데이터
     * @return
     * @throws Exception
     */
    public String sendRequest(HttpMethod method, String url, String serviceKey, List<NameValuePair> params) throws Exception {

        if (!(method instanceof HttpMethod)) {
            throw new IllegalArgumentException();
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpUriRequest request = null;

            String paramString = "";
            StringEntity body = null;
            switch (method) {
                case GET:
                    paramString = params == null ? "" : "?" + URLEncodedUtils.format(params, "UTF-8");
                    request = new HttpGet(url + paramString);
                    request.addHeader("accept", contentType == HttpContentType.JSON ? CONTENT_TYPE_JSON : CONTENT_TYPE_NORMAL);
                    // request.addHeader("User-Agent", USER_AGENT);
                    break;

                case POST:
                    body = new StringEntity("", "UTF-8");

                    switch (contentType) {
                        case JSON:
                            body = new StringEntity(getParam(params), "UTF-8");
                            body.setContentType("application/json");
                            break;
                        case NORMAL:
                            body = new UrlEncodedFormEntity(params, "UTF-8");
                            body.setContentType("application/x-www-form-urlencoded");
                            break;
                    }

                    request = new HttpPost(url);
                    ((HttpPost) request).setEntity(body);
                    break;
                case PUT:
                    body = new StringEntity("", "UTF-8");

                    switch (contentType) {
                        case JSON:
                            body = new StringEntity(getParam(params), "UTF-8");
                            body.setContentType("application/json");
                            break;
                        case NORMAL:
                            body = new UrlEncodedFormEntity(params, "UTF-8");
                            body.setContentType("application/x-www-form-urlencoded");
                            break;
                    }
                    
                    request  = new HttpPut(url);
                    ((HttpPut) request).setEntity(body);
                    break;
                case DELETE:
                    paramString = params == null ? "" : "?" + URLEncodedUtils.format(params, "UTF-8");
                    request  = new HttpDelete(url); 
                    request.addHeader("accept", contentType == HttpContentType.JSON ? CONTENT_TYPE_JSON : CONTENT_TYPE_NORMAL);
                    
                    break;

            }

            if (serviceKey != null && !serviceKey.equals("")) {
                request.addHeader("serviceKey", serviceKey);
            }

            return httpclient.execute(request, responseHandler);
        } finally {
            httpclient.close();
        }
    }

    private String getParam(List<NameValuePair> params) {
        if (params == null) {
            return "";
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        for (NameValuePair entity : params) {
            map.put(entity.getName(), entity.getValue());
        }

        return (new Gson()).toJson(map);
    }

    /**
     * HTTP 통신을 위한 함수
     * 
     * @param method
     *            HTTP Method 종류
     * @param url
     *            호출주소
     * @param serviceKey
     *            서비스
     * @param params
     *            요청할 전문 데이터
     * @param typeOfT
     *            리턴받을 객체
     * @return
     * @throws Exception
     */
    public <E> E sendRequest(HttpMethod method, String url, String serviceKey, List<NameValuePair> params, Class<E> typeOfT) throws Exception {
        String responseBody = sendRequest(method, url, serviceKey, params);
        return (new Gson()).fromJson(responseBody, typeOfT);
    }

    /**
     * HTTP 통신을 위한 함수 (표준방식)
     * 
     * @param method
     *            HTTP Method 종류
     * @param url
     *            호출주소
     * @param serviceKey
     *            서비스
     * @param params
     *            요청할 전문 데이터
     * @param typeOfT
     *            리턴받을 객체
     * @return
     * @throws Exception
     */
    public <E> E sendRequestByResult(HttpMethod method, String url, String serviceKey, List<NameValuePair> params, Class<E> typeOfT) throws Exception {
        String responseBody = sendRequest(method, url, serviceKey, params);
        return (new Gson()).fromJson((new Gson()).fromJson(responseBody, Result.class).resultData, typeOfT);
    }

}
