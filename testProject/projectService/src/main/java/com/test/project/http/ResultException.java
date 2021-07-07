package com.test.project.http;

public class ResultException extends RuntimeException {
	Result<?> result;

	public ResultException(Result<?> result) {
		super();
		this.result = result;
	}

	public Result<?> getResult() {
		return result;
	}

	public void setResult(Result<?> result) {
		this.result = result;
	};
}
