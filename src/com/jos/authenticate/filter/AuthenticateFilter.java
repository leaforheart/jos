package com.jos.authenticate.filter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inveno.util.PropertyUtils;
import com.inveno.util.StringUtil;
import com.jos.authenticate.service.AuthenticateServiceImpl;
import com.jos.common.util.Constants;
import com.jos.common.util.SysContext;

public class AuthenticateFilter implements Filter {
	
	private String[] checkUrl;
	private String mainUrl = PropertyUtils.getProperty(Constants.MAIN_URL);

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest paramServletRequest,ServletResponse paramServletResponse, FilterChain chain) throws IOException, ServletException {
		
		//尝试转化成http
		HttpServletRequest request = null;
	    HttpServletResponse response = null;
	    try {
	    	request = (HttpServletRequest) paramServletRequest;
	    	response = (HttpServletResponse) paramServletResponse;
	    } catch(Exception e) {
	    	chain.doFilter(paramServletRequest, paramServletResponse);
	    	return;
	    }
	    //检查是否是不需要检查url
	    boolean flag = isCheckUrl(request.getServletPath());
	    if(!flag) {
	    	chain.doFilter(paramServletRequest, paramServletResponse);
	    	return;
	    }
	    //查找回话cookie
	    Cookie[] cookies = request.getCookies();
	    String uuid = "";
	    for(Cookie cookie:cookies) {
	    	if(Constants.SESSIONID.equals(cookie.getName())) {
	    		uuid = cookie.getValue();
	    	}
	    }
	    if(StringUtil.isEmpty(uuid)) {
	    	response.sendRedirect(mainUrl);//不存在登录cookie
	    	return;
	    }
	    //后台验证
	    
	    HashMap<String,String> map = (HashMap<String, String>) SysContext.getUserMap();
	    String rCode = map.get(Constants.RETURN_CODE);
	    if(!Constants.SUCCESS_CODE.equals(rCode)) {
	    	response.sendRedirect(mainUrl);
	    	return;
	    }
		
		chain.doFilter(paramServletRequest, paramServletResponse);
	    
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String urls = config.getInitParameter("checkUrl");
		if (StringUtil.isNotEmpty(urls)) {
			urls = urls.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\t", "");
			checkUrl = urls.split(",");
		}

		if (checkUrl != null) {
			for (int i = 0; i < checkUrl.length; i++) {
				checkUrl[i] = checkUrl[i].trim();
			}
		}
	}
	
	private boolean isCheckUrl(String url) {
		if (null==url || "".equals(url)){
			return false;
		}

		for (int i = 0; i < checkUrl.length; i++) {
			if (url.equalsIgnoreCase(checkUrl[i]) || url.toLowerCase().startsWith(checkUrl[i].toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
