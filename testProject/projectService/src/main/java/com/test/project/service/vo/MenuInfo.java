package com.test.project.service.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.project.service.common.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Alias("menuInfo")
public class MenuInfo extends CommonVO implements Serializable {
	
	private static final long serialVersionUID = 8353385232177889469L;
	
	private String menuNo;			// 메뉴 번호
	private String prtMenuNo;		// 상위 메뉴 번호	
	private String menuNm;			// 메뉴 명
	private int menuOdr;			// 메뉴 순서
	private String linkAddr;		// 연결 주소
	private String useYn;			// 사용 여부
	private Date regDt;				// 등록 일자 
	private Date modDt;				// 수정 일자 
	private String no;				// 번호
	private String gubun;			// 구분자

	int childCount;					// 학생 수
	private String grpId;			// 그룹 아이디
	List<String> UserGrpNos;		// 사용자그룹번호
	List<String> menuNos;			// 메뉴번호들
	@JsonIgnore
	private String selected;		// 선택자
	
	
}
