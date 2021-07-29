package com.test.project.service.mapper.intf;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.test.project.service.vo.UserInfo;

@Repository
@Mapper
public interface UserMapper {
	
	/* 사용자 로그인 정보 조회 */
	UserInfo selectUserInfo(String userId);
	/* 사용자 그룹 리스트 조회 */
	List<String> selectUserGrpList(String userNo);
	/* 구몬세상 사용자 로그인 정보 조회 */
	UserInfo selectClassUserInfo(String userId);
}
