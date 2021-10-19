package com.test.project.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.redjframework.util.ArrayUtil;
import com.test.project.controller.annotations.AccessAuth;
import com.test.project.controller.common.CommonController;
import com.test.project.http.ResultCode;
import com.test.project.service.MainService;
import com.test.project.service.UserService;
import com.test.project.service.vo.MenuInfo;
import com.test.project.service.vo.UserGrpMenuInfo;
import com.test.project.service.vo.UserInfo;
import com.test.project.service.vo.UserSessionInfo;
import com.test.project.util.SpenUtil;

@Controller
@AccessAuth(false)
public class MainController extends CommonController {
	public static String url_error = "error/error.html";

	public static String url_login = "login/loginForm.html";

	@Autowired UserService userService;

	@Autowired MainService mainService;
	
	Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 메인 화면 조회
	 * (login 화면에서 연결됨)
	 * 
	 * @param menuNo
	 * @param tutorNo
	 * @return
	 */
	@RequestMapping("index.html")
	@AccessAuth
	public Object index(HttpServletRequest request, @RequestParam(value="menuNo", required=false) String menuNo){

		/* Session 사용자 정보 가져오기 */
		List<MenuInfo> menuInfos = userInfo().getMenuInfos();
		
		String title = "";
		if(menuNo == null){
			for(MenuInfo menuInfo: menuInfos){
				if(menuInfo.getPrtMenuNo() == null){
					menuNo = menuInfo.getMenuNo();
					break;
				}
			}
		}

		for(MenuInfo menuInfo: menuInfos){
			if(menuNo.equals(menuInfo.getMenuNo())){
				title = menuInfo.getMenuNm();
				break;
			}
		}
		
		request.setAttribute("subTitle", title);
 
		return forward("common/index").addObject("menuNo", menuNo);
	}
	
	@GetMapping("/login/loginForm.html")
	public Object loginForm(){
		return forward("common/login/loginForm");
	}

	@RequestMapping("/error/error")
	public Object errorPage(HttpServletRequest request){
		
		return forward("common/error/error").addObject("resultCode", request.getAttribute("javax.servlet.error.status_code"));
	}
	
	@PostMapping(value="/login/login")
	public Object login(@RequestParam("userId") String userId, @RequestParam("pwd") String pwd, HttpServletRequest request) throws Exception{
		if(("".equals(userId) || null == userId) || ("".equals(pwd) || null == pwd)){
			/* not_found_user(사용자 정보를 찾을 수 없습니다.) */
			return result(ResultCode.not_found_user);
		}
		
		UserSessionInfo sessionInfo = new UserSessionInfo();
		sessionInfo.setUserId(userId);
		sessionInfo.setPwd(pwd);
		
		/* 사용자 정보 조회 호출 */
		UserInfo userInfo = userService.getUserInfo(userId, pwd);
		if(userInfo == null){
			/* not_found_user(사용자 정보를 찾을 수 없습니다.) */
			return result(ResultCode.not_found_user);
		}
		
		/* 사용자 그룹 리스트 조회 호출 */
		List<String> userGrps = userService.getUserGrpList(userInfo.getUserNo());
		MenuInfo menuInfo = new MenuInfo();
		menuInfo.setUserGrpNos(userGrps);
		
		/* 그룹별 메뉴리스트 조회 호출 */
		List<MenuInfo> menuInfos = mainService.findMenuByGrps(menuInfo);

		/* not_found_role(교사에 할당된 권한이 없습니다. 관리자에게 문의하여 주세요) */
		if(menuInfos.size() == 0){
			return result(ResultCode.not_found_role);
		}
		
		/* 그룹 정보 조회 호출 */
		List<UserGrpMenuInfo> grpList = mainService.findGrpList();
		

		for(MenuInfo menuInfo1: menuInfos){
			int childCount = 0;
			for(MenuInfo menuInfo2: menuInfos){
				if(menuInfo2.getPrtMenuNo() != null && menuInfo2.getPrtMenuNo().equals(menuInfo1.getMenuNo())){
					childCount++;					
				}
			}

			menuInfo1.setChildCount(childCount);
		}

		sessionInfo.setMenuInfos(menuInfos);
		sessionInfo.setUserNo(userInfo.getUserNo());
		sessionInfo.setUserId(userInfo.getUserId());
		sessionInfo.setUserNm(userInfo.getUserNm());
		sessionInfo.setUserDivCd(userInfo.getUserDivCd());  //add jhr - 20160420
		sessionInfo.setGrpRoles(userGrps);

		List<String> roleNames = new ArrayList<String>();
		for(UserGrpMenuInfo grpInfo: grpList){
			if(userGrps.contains(grpInfo.getGrpId())){
				roleNames.add(grpInfo.getGrpNm());
			}
		}

		sessionInfo.setTutorGrpNm(ArrayUtil.join(roleNames, ","));
		String sess = SpenUtil.json(sessionInfo);
		request.getSession().setAttribute(SESS_USER_INFO, sess);
		
		String go = String.format("%s/index.html?menuNo=%s", request.getContextPath(), menuInfos.get(0).getMenuNo());
		
		return success().addObject("go", go);
	}
	
	@GetMapping(value="/login/logout")
	public Object loginOut(HttpServletRequest request){
		request.getSession().invalidate();
		return redirect("/index.html");
	}

		
}
