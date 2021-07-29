package com.test.project.service.mapper.intf;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.test.project.service.vo.MenuInfo;
import com.test.project.service.vo.UserGrpMenuInfo;

@Repository
@Mapper
public interface UserGrpMenuMapper {

	/* 그룹별 메뉴 리스트 조회 */
	List<MenuInfo> selectMenuListByGrp(MenuInfo menuInfo);
	/* 그룹 리스트 조회 */		
	List<UserGrpMenuInfo> selectGrpList();
	
}
