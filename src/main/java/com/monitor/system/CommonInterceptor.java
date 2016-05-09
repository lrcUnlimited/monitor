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
 * 拦截session过期 登录信息 权限控制 越权 字符注入
 * 
 * @Description TODO
 * @author clsu
 * @date 2013-7-25下午02:53:03
 * @CodeReviewer zwwang
 */
public class CommonInterceptor implements HandlerInterceptor {

	private String[] excludeUrls;

	private String loginUrl;
	private String processUrl;
	private String returnUrl;

	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse res, Object obj, Exception e) throws Exception {
		// 设置cookie
		res.addHeader("Set-Cookie", "uid=112; Path=/; HttpOnly");

		// 设置https的cookie
		res.addHeader("Set-Cookie", "uid=112; Path=/; Secure; HttpOnly");
	}

	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj, ModelAndView model) throws Exception {
	}

	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj) throws Exception {
		// 国际化配置
		req.getSession()
				.setAttribute("language", req.getLocale().getLanguage());

		String referer = req.getHeader("Referer");
		String contextPath = req.getContextPath();
		if (StringUtil.isNotEmpty(referer) && referer.indexOf(contextPath) < 0) {
			CodeException ce = new CodeException("存在跨站攻击的风险");
			throw ce;
		}
		// 安全过滤
		Enumeration<String> argsEn = req.getParameterNames();
		while (argsEn.hasMoreElements()) {
			String argV = req.getParameter(argsEn.nextElement());
			if (false == SecurityUtil.checkXSSValidity(argV)) {
				// CodeException ce = new CodeException("", "包含XSS字符");
				// throw ce;
			}
			if (false == SecurityUtil.checkSQLValidity(argV)) {
				CodeException ce = new CodeException("包含SQL注入字符");
				throw ce;
			}
		}

		boolean isOperation = false;
		String url = req.getRequestURI();
		String[] pathArr = url.split("\\.do")[0].split("/");
		String method = pathArr[pathArr.length - 1];
		if (method.startsWith("o_")) {
			// 操作权限
			isOperation = true;
		}
		String servletPath = req.getServletPath();
		// 不拦截的URL
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (exc.equals(servletPath)) {
					System.out.println("true");
					return true;
				}
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
