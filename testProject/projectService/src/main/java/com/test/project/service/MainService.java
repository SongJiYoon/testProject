package com.test.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.project.service.mapper.intf.UserGrpMenuMapper;
import com.test.project.service.vo.MenuInfo;
import com.test.project.service.vo.UserGrpMenuInfo;

@Service
public class MainService {

	@Autowired UserGrpMenuMapper userGrpMenuMapper ;
	
	/**
	 * 그룹별 메뉴 리스트 조회
	 * 
	 * @param menuInfo
	 * @return
	 */
	public List<MenuInfo> findMenuByGrps(MenuInfo menuInfo) {
		return userGrpMenuMapper.selectMenuListByGrp(menuInfo);
	}
	
	/**
	 * 그룹 리스트 조회
	 * 
	 * @return
	 */
	public List<UserGrpMenuInfo> findGrpList(){
		return userGrpMenuMapper.selectGrpList();
	}
	
	
	
}
