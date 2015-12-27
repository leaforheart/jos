package com.jos.common.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;

import com.inveno.util.JsonUtil;
import com.inveno.util.MemCachedUtil;
import com.inveno.util.PropertyUtils;
import com.inveno.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.jos.authenticate.model.User;
import com.jos.authenticate.service.AuthenticateServiceImpl;
import com.jos.common.baseclass.IBaseDAO;
import com.jos.common.exception.BusinessException;
import com.jos.redis.RedisClient;

/**
 * 获得容器及全�?��源的工具�?
 * 
 * @author yaoyuan
 */
public final class SysContext {
	private SysContext() {
		// no instance
	}

	/** 缓存里所有KEY */
	private static List<String> cacheKeys = new LinkedList<String>();

	/** 把session存起�?*/
	private static Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>(
			200);;

	/** 当前的请�?*/
	private static Map<String, HttpServletRequest> requests = new ConcurrentHashMap<String, HttpServletRequest>(
			50);

	/** 当前的响�?*/
	private static Map<String, HttpServletResponse> responses = new ConcurrentHashMap<String, HttpServletResponse>(
			50);

	public static final Logger logger = Logger.getLogger("com.pms");

	/** 随意的一个DAO */
	public static IBaseDAO anyDao;

	private static ServletContext context;

	/**
	 * 返回WEB容器
	 * 
	 * 2011-1-20
	 * @author yaoyuan 
	 * @return
	 */
	private static ServletContext getServletContext() {
		return context;
	}

	public static void putServletContext(ServletContext servletContext) {
		context = servletContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T) getSpringContext().getBean(beanName);
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
		return getSpringContext().getBeansOfType(clazz);
	}

	/**
	 * 当前会话 2008-10-20
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static Map<String, HttpSession> getSessions() {
		// TomcatWrapperServlet.fillSessions(sessions);
		return sessions;
	}

	/**
	 * 当前请求，注意在另起的线程中可能取不到request，要事先将其取出再起线程 2008-10-20
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest req = getRequests().get(
				String.valueOf(Thread.currentThread().hashCode()));
		if (req == null) {
			return req = (HttpServletRequest) ActionContext.getContext()
					.getContextMap().get(StrutsStatics.HTTP_REQUEST);
		}
		return req;
	}

	/**
	 * 当前回应 2008-10-20
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		HttpServletResponse res = getResponses().get(
				String.valueOf(Thread.currentThread().hashCode()));
		if (res == null) {
			return (HttpServletResponse) ActionContext.getContext()
					.getContextMap().get(StrutsStatics.HTTP_RESPONSE);
		}
		return res;
	}

	/**
	 * 取得session 2008-8-5
	 * 
	 * @author yaoyuan
	 * @param autoCreate
	 *            session为空时是否自动创�?
	 * @return
	 */
	public static HttpSession getSession(boolean autoCreate) {
		HttpSession session = getRequest().getSession(autoCreate);
		if (!autoCreate && session == null) {
			throw new BusinessException("对不起，您未登陆或已超时");
		}
		return session;
	}

	/**
	 * 缓存KEY 2008-10-20
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static List<String> getCacheKeys() {
		return cacheKeys;
	}

	/**
	 * 取得容器 2008-10-20
	 * 
	 * @author yaoyuan
	 * @return
	 */
	private static ApplicationContext getSpringContext() {
		return ContextLoader.getCurrentWebApplicationContext();
	}

	public static Logger getLogger() {
		return logger;
	}

	/**
	 * 这里可以取得当前�?��的请�?2008-11-30
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static Map<String, HttpServletRequest> getRequests() {
		return requests;
	}

	public static void saveMessage(String msg) {
		getRequest().setAttribute("msg", msg);
	}

	/**
	 * @return the responses
	 */
	public static Map<String, HttpServletResponse> getResponses() {
		return responses;
	}

	/**
	 * 当前线程的HASHCODE 2009-1-16
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static String getCurrentThreadHashCode() {
		return String.valueOf(Thread.currentThread().hashCode());
	}

	/**
	 * 取得当前主机的域�?2009-3-3
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static String getServerName() {
		return getRequest().getServerName();
	}

	/**
	 * 增加�?��SESSION 2009-3-19
	 * 
	 * @author yaoyuan
	 * @param arg0
	 */
	public static void addSession(HttpSessionEvent arg0) {
		SysContext.getSessions().put(arg0.getSession().getId(),
				arg0.getSession());
	}

	/**
	 * 减少�?��SESSION 2009-3-19
	 * 
	 * @author yaoyuan
	 * @param arg0
	 */
	public static void subtractSession(HttpSessionEvent arg0) {
		SysContext.getSessions().remove(arg0.getSession().getId());
	}

	/**
	 * 取应用路�?/futureStore Mar 27, 2009
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static String getContextPath() {
		return context.getContextPath();
	}

	/**
	 * 系统名称 2009-4-9
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static String getSystemName() {
		return PropertyUtils.getProperty("移动商城");
	}

	/**
	 * 工程名称 2009-4-9
	 * 
	 * @author yaoyuan
	 * @return
	 */
	public static String getProjectName() {
		return PropertyUtils.getProperty("screen");
	}

	/**
	 * 基于网站根目录的目录，如在根目录下的temp目录建立�?��名为abc.txt文件，那传入参数�?temp\\abc.txt" 完整代码示例：File
	 * file = new File(createFilePath("temp\\abc.txt"));
	 * 注意代码运行在linux和unix上时目录分隔符的差异 2009-5-13
	 * 
	 * @author yaoyuan
	 * @param filePath
	 * @return
	 */
	public static String createFilePath(String filePath) {
		return getServletContext().getRealPath(filePath);
	}
	
	/**
	 * 是否访问前台
	 * 2011-1-12
	 * @author yaoyuan
	 * @return
	 */
	public static boolean isAccessWap(){
		String servletPath = getRequest().getServletPath();
		if(servletPath!=null && servletPath.startsWith("/admin/")||isAccessMerchant()){
			return false;
		}
		return true;
	}
	
	/**
	 * isAccessWap
	 * <p>方法说明:是否访问商户系统</p>
	 * @2011-7-27
	 * 覃江
	 */
	public static boolean isAccessMerchant(){
		String servletPath = getRequest().getServletPath();
		if(servletPath!=null && servletPath.startsWith("/merchant/")){
			return true;
		}
		return false;
	}
	/**
	 * 是否wap1访问
	 * 2011-1-12
	 * @author yaoyuan
	 * @return
	 */
	public static boolean isAccessWap1(){
		HttpServletRequest request = SysContext.getRequest();
		String[] version = request.getServletPath().split("/");
		if(version[1].equals("wap1")){
			return true;
		}
		return false;
	}

	
	/**
	 * 从缓存中得到合作厂商账户
	 * @author wangdesheng
	 * @date 2012-8-31
	 * @return
	 */
/*	public static FirmAccount getFirmAccount(){
		FirmAccount firm=(FirmAccount)MemCachedUtil.getSessionAttribute(Constants.FIRM_LOGIN_KEY);
		 if(null==firm){
			 logger.warn("************usertype从cookie里获�?*************");
			 String userType = HttpUtil.getCookieValue(Constants.PMS_USER_TYPE, getRequest());
			 String userId = HttpUtil.getCookieValue(Constants.PMS_USER_ID, getRequest());
			 FirmAccount firm1 = new FirmAccount();
			 firm1.setPmUserType(userType);
			 if (userType.equals(Dictionary.MAN)) {
				 firm1.setId(userId);
				} else
				firm1.setFirmId(userId);
			 return firm1;
		 }
		return firm;
		
	}*/
	
	
	
	/**
	 * 得到服务器路�?
	 * 
	 * 2011-2-25
	 * @author yaoyuan
	 * @return
	 */
	public static String getBasePath() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath() + "/";
	}
	
	/**
	 * 得到服务器路�?不包含端口号和ContextPath)
	 * 
	 * 2013-5-13
	 * @author liming
	 * @return
	 */
	public static String getBasePaths() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request.getScheme() + "://" + request.getServerName();
	}

	/**
	 * 
	 * 获得当前web容器的绝对路�?以后如将上传路径 upload目录 移至web容器路径以外,该方法直接返回空字符串即�?
	 * 2011-2-25
	 * @author yaoyuan
	 * @return
	 */
	public static String getBaseServletRealPath() {
		return ServletActionContext.getServletContext().getRealPath("/");
	}
	
	public static User getUser() {
		User user = new User();
		String uuid = getUuid();
		if(StringUtil.isEmpty(uuid)) {
			return user;
		}
		Map<String, String> map = new HashMap<String,String>();
		try {
			map = getUserMap();
		} catch (Exception e) {
			e.printStackTrace();
			return user;
		}
		
		if(null!=map){
			String userId = map.get("userId");
			String primPrin = map.get("primPrin");
			String principal1 = map.get("principal1");
			String principal2 = map.get("principal2");
			String principal3 = map.get("principal3");
			user.setId(userId);
			user.setPrimPrin(primPrin);
			user.setPrincipal1(principal1);
			user.setPrincipal2(principal2);
			user.setPrincipal3(principal3);
		}
		return user;
		
	}
	
	public static Map<String,String> getUserMap() {
		Jedis jedis = RedisClient.getJedis();
		HashMap<String,String> map = new HashMap<String,String>();
		
		String userId = jedis.hget(getUuid(), "userId");
		if(StringUtil.isEmpty(userId)) {
			map.put(Constants.RETURN_CODE,"-1");
			return map;
		}
		String primPrin = jedis.hget(getUuid(), "primPrin");
		String principal1 = jedis.hget(getUuid(), "principal1");
		String principal2 = jedis.hget(getUuid(), "principal2");
		String principal3 = jedis.hget(getUuid(), "principal3");
		String credential = jedis.hget(getUuid(), "credential");
		
		map.put("userId", userId);
		map.put("primPrin", primPrin);
		map.put("principal1", principal1);
		map.put("principal2", principal2);
		map.put("principal3", principal3);
		map.put("credential", credential);
		
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		
		return map;
	}
	
	public static String getUuid() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Cookie[] cookies = request.getCookies();
		String uuid = "";
		if(cookies==null) {
			return "";
		}
		for(Cookie c:cookies) {
			if("sessionid".equals(c.getName())) {
				uuid = c.getValue();
			}
		}
		return uuid;
	}
	

}
