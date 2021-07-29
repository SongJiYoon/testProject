package com.test.project.service.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("userInfo")
public class UserInfo {
	
	private String userNo;		    // 사용자 번호
	private String userId;		    // 사용자 ID
	private String pwd;			    // 비밀번호
	private String userNm;		    // 사용자 명
	private String telNo;		    // 연락처
	private String userDivCd;	    // 사용자 구분
	private String userStsCd;	    // 상태
	private String photoFileNo;     // 사진 파일 번호
	private Date regDttm;			// 등록 일자
	private Date modDttm;			// 수정 일자
}
