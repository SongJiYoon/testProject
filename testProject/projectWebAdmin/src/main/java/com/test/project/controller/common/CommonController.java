package com.test.project.controller.common;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.redjframework.util.ObjectUtil;
import com.test.project.controller.annotations.AccessAuth;
import com.test.project.http.Result;
import com.test.project.http.ResultCode;
import com.test.project.http.ResultException;
import com.test.project.service.common.CommonVO;
import com.test.project.service.vo.UserSessionInfo;
import com.test.project.util.SpenUtil;

@AccessAuth
public class CommonController {
	
	public static final String SESS_USER_INFO = "_USER_INFO_";

	private static final String RESULTDATA_DATA = "data";

	Logger log = LoggerFactory.getLogger(CommonController.class);

	protected UserSessionInfo userInfo(){
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		String sess = (String) attributes.getAttribute(SESS_USER_INFO, ServletRequestAttributes.SCOPE_SESSION);
		UserSessionInfo userInfo;
		try {
			userInfo = SpenUtil.fromJson(sess, UserSessionInfo.class);
			return userInfo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected CustomModelAndView forward(){
		CustomModelAndView view = new CustomModelAndView();
		return view;
	}

	protected CustomModelAndView forward(String name){
		CustomModelAndView view = new CustomModelAndView();
		view.setViewName(name);
		return view;
	}

	public CustomModelAndView redirect(String path){
		CustomModelAndView view = new CustomModelAndView("redirect:" + path);
		return view;
	}

	protected CustomModelAndView success(String msg, Object data){
		return result(ResultCode.success, msg, data);
	}

	protected CustomModelAndView success(Object data){
		return success(null, data);
	}

	protected CustomModelAndView success(){
		return success(null, null);
	}

	protected CustomModelAndView error(String msg){
		return error(msg, null);
	}

	protected CustomModelAndView error(String msg, Object data){
		return result(ResultCode.error, msg, data);
	}

	protected CustomModelAndView error(Object data){
		return error(null, data);
	}

	protected CustomModelAndView error(){
		return error(null);
	}

	protected CustomModelAndView result(ResultCode resultCode){
		return result(resultCode, resultCode.getMsg());
	}

	protected CustomModelAndView result(Result result){
		return result(result.getResultCode(), result.getResultMsg(), result.getResultData());
	}

	protected CustomModelAndView result(ResultCode resultCode, String msg){
		return result(resultCode, msg, null);
	}

	protected CustomModelAndView result(ResultCode resultCode, String msg, Object data){
		return result(resultCode.getCode(), ObjectUtil.nvl(msg, resultCode.getMsg()), data);
	}

	private CustomModelAndView result(String resultCode, String msg, Object data){
		CustomModelAndView view = new CustomModelAndView();
		view.setViewName("error/error.html");
		view.addObject("resultCode", resultCode);
		view.addObject("resultMsg", msg);
		view.addResultData(RESULTDATA_DATA, data);
		return view;
	}
	
	@ExceptionHandler
	public CustomModelAndView _handleException(Exception ex) {
		if(ex instanceof ResultException){
			return result(((ResultException) ex).getResult());
		}

		if(log.isErrorEnabled()){
			log.error("CommonController Exception", ex);
		}

		return error();
	}

	@InitBinder
    public void initBinder(WebDataBinder binder) {
		for(String propertyName : getStartPropertyNames()){
			binder.registerCustomEditor(Date.class, propertyName, new PropertyEditorSupport(){
				@Override
				public void setAsText(String text) throws IllegalArgumentException {
					if(text != null
							&& !"".equals(text)){
						Date date = textToDate(text);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
						if(text.matches("\\w{4}-\\w{2}")){
							calendar.set(Calendar.DAY_OF_MONTH, 1);
						}

						setValue(calendar.getTime());
					}
				}
	        });
		}

		for(String propertyName : getEndPropertyNames()){
			binder.registerCustomEditor(Date.class, propertyName, new PropertyEditorSupport(){
				@Override
				public void setAsText(String text) throws IllegalArgumentException {
					if(text != null
							&& !"".equals(text)){
						Date date = textToDate(text);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.set(Calendar.HOUR_OF_DAY, 23);
						calendar.set(Calendar.MINUTE, 59);
						calendar.set(Calendar.SECOND, 59);
						calendar.set(Calendar.MILLISECOND, 999);
						if(text.matches("\\w{4}-\\w{2}")){
							calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
						}

						setValue(calendar.getTime());
					}
				}
	        });
		}

        binder.registerCustomEditor(Date.class,  new PropertyEditorSupport(){
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if(text != null
						&& !"".equals(text)){
					Date date = textToDate(text);
					setValue(date);
				}
			}
        });

        binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport(){
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if(!"".equals(text))
					super.setAsText(text);
			}
        });
    }

	public static final String RESPONSE_NAME_AT_ATTRIBUTES = ServletRequestAttributes.class.getName() + ".ATTRIBUTE_NAME";

	protected Object download(String name, String url, HttpServletResponse response) throws IOException{

		response.setHeader("Content-Type", "application/octet-stream;");
		String downFileName = java.net.URLEncoder.encode(name, "UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + downFileName + ";");

		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();

		try{
			InputStream in = conn.getInputStream();
			OutputStream out = response.getOutputStream();
			byte[] bs = new byte[5120];
			int l = 0;
			while((l = in.read(bs)) != -1){
				out.write(bs, 0, l);
			}
			out.flush();

		} catch(Exception e){
			e.printStackTrace();
		} finally{
			conn.disconnect();
		}

		return null;
	}

	protected String[] getEndPropertyNames() {
		return new String[]{ "endDate", "enddate" };
	}

	protected String[] getStartPropertyNames() {
		return new String[]{ "startDate", "startdate" };
	}

	protected Date textToDate(String text) {
		if(text.matches("\\w{4}-\\w{2}")){
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
				return dateFormat.parse(text);
			} catch (ParseException e) {
			}
		}
		else if(text.matches("\\w{4}-\\w{2}-\\w{2}")){
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				return dateFormat.parse(text);
			} catch (ParseException e) {
			}
		}
		else{
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat();
				return dateFormat.parse(text);
			} catch (ParseException e) {
			}
		}

		return null;
	}

	protected void paging(CommonVO commonVO, List<? extends CommonVO> list) {
		if(list.size() >= 1){
			CommonVO commonVO2 = list.get(0);
			commonVO.setTotCnt(commonVO2.getTotCnt());
		}
	}

	protected boolean isExcel(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getRequestURI().endsWith(".xls");
	}
}
