package com.test.project.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.project.service.mapper.intf.UserMapper;
import com.test.project.service.vo.UserInfo;
import com.test.project.util.CryptoUtil;


@Service
public class UserService {
	Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired UserMapper userMapper;
	@Autowired CryptoUtil crypto;
	
	/**
	 * [로그인] 로그인
	 * @param userID
	 * @param passwd
	 * @return
	 */
	public UserInfo getUserInfo(String userId, String pwd) throws Exception{
		
		/* 사용자 정보 조회 */
		UserInfo userInfo = userMapper.selectUserInfo(userId);
		if(userInfo == null){
			return null;
		}
		
		/* 비밀번호 체크 */
		//String encPasswd = crypto.sha512(pwd, "UTF-8");
		if(!pwd.equals(userInfo.getPwd())){
			return null;
		}
		        
		return userInfo;
	}
	
	/**
	 * 사용자 그룹 정보 리스트 조회
	 * 
	 * @param userNo
	 * @return
	 */
	public List<String> getUserGrpList(String userNo) {
		List<String> grpList = new ArrayList<String>();
		for(String grp : userMapper.selectUserGrpList(userNo)){
			grpList.add(grp);
		}
		return grpList;
	}
	
	/**
	 * App 교사 로그인정보 조회
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public UserInfo getUserInfo(String userId) throws Exception{
		
		return userMapper.selectUserInfo(userId);
	}

}
