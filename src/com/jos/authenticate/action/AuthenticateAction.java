package com.jos.authenticate.action;

import java.util.HashMap;

import com.inveno.util.JsonUtil;
import com.jos.authenticate.service.AuthenticateService;
import com.jos.authenticate.vo.AuthenticateBean;
import com.jos.common.baseclass.BaseAction;
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
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(authenticateService.login(authenticateBean)));
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
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(authenticateService.loginOut(authenticateBean)));
	}
	
	public void setPassword(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(authenticateService.setPassword(authenticateBean)));
	}
	
	public void getUserInfo(){
		HashMap<String,String> map = authenticateService.getUserInfo(SysContext.getUuid());
		HashMap<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.putAll(map);
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(returnMap));
	}
	
}
