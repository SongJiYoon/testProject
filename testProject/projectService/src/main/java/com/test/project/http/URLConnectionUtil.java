package com.test.project.http;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class URLConnectionUtil {
	private static final Object Contenttype = "Content-type";

	protected static Log logger = LogFactory.getLog(URLConnectionUtil.class);

	static ObjectMapper mapper = new ObjectMapper();

	static{
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}


	String encoding = "UTF-8";

	int connectionTimeout = 3000;

	int readTimeout = 3000;


	public <T> Result<T> request(String url, Map<String, String> headers, Map<String, ?> parameters, String method, Class<T> gernericType){
		try{
			String resultString = request(url, headers, parameters, method);
			Result result = mapper.readValue(resultString, Result.class);
			Object resultData = result.getResultData();
			resultData = mapper.readValue(mapper.writeValueAsString(result.getResultData()), gernericType);
			result.setResultData(resultData);
			return result;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public String request2(String url, Map<String, String> headers, Map<String, ?> parameters, String method) {
		try{
			String resultString = request(url, headers, parameters, method);
			return resultString;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}


	/**
	 * URL과 파라미터를 넘겨받아 해당 URL로 POST방식 http 요청 후 응답결과를 반환하는 메소드.
	 *
	 * @param String url    : 호출할 URL
	 * @param HashMap<String, String> headerMap : 헤더에 넣을 값목록
	 * @param HashMap<String, String> paramMap  : 파라미터 목록
	 * @param String method : HTTP Method
	 * @return String       : 응답받은 문자열
	 */
	public String request(String url, Map<String, String> headerMap, Map<String, ?> paramMap, String method) {
		HttpClient client = new DefaultHttpClient();

		try{
			HttpRequestBase request = new HttpPost(url);

			if("POST".equalsIgnoreCase(method)){
				HttpPost httpPost = new HttpPost(url);
				request = httpPost;

				if(isMultipart(paramMap)){
					MultipartEntityBuilder builder = MultipartEntityBuilder.create();
					builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					builder.setCharset(Charset.forName(encoding));

					for(Map.Entry<String, ?> e: paramMap.entrySet()){
						if(e.getValue() instanceof File){
							//File f = (File) e.getValue();
							//builder.addBinaryBody(e.getKey(), f, ContentType.DEFAULT_BINARY, f.getName());
						    builder.addBinaryBody(e.getKey(), (File)e.getValue());
						}
						else{
							ContentType contentType = ContentType.create("text/plain", encoding);
							builder.addTextBody(e.getKey(), (String) e.getValue(), contentType);
						}
					}

					HttpEntity entity = builder.build();
					httpPost.setEntity(entity);
				}
				else if(headerMap!=null && headerMap.containsKey(Contenttype)
						&& headerMap.get(Contenttype).contains("json")){
	                StringEntity userEntity = new StringEntity(mapper.writeValueAsString(paramMap));
					httpPost.setEntity(userEntity);
				}
				else{
					request.addHeader("Content-type", "application/x-www-form-urlencoded;charset=" + encoding);
					httpPost.setEntity(new UrlEncodedFormEntity(convertParam(paramMap), encoding));
				}
			}
			else if("GET".equalsIgnoreCase(method)){
				List<NameValuePair> paramList = convertParam(paramMap);
				request = new HttpGet(url+"?"+URLEncodedUtils.format(paramList, encoding));
			}

			if(headerMap != null){
				for(Map.Entry<String, String> e: headerMap.entrySet()){
					request.setHeader(e.getKey(), e.getValue());
				}
			}

			ResponseHandler<String> rh = new BasicResponseHandler();
            return client.execute(request, rh);
		} catch(Exception e){
			throw new RuntimeException(e);
		} finally{
			client.getConnectionManager().shutdown();
		}

	}

	private boolean isMultipart(Map<String, ?> paramMap) {
		for(Map.Entry<String, ?> e: paramMap.entrySet()){
			if(e.getValue() instanceof File)
				return true;
		}

		return false;
	}

	 private List<NameValuePair> convertParam(Map<String, ?> params){
		 List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		 for(Map.Entry<String, ?> e: params.entrySet()){
			 if(e.getValue() != null)
				 paramList.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
		 }

		 return paramList;
	 }

}