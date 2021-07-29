package com.test.project.controller.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.redjframework.util.ArrayUtil;
import com.redjframework.util.ObjectUtil;
import com.test.project.controller.MainController;
import com.test.project.controller.annotations.AccessAuth;
import com.test.project.service.vo.MenuInfo;
import com.test.project.service.vo.UserSessionInfo;
import com.test.project.util.SpenUtil;

@Component
public class AccessInterceptor implements HandlerInterceptor {

	public static final String SESS_CURR_MENU = "sess_curr_menu";
	
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod h = (HandlerMethod) handler;
            AccessAuth accessAuth = h.getMethodAnnotation(AccessAuth.class);
            if(accessAuth == null){
            	accessAuth = h.getBeanType().getAnnotation(AccessAuth.class);
            }

            if(accessAuth != null && accessAuth.value()){
            	
            	/* session에서 사용자 정보 조회 */
            	String sess = (String) request.getSession().getAttribute(CommonController.SESS_USER_INFO);
            	
            	if(sess == null){
            		response.sendRedirect(request.getContextPath() + "/" + MainController.url_login);
            		return false;
            	}
            	
            	UserSessionInfo userSessionInfo = SpenUtil.fromJson(sess, UserSessionInfo.class);
            	
            	/* 메뉴 전체 정보 조회 */
				List<MenuInfo> menuInfos = userSessionInfo.getMenuInfos();
				String menuNo = ObjectUtil.assign(request.getParameter("menuNo"), (String) request.getSession().getAttribute(SESS_CURR_MENU), getDefaultMenu(menuInfos));
				
				/* 처음 활성화 될 메뉴 조회 */
				menuNo = getFirstActiveMenu(menuInfos, menuNo);
				
				/* 활성화 메뉴의 최상의 대분류 메뉴 조회 */
				MenuInfo topMenuInfo = getTopMenuInfo(menuInfos, menuNo);
				
				request.getSession().setAttribute(SESS_CURR_MENU, menuNo);
				request.setAttribute("user", userSessionInfo);
				request.setAttribute("currTopMenuNo", topMenuInfo.getMenuNo());
				request.setAttribute("currMenuNo", menuNo);
				request.setAttribute("currNavi", getNavi(menuInfos, menuNo));
            }
        }

        return true;
	}


	private Object getNavi(List<MenuInfo> menuInfos, String menuNo) {
		List<String> navis = new ArrayList<String>();
		composeNavi(menuInfos, menuNo, navis);
		Collections.reverse(navis);
		return ArrayUtil.join(navis, " > ");
	}


	private void composeNavi(List<MenuInfo> menuInfos, String menuNo, List<String> navis) {
		for(MenuInfo menuInfo: menuInfos){
			if(menuNo.equals(menuInfo.getMenuNo())){
				navis.add(menuInfo.getMenuNm());
				if(menuInfo.getPrtMenuNo() == null)
					return;

				composeNavi(menuInfos, menuInfo.getPrtMenuNo(), navis);
			}
		}
	}

	/**
	 * 메뉴의 대분류 중 가장 앞의 메뉴 선택
	 * 
	 * @param menuInfos
	 * @return
	 */
	private String getDefaultMenu(List<MenuInfo> menuInfos) {
		for(MenuInfo menuInfo: menuInfos){
			if(menuInfo.getPrtMenuNo() == null){
				return menuInfo.getMenuNo();
			}
		}

		return null;
	}
	
	/**
	 * 활성화 메뉴 조회
	 * 
	 * @param menuInfos
	 * @param menuNo
	 * @return
	 */
	private String getFirstActiveMenu(List<MenuInfo> menuInfos, String menuNo) {
		
		/* 활성화될 메뉴 조회 */
		for(MenuInfo menuInfo: menuInfos){			
			if(menuNo.equals(menuInfo.getMenuNo()) && ObjectUtil.notEmpty(menuInfo.getLinkAddr())){
				return menuInfo.getMenuNo();
			}
		}
		
		/* 활성화 메뉴 없을 경우 하위의 메뉴 조회 */
		for(MenuInfo menuInfo: menuInfos){
			if(menuNo.equals(menuInfo.getPrtMenuNo())){
				return getFirstActiveMenu(menuInfos, menuInfo.getMenuNo());
			}
		}

		return null;
	}
	

	/**
	 * 활성화 메뉴의 최상의 대분류 메뉴 조회
	 * 
	 * @param menuInfos
	 * @param menuNo
	 * @return
	 */
	private MenuInfo getTopMenuInfo(List<MenuInfo> menuInfos, String menuNo) {
		
		for(MenuInfo menuInfo: menuInfos){
			if(menuNo.equals(menuInfo.getMenuNo())){
				if(menuInfo.getPrtMenuNo() == null){
					return menuInfo;
				}
				else{
					return getTopMenuInfo(menuInfos, menuInfo.getPrtMenuNo());
				}
			}
		}

		return null;
	}
}
