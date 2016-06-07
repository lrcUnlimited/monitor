package com.monitor.system;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.monitor.exception.CodeException;
import com.monitor.util.SecurityUtil;
import com.monitor.util.StringUtil;

/**
 * 
 * @author li
 * 
 */
public class CommonInterceptor implements HandlerInterceptor {

	private String[] excludeUrls;

	private String loginUrl;
	private String processUrl;
	private String returnUrl;

	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse res, Object obj, Exception e) throws Exception {
	}

	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj, ModelAndView model) throws Exception {
	}

	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj) throws Exception {

		String referer = req.getHeader("Referer");
		String contextPath = req.getContextPath();
		if (StringUtil.isNotEmpty(referer) && referer.indexOf(contextPath) < 0) {
			CodeException ce = new CodeException("存在跨站攻击的风险");
			throw ce;
		}
		System.out.println(obj);
		// 安全过滤
		Enumeration<String> argsEn = req.getParameterNames();
		while (argsEn.hasMoreElements()) {
			String argV = req.getParameter(argsEn.nextElement());
			System.out.println(argV);
			if (false == SecurityUtil.checkXSSValidity(argV)) {
				// CodeException ce = new CodeException("", "包含XSS字符");
				// throw ce;
			}
			if (false == SecurityUtil.checkSQLValidity(argV)) {
				CodeException ce = new CodeException("包含SQL注入字符");
				throw ce;
			}
		}

		return true;
	}

	public String[] getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getProcessUrl() {
		return processUrl;
	}

	public void setProcessUrl(String processUrl) {
		this.processUrl = processUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
