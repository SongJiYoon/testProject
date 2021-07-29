package com.test.project.service.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.test.project.service.common.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Alias("userSessionInfo")
public class UserSessionInfo extends CommonVO implements Serializable {

    private static final long serialVersionUID = 1L;

	private String userNo;		// 사용자 번호
	private String userId;		// 사용자 ID
	private String pwd;			// 비밀번호
	private String userNm;		// 사용자 명
	private String userDivCd;	// 사용자 구분
	private String userStsCd;	// 상태
	private Date regDt;			// 등록 일자
	private Date modDt;			// 수정 일자

    /* 그룹별권한 및 그룸명표시 할 경우 사용 */
    String tutorGrpNo;      	// 교사그룹번호
    String tutorGrpNm;			// 교사그룹명(메인화면의 그룹명표시)
    
    List<String> grpRoles = null;
	List<MenuInfo> menuInfos = new ArrayList<MenuInfo>();
}