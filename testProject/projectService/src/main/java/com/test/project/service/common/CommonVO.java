package com.test.project.service.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redjframework.bean.annotations.Bean;

@JsonIgnoreProperties(ignoreUnknown=true, value={"callbacks"})
@Bean 
public class CommonVO {

	public static enum YN { Y, N };

	int page = 1;
	int totCnt;
	int cntPerPage = 15;
	int startPage;
	int endPage;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotCnt() {
		return totCnt;
	}

	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}

	public int getCntPerPage() {
		return cntPerPage;
	}

	public void setCntPerPage(int cntPerPage) {
		this.cntPerPage = cntPerPage;
	}

	public int getStartPage() {
		startPage = (this.getPage() - 1) * this.getCntPerPage() + 1;
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		endPage = this.getPage() * this.getCntPerPage();
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getTotPage(){
		return (int) Math.ceil((double)totCnt / cntPerPage);
	}
	
}
