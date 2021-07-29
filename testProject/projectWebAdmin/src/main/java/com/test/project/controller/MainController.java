package com.test.project.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.redjframework.util.ArrayUtil;
import com.test.project.controller.annotations.AccessAuth;
import com.test.project.controller.common.CommonController;
import com.test.project.http.ResultCode;
import com.test.project.service.CmsFileService;
import com.test.project.service.MainService;
import com.test.project.service.UserService;
import com.test.project.service.vo.CmsFileRevision;
import com.test.project.service.vo.MenuInfo;
import com.test.project.service.vo.UserGrpMenuInfo;
import com.test.project.service.vo.UserInfo;
import com.test.project.service.vo.UserSessionInfo;
import com.test.project.util.SpenUtil;

@Controller
@RequestMapping("/*")
@AccessAuth(false)
public class MainController extends CommonController {
	public static String url_error = "error/error.html";

	public static String url_login = "login/loginForm.html";

	@Autowired UserService userService;

	@Autowired MainService mainService;
	
	@Autowired CmsFileService cmsFileService;

	@Value("${server.uploadTempDir}")
	String serverUploadTempDir;
	
	Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 메인 화면 조회
	 * (login 화면에서 연결됨)
	 * 
	 * @param menuNo
	 * @param tutorNo
	 * @return
	 */
	@RequestMapping("index")
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

		return forward("index.html").addObject("menuNo", menuNo);
	}

	/**
	 * 로그인
	 * 
	 * @param userid
	 * @param pwd
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login/*", method=RequestMethod.POST)
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
	

	@RequestMapping(value="/login/*", method=RequestMethod.GET)
	public Object loginForm(){
		System.out.println("뭔개같은");
		return forward();
	}

	@RequestMapping(value="/login/logout", method=RequestMethod.GET)
	public Object loginOut(HttpServletRequest request){
		request.getSession().invalidate();
		return redirect("/");
	}

	@RequestMapping("/error/*")
	public Object errorPage(HttpServletRequest request){
		
		return forward().addObject("resultCode", request.getAttribute("javax.servlet.error.status_code"));
	}

	@RequestMapping("/plupload")
	@ResponseBody
	public Object plupload(
			@RequestBody MultipartFile file,
			@RequestParam String name,
			@RequestParam(defaultValue="1") int chunks,
			@RequestParam(defaultValue="0") int chunk,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
        File savedFile = new File(serverUploadTempDir, name);

        if(!savedFile.exists()
        		|| chunk == 0){
        	file.transferTo(savedFile);
        }
        else{
        	FileOutputStream out = new FileOutputStream(savedFile, true);
        	InputStream in = file.getInputStream();

        	try{
        		byte[] bs = new byte[5120];
        		int bl = 0;
        		while((bl = in.read(bs)) != -1){
        			out.write(bs, 0, bl);
        		}
        		out.flush();
        	} finally{
        		in.close();
        		out.close();
        	}
        }

        if(chunk + 1 == chunks){
        	MultipartFile multipartFile = new MockMultipartFile("file",
        			name,
                    "application/octet-stream", new FileInputStream(savedFile));

        	CmsFileRevision cmsFile = cmsFileService.newCmsUploadFile(name, multipartFile);
        	return success(cmsFile);
        }

        return success();
	}

	@RequestMapping("/plfiles")
	@ResponseBody
	public Object plfiles(@RequestBody String[] fileOids){
		List<CmsFileRevision> files = new ArrayList<CmsFileRevision>();

		for(String foid: fileOids){
			files.add(cmsFileService.getMaxCmsFileRevision(foid));
		}

		return success(files);
	}
		
}
