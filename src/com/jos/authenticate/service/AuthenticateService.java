package com.jos.authenticate.service;

import java.util.HashMap;

import com.jos.authenticate.vo.AuthenticateBean;
import com.jos.common.baseclass.IBaseService;

public interface AuthenticateService extends IBaseService {
	HashMap<String,Object> login(AuthenticateBean authenticateBean);
	HashMap<String,Object> enroll(AuthenticateBean authenticateBean);
	HashMap<String,Object> resetPassword(AuthenticateBean authenticateBean);
	HashMap<String,Object> phoneCode(AuthenticateBean authenticateBean);
	HashMap<String,Object> loginOut(AuthenticateBean authenticateBean);
	HashMap<String,Object> setPassword(AuthenticateBean authenticateBean);
	HashMap<String,String> getUserInfo(String uuid);
}
