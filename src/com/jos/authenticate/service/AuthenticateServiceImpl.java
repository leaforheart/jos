package com.jos.authenticate.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;

import redis.clients.jedis.Jedis;

import com.inveno.util.CollectionUtils;
import com.inveno.util.MD5Utils;
import com.inveno.util.PropertyUtils;
import com.inveno.util.StringUtil;
import com.jos.authenticate.dao.AuthenticateDao;
import com.jos.authenticate.model.EmsMtTask;
import com.jos.authenticate.model.User;
import com.jos.authenticate.vo.AuthenticateBean;
import com.jos.common.baseclass.AbstractBaseService;
import com.jos.common.util.Constants;
import com.jos.common.util.SysContext;
import com.jos.redis.RedisClient;

public class AuthenticateServiceImpl extends AbstractBaseService implements AuthenticateService {
	
	private AuthenticateDao authenticateDao;

	public AuthenticateDao getAuthenticateDao() {
		return authenticateDao;
	}

	public void setAuthenticateDao(AuthenticateDao authenticateDao) {
		this.authenticateDao = authenticateDao;
	}

	@Override
	public HashMap<String, Object> login(AuthenticateBean authenticateBean) {
		Jedis jedis = RedisClient.getJedis();
		HashMap<String, Object>  map = new HashMap<String, Object>();
		try {
			User user = authenticateBean.getUser();
			String principal = user.getPrimPrin();
			String credential = user.getCredential();
			
			List<String> para = new ArrayList<String>();
			para.add(principal);
			para.add(principal);
			para.add(principal);
			para.add(principal);
			List<User> userList = authenticateDao.findByHql("from User as user where user.primPrin=? or user.principal1=? or user.principal2=? or user.principal3=?", para);
			if(CollectionUtils.isEmpty(userList)){
				map.put(Constants.RETURN_CODE, "-1");//用户不存在
				return map;
			}
			if(userList.size()>1) {
				map.put(Constants.RETURN_CODE, "-2");//用户在系统中存在多个
				return map;
			}
			User dbUser = userList.get(0);
			String encodeCredential = dbUser.getCredential();
			if(!encodeCredential.equals(MD5Utils.getResult(credential))) {
				map.put(Constants.RETURN_CODE, "-3");//密码错误
				return map;
			}
			dbUser.setStatus("1");
			
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put("primPrin", dbUser.getPrimPrin());
			map.put("principal1", dbUser.getPrincipal1());
			map.put("principal2", dbUser.getPrincipal2());
			map.put("principal3", dbUser.getPrincipal3());
			
			String uuid = UUID.randomUUID().toString();
			jedis.hset(uuid, "userId", dbUser.getId());
			jedis.hset(uuid, "primPrin", dbUser.getPrimPrin());
			if(StringUtil.isNotEmpty(dbUser.getPrincipal1())) {
				jedis.hset(uuid, "principal1", dbUser.getPrincipal1());
			}
			if(StringUtil.isNotEmpty(dbUser.getPrincipal2())) {
				jedis.hset(uuid, "principal2", dbUser.getPrincipal2());
			}
			if(StringUtil.isNotEmpty(dbUser.getPrincipal3())) {
				jedis.hset(uuid, "principal3", dbUser.getPrincipal3());
			}
			jedis.hset(uuid, "credential", encodeCredential);
			jedis.expire(uuid, 60*60*2);
			
			Cookie cookie = new Cookie(Constants.SESSIONID,uuid);
			cookie.setMaxAge(Integer.parseInt(PropertyUtils.getProperty(Constants.COOKIE_TIME)));//存活30分钟
			String domain = PropertyUtils.getProperty(Constants.COOKIE_DOMAIN);
			if(!StringUtil.isEmpty(domain)) {
				cookie.setDomain(domain);
			}
			cookie.setPath("/");
			map.put("cookie",cookie);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE,Constants.SEVER_ERROR);
		} finally {
			RedisClient.returnResource(jedis);
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> enroll(AuthenticateBean authenticateBean) {	
		Jedis jedis = RedisClient.getJedis();
		HashMap<String, Object>  map = new HashMap<String, Object>();
		try {
			String code = authenticateBean.getPhoneCode();
			String primPrin = authenticateBean.getUser().getPrimPrin();
			String codeInRedis = jedis.get(primPrin+Constants.PHONECODE+Constants.USECODE_REGISTER);
			String credential = authenticateBean.getUser().getCredential();
			
			if(codeInRedis==null||!codeInRedis.equals(code)) {
				map.put(Constants.RETURN_CODE,"-1");//验证码错误
				return map;
			}
			List<String> parameters = new ArrayList<String>();
			parameters.add(primPrin);
			parameters.add(primPrin);
			parameters.add(primPrin);
			parameters.add(primPrin);
			List<User> list = authenticateDao.findByHql("from User where prim_prin=? or principal1=? or principal2=? or principal3=?", parameters);
			if(list.size()>0) {
				map.put(Constants.RETURN_CODE,"-2");//用户已存在
				return map;
			}
			User user = new User();
			user.setPrimPrin(primPrin);
			user.setCredential(MD5Utils.getResult(credential));
			user.setCreateTime(new Date());
			user.setLastUpdateTime(new Date());
			user.setStatus("0");
			authenticateDao.save(user);
			jedis.del(primPrin+Constants.PHONECODE+Constants.USECODE_REGISTER);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE,Constants.SEVER_ERROR);
		} finally {
			RedisClient.returnResource(jedis);
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> resetPassword(AuthenticateBean authenticateBean) {
		Jedis jedis = RedisClient.getJedis();
		HashMap<String, Object>  map = new HashMap<String, Object>();
		try {
			String encodeCredential = jedis.hget(SysContext.getUuid(), "credential");
			String credential = authenticateBean.getUser().getCredential();
			if(encodeCredential==null||!encodeCredential.equals(MD5Utils.getResult(credential))) {
				map.put(Constants.RETURN_CODE, "-1");
				return map;
			}
			String userId = jedis.hget(SysContext.getUuid(), "userId");
			if(StringUtil.isEmpty(userId)) {
				map.put(Constants.RETURN_CODE, "-2");
				return map;
			}
			User user = authenticateDao.findById(userId, User.class);
			String newCredential = authenticateBean.getNewCredential();
			user.setCredential(newCredential);
			user.setLastUpdateTime(new Date());
			jedis.hset(SysContext.getUuid(), "credential", newCredential);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE,Constants.SEVER_ERROR);
		} finally {
			RedisClient.returnResource(jedis);
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> phoneCode(AuthenticateBean authenticateBean) {
		Jedis jedis = RedisClient.getJedis();
		HashMap<String, Object>  map = new HashMap<String, Object>();
		
		try {
			String primPrin = authenticateBean.getUser().getPrimPrin();
			List<String> parameters = new ArrayList<String>();
			parameters.add(primPrin);
			parameters.add(primPrin);
			parameters.add(primPrin);
			parameters.add(primPrin);
			List<User> list = authenticateDao.findByHql("from User where primPrin=? or principal1=? or principal2=? or principal3=? ", parameters);
			String phoneCodeUse = authenticateBean.getPhoneCodeUse();
			if(Constants.USECODE_REGISTER.equals(phoneCodeUse)) {
				if(list.size()>0) {
					map.put(Constants.RETURN_CODE, "-1");
					return map;
				}
			}else if(Constants.USECODE_GETBACK.equals(phoneCodeUse)) {
				if(list==null||list.size()!=1) {
					map.put(Constants.RETURN_CODE, "-2");
					return map;
				}
			}
			String code = getCode(primPrin,phoneCodeUse);
			if(StringUtil.isEmpty(code)) {
				map.put(Constants.RETURN_CODE, "-3");
				return map;
			}
			jedis.set(primPrin+Constants.PHONECODE+phoneCodeUse, code);
			jedis.expire(primPrin+Constants.PHONECODE+phoneCodeUse, 60*10);
			map.put(Constants.RETURN_CODE,Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE,Constants.SEVER_ERROR);
		} finally {
			RedisClient.returnResource(jedis);
		} 
		
		return map;
	}
	
	private String getCode(String phone,String phoneCodeUse) {
		Jedis jedis = RedisClient.getJedis();
		String code = "";
		try {
			if(StringUtil.isNotEmpty(jedis.get(phone+Constants.PHONECODE+phoneCodeUse))){
				code = jedis.get(phone+Constants.PHONECODE+phoneCodeUse);
			}
			if(StringUtil.isEmpty(code)) {
				code = String.valueOf(Math.round((Math.random()*9+1)*100000));;
			}
			EmsMtTask emt = new EmsMtTask();
			emt.setTaskId(UUID.randomUUID().toString());
			emt.setChannelId(Constants.CHANNEL_ID);
			emt.setCreateTime(new Date());
			emt.setExt("");
			if(Constants.USECODE_REGISTER.equals(phoneCodeUse)) {
				emt.setMsgContent(Constants.REGISTER_CONTENT.replace(Constants.CONTENT_CODE, code));
			}else if(Constants.USECODE_GETBACK.equals(phoneCodeUse)) {
				emt.setMsgContent(Constants.GETBACK_CONTENT.replace(Constants.CONTENT_CODE, code));
			}else {
				return "";
			}
			emt.setPriority(1);
			emt.setSendTime(new Date());
			emt.setToMobile(phone);
			authenticateDao.save(emt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisClient.returnResource(jedis);
		} 
		return code;
	}

	@Override
	public HashMap<String, Object> loginOut(AuthenticateBean authenticateBean) {
		Jedis jedis = RedisClient.getJedis();
		HashMap<String, Object>  map = new HashMap<String, Object>();
		
		try {
			String uuid = SysContext.getUuid();
			jedis.hdel(uuid);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE,Constants.SEVER_ERROR);
		} finally {
			RedisClient.returnResource(jedis);
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> setPassword(AuthenticateBean authenticateBean) {
		Jedis jedis = RedisClient.getJedis();
		HashMap<String, Object>  map = new HashMap<String, Object>();
		
		try {
			String code = authenticateBean.getPhoneCode();
			String primPrin = authenticateBean.getUser().getPrimPrin();
			String codeInRedis = jedis.get(primPrin+Constants.PHONECODE+Constants.USECODE_GETBACK);
			if(codeInRedis==null||!codeInRedis.equals(code)) {
				map.put(Constants.RETURN_CODE,"-1");//验证码错误
				return map;
			}
			String newCredential = authenticateBean.getNewCredential();
			List<String> parameters = new ArrayList<String>();
			parameters.add(primPrin);
			parameters.add(primPrin);
			parameters.add(primPrin);
			parameters.add(primPrin);
			List<User> list = authenticateDao.findByHql("from User where primPrin=? or principal1=? or principal2=? or principal3=?", parameters);
			if(list==null||list.size()!=1) {
				map.put(Constants.RETURN_CODE, "-2");
				return map;
			}
			User user = list.get(0);
			user.setCredential(newCredential);
			user.setLastUpdateTime(new Date());
			jedis.del(primPrin+Constants.PHONECODE+Constants.USECODE_GETBACK);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE,Constants.SEVER_ERROR);
		} finally {
			RedisClient.returnResource(jedis);
		}
		
		return map;
	}
	
	@Override
	public HashMap<String,String> getUserInfo() {
		HashMap<String,String> map = (HashMap<String, String>) SysContext.getUserMap();
		String userId = map.get("userId");
		if(StringUtil.isEmpty(userId)) {
			map.clear();
			map.put(Constants.RETURN_CODE, "-1");
			return map;
		}
		User user = authenticateDao.findById(userId, User.class);
		if(user==null||StringUtil.isEmpty(user.getId())) {
			map.clear();
			map.put(Constants.RETURN_CODE, "-1");
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}


}
