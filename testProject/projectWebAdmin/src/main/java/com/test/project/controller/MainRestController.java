//package com.test.project.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.redjframework.util.ArrayUtil;
//import com.test.project.controller.annotations.AccessAuth;
//import com.test.project.controller.common.CommonController;
//import com.test.project.http.ResultCode;
//import com.test.project.service.MainService;
//import com.test.project.service.UserService;
//import com.test.project.service.vo.MenuInfo;
//import com.test.project.service.vo.UserGrpMenuInfo;
//import com.test.project.service.vo.UserInfo;
//import com.test.project.service.vo.UserSessionInfo;
//import com.test.project.util.SpenUtil;
//
//@RestController
//@AccessAuth(false)
//public class MainRestController extends CommonController{
//
//	@Autowired MainService mainService;
//	@Autowired UserService userService;
//	
//	@PostMapping(value="/login/login")
//	public Object login(@RequestParam("userId") String userId, @RequestParam("pwd") String pwd, HttpServletRequest request) throws Exception{
//		if(("".equals(userId) || null == userId) || ("".equals(pwd) || null == pwd)){
//			/* not_found_user(사용자 정보를 찾을 수 없습니다.) */
//			return result(ResultCode.not_found_user);
//		}
//		
//		UserSessionInfo sessionInfo = new UserSessionInfo();
//		sessionInfo.setUserId(userId);
//		sessionInfo.setPwd(pwd);
//		
//		/* 사용자 정보 조회 호출 */
//		UserInfo userInfo = userService.getUserInfo(userId, pwd);
//		if(userInfo == null){
//			/* not_found_user(사용자 정보를 찾을 수 없습니다.) */
//			return result(ResultCode.not_found_user);
//		}
//		
//		/* 사용자 그룹 리스트 조회 호출 */
//		List<String> userGrps = userService.getUserGrpList(userInfo.getUserNo());
//		MenuInfo menuInfo = new MenuInfo();
//		menuInfo.setUserGrpNos(userGrps);
//		
//		/* 그룹별 메뉴리스트 조회 호출 */
//		List<MenuInfo> menuInfos = mainService.findMenuByGrps(menuInfo);
//
//		/* not_found_role(교사에 할당된 권한이 없습니다. 관리자에게 문의하여 주세요) */
//		if(menuInfos.size() == 0){
//			return result(ResultCode.not_found_role);
//		}
//		
//		/* 그룹 정보 조회 호출 */
//		List<UserGrpMenuInfo> grpList = mainService.findGrpList();
//		
//
//		for(MenuInfo menuInfo1: menuInfos){
//			int childCount = 0;
//			for(MenuInfo menuInfo2: menuInfos){
//				if(menuInfo2.getPrtMenuNo() != null && menuInfo2.getPrtMenuNo().equals(menuInfo1.getMenuNo())){
//					childCount++;					
//				}
//			}
//
//			menuInfo1.setChildCount(childCount);
//		}
//
//		sessionInfo.setMenuInfos(menuInfos);
//		sessionInfo.setUserNo(userInfo.getUserNo());
//		sessionInfo.setUserId(userInfo.getUserId());
//		sessionInfo.setUserNm(userInfo.getUserNm());
//		sessionInfo.setUserDivCd(userInfo.getUserDivCd());  //add jhr - 20160420
//		sessionInfo.setGrpRoles(userGrps);
//
//		List<String> roleNames = new ArrayList<String>();
//		for(UserGrpMenuInfo grpInfo: grpList){
//			if(userGrps.contains(grpInfo.getGrpId())){
//				roleNames.add(grpInfo.getGrpNm());
//			}
//		}
//
//		sessionInfo.setTutorGrpNm(ArrayUtil.join(roleNames, ","));
//		String sess = SpenUtil.json(sessionInfo);
//		request.getSession().setAttribute(SESS_USER_INFO, sess);
//		
//		String go = String.format("%s/index?menuNo=%s", request.getContextPath(), menuInfos.get(0).getMenuNo());
//		
//		return success().addObject("go", go);
//	}
//	
//	@GetMapping(value="/login/logout")
//	public Object loginOut(HttpServletRequest request){
//		request.getSession().invalidate();
//		return redirect("/");
//	}
//	
////	@RequestMapping("test")
////	public Object test() {
////
////		System.out.println("??");
////		return result(ResultCode.success, ResultCode.success.getMsg(), null);
////	}
//	
//}
