package com.test.project.service.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.test.project.service.common.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Alias("userGrpMenuInfo")
public class UserGrpMenuInfo extends CommonVO {
	
	private String grpId;		// 그룹 ID
	private String grpNm;		// 그룹 명
	private String userNo;		// 사용자 번호
	private String menuNo;		// 메뉴 번호
	private Date regDttm;		// 등록 일자
	private Date modDttm;		// 수정 일자
	private String accountCnt;  // 소속 계정수
	private String menuCnt;     // 소속 메뉴수
}
