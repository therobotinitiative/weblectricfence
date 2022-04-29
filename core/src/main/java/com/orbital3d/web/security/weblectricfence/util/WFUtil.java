package com.orbital3d.web.security.weblectricfence.util;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationWorker;
import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationWorkerImpl;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationWorker;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationWorkerImpl;
import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.type.WebLectricSubject;

public class WFUtil
{
	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext applicationContext)
	{
		WFUtil.applicationContext = applicationContext;
	}

	private static final String SUBJECT_KEY = "subject-key";
	private static final String AUTHENTICATION_TOKEN_KEY = "authentication-token-key";
	private static final String REFRESH_TOKEN_KEY = "refresh-token-key";

	private WFUtil()
	{
		// Nothing
	}

	public static HttpSession getSession()
	{
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		return session;
	}

	public static void setSubject(WebLectricSubject subject)
	{
		if (subject == null)
		{
			throw new IllegalArgumentException("Subject must be set");
		}
		if (StringUtils.isAllBlank(subject.getAuthenticationToken()))
		{
			throw new IllegalArgumentException("Authentication token must be set");
		}
		WFUtil.getSession().setAttribute(AUTHENTICATION_TOKEN_KEY, subject.getAuthenticationToken());
		WFUtil.getSession().setAttribute(REFRESH_TOKEN_KEY, subject.getRefreshToken());
		WFUtil.getSession().setAttribute(SUBJECT_KEY, subject);
	}

	public static WebLectricSubject getSubject()
	{
		return (WebLectricSubject) WFUtil.getSession().getAttribute(SUBJECT_KEY);
	}

	public static void login(String userName, String password) throws AuthenticationException
	{
		AuthenticationWorker authWorker = applicationContext.getAutowireCapableBeanFactory().getBean(AuthenticationWorkerImpl.class);
		authWorker.authenticate(userName, password);
	}

	public static void logout()
	{
		try
		{
			WebLectricSubject subject = (WebLectricSubject) WFUtil.getSession().getAttribute(SUBJECT_KEY);
			AuthorizationWorker authWorker = applicationContext.getAutowireCapableBeanFactory().getBean(AuthorizationWorkerImpl.class);
			if (subject != null)
			{
				authWorker.unauthorize(subject);
			}
			getSession().setAttribute(SUBJECT_KEY, null);
			getSession().invalidate();
		}
		catch (IllegalStateException e)
		{
			// Fail silently
		}
	}

	public static boolean isAuthenticated()
	{
		WebLectricSubject subject = (WebLectricSubject) WFUtil.getSession().getAttribute(SUBJECT_KEY);
		if (subject != null)
		{
			String authenticationToken = subject.getAuthenticationToken();
			if (authenticationToken != null)
			{
				String sessionAuthenticationToken = (String) WFUtil.getSession().getAttribute(AUTHENTICATION_TOKEN_KEY);
				if (sessionAuthenticationToken != null)
				{
					return sessionAuthenticationToken.equals(authenticationToken);
				}
			}
		}
		return false;
	}
}