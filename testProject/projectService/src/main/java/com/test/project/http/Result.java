package com.test.project.http;

import java.util.Date;

public class Result<T> {
	
	String resultCode;

	String resultMsg;

	T resultData;

	Date resultTime;

	public Result() {
		super();
	}

	public Result(ResultCode resultCode) {
        this(resultCode, resultCode.getMsg());
	}

	public Result(ResultCode resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode.getCode();
		this.resultMsg = resultMsg;
        if(this.resultMsg == null)
        	this.resultMsg = resultCode.getMsg();
	}

	public String getResultCode() {
		return resultCode;
	}

	public Result setResultCode(String resultCode) {
		this.resultCode = resultCode;
		return this;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public Result setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
		return this;
	}

	public T getResultData() {
		return resultData;
	}

	public Result setResultData(T resultData) {
		this.resultData = resultData;
		return this;
	}

	public Date getResultTime() {
		return resultTime;
	}

	public void setResultTime(Date resultTime) {
		this.resultTime = resultTime;
	}
}
