package com.jos.authenticate.action;

import java.util.HashMap;

import javax.servlet.http.Cookie;

import com.inveno.util.JsonUtil;
import com.jos.authenticate.service.AuthenticateService;
import com.jos.authenticate.vo.AuthenticateBean;
import com.jos.common.baseclass.BaseAction;
import com.jos.common.util.Constants;
import com.jos.common.util.SysContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings({ "rawtypes", "serial" })
public class AuthenticateAction extends BaseAction implements ModelDriven {
	
	public AuthenticateService getAuthenticateService() {
		return authenticateService;
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}

	public AuthenticateBean getAuthenticateBean() {
		return authenticateBean;
	}

	public void setAuthenticateBean(AuthenticateBean authenticateBean) {
		this.authenticateBean = authenticateBean;
	}

	private AuthenticateService authenticateService;
	private AuthenticateBean authenticateBean;

	@Override
	public Object getModel() {
		if(authenticateBean==null) {
			authenticateBean = new AuthenticateBean();
		}
		return authenticateBean;
	}
	
	public void login(){
		HashMap<String,Object> map = authenticateService.login(authenticateBean);
		Cookie cookie = (Cookie) map.get("cookie");
		if(cookie!=null) {
			getResponse().addCookie(cookie);
			map.remove("cookie");
		}
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(map));
	}
	
	public void enroll(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(authenticateService.enroll(authenticateBean)));
	}
	
	public void resetPassword(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(authenticateService.resetPassword(authenticateBean)));	
	}
	
	public void phoneCode(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(authenticateService.phoneCode(authenticateBean)));
	}
	
	public void loginOut(){
		HashMap<String,Object> map = authenticateService.loginOut(authenticateBean);
		Cookie[] cookies = getRequest().getCookies();
		for(Cookie cookie:cookies) {
			if(Constants.SESSIONID.equals(cookie.getName())) {
				cookie.setMaxAge(0);//设置cookie失效
				getResponse().addCookie(cookie);
			}
		}
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(map));
	}
	
	public void setPassword(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(authenticateService.setPassword(authenticateBean)));
	}
	
	public void getUserInfo(){
		HashMap<String,String> map = authenticateService.getUserInfo();
		HashMap<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.putAll(map);
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(returnMap));
	}
	
}
