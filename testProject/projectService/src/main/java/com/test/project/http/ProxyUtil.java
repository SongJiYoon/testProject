package com.test.project.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProxyUtil {
	static ObjectMapper mapper = new ObjectMapper();

	static{
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static <T> Result proxy(String u, Map<String, String> headers, InputStream in, Class<T> gernericType) {
		return proxy(u, headers, null, null, in, gernericType);
	}
	public static <T> Result proxy(String u, Map<String, String> headers, Map<String, String> parameters, Class<T> gernericType) {
		return proxy(u, headers, parameters, null, null, gernericType);
	}
	public static <T> Result proxy(String u, Map<String, String> headers, String jsonObject, Class<T> gernericType) {
		return proxy(u, headers, null, jsonObject, null, gernericType);
	}
	private static <T> Result proxy(String u, Map<String, String> headers, Map<String, String> parameters, String jsonObject, InputStream in, Class<T> gernericType) {
		long startTime = System.currentTimeMillis();
		try{
			String resultString = proxy(u, headers, parameters, jsonObject, in);
			Result result = mapper.readValue(resultString, Result.class);
			Object resultData = result.getResultData();

			if(resultData instanceof List){
				JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, gernericType);
				resultData = mapper.readValue(mapper.writeValueAsString(result.getResultData()), type);

			}
			else{
				resultData = mapper.readValue(mapper.writeValueAsString(result.getResultData()), gernericType);
			}

			result.setResultData(resultData);
			return result;
		} catch(Exception e){
			//time
			long endTime = System.currentTimeMillis();
			System.out.println("[Url] : "+ (endTime-startTime));

			//url
			System.out.println("[Url] : " + u);
			//header
			if(headers != null) {
				Set<String> keySet = headers.keySet();
				for(String key: keySet)
					System.out.println("[Header] : " + key + "=" +  headers.get(key));
			}
			//parameters
			if(parameters != null) {
				Set<String> keySet = parameters.keySet();
				for(String key: keySet)
					System.out.println("[Parameters] : " + key + "-" +  parameters.get(key));
			}
			//jsonObject
			System.out.println("[JsonObject] : " + jsonObject);

			throw new RuntimeException(e);
		}
	}

	public static String proxy(String u, Map<String, String> headers, InputStream in) {
		return proxy(u, headers, null, null, in);
	}
	public static String proxy(String u, Map<String, String> headers, Map<String, String> parameters) {
		return proxy(u, headers, parameters, null, null);
	}
	public static String proxy(String u, Map<String, String> headers, String jsonObject) {
		return proxy(u, headers, null, jsonObject, null);
	}

	private static String proxy(String u, Map<String, String> headers, Map<String, String> parameters, String jsonObject, InputStream in){
		StringBuilder paramBuilder = new StringBuilder();

		try {
			if(parameters != null && parameters.size() > 0) {
				Collection<String> paramKeys = parameters.keySet();
				Iterator<String> iterator = paramKeys.iterator();
				int idx = 0;

				while (iterator.hasNext()) {
					String paramKey = String.valueOf(iterator.next());

					if(idx == 0) {
						paramBuilder.append(paramKey + "=" + URLEncoder.encode(parameters.get(paramKey), "UTF-8"));
					} else {
						paramBuilder.append("&" + paramKey + "=" + URLEncoder.encode(parameters.get(paramKey), "UTF-8"));
					}

					idx++;
				}
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}

		return defaultProxy(u, headers,  paramBuilder.toString(), jsonObject, in);
	}

	private static String defaultProxy(String u, Map<String, String> headers,  String paramString, String jsonString, InputStream in){
		HttpURLConnection conn = null;

		try{
			URL url = new URL(u);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(20000);//20sec
			conn.setReadTimeout(20000);//20sec
			conn.setDoOutput(true);
			conn.setDoInput(true);

			if(headers != null){
				for(Map.Entry<String, String> e: headers.entrySet()){
					conn.addRequestProperty(e.getKey(), e.getValue());
				}
			}


			conn.connect();

			OutputStream out = conn.getOutputStream();
			byte[] bs = new byte[5120];
			int c = 0;

			if(paramString != null) {
				byte[] paramBytes = paramString.getBytes();
				if(paramBytes != null && paramBytes.length > 0 ) {
					out.write(paramBytes);
				}
			}

			if(jsonString != null) {
				byte[] jsonBytes = jsonString.getBytes();
				if(jsonBytes != null && jsonBytes.length > 0 ) {
					out.write(jsonBytes);
				}
			}


			if(in != null) {
				while((c = in.read(bs)) != -1){
					out.write(bs, 0, c);
				}
			}

			BufferedInputStream reader = new BufferedInputStream(conn.getInputStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bs = new byte[5120];
			c = 0;

			while((c = reader.read(bs)) != -1){
				baos.write(bs, 0, c);
			}

			reader.close();
			String charset = conn.getContentEncoding();
			if(charset == null)
				charset = "UTF-8";

			return new String(baos.toByteArray(), charset);
		} catch(Exception e){
			throw new RuntimeException(e);
		} finally{
			conn.disconnect();
		}
	}
}
