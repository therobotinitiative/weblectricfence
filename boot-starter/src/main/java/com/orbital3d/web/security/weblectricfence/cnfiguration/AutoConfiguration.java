package com.orbital3d.web.security.weblectricfence.cnfiguration;

import java.lang.reflect.Method;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextListener;

import com.orbital3d.web.security.weblectricfence.annotation.RequiresPermission;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationMatcher;
import com.orbital3d.web.security.weblectricfence.authorization.DefaultAuthorizationMatcher;
import com.orbital3d.web.security.weblectricfence.exclude.DefaultExcludeAuthenticationFilter;
import com.orbital3d.web.security.weblectricfence.exclude.ExcludeAuthenticationFilter;
import com.orbital3d.web.security.weblectricfence.type.Permission;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

/**
 * Auto configuration for WebLectric Fence.
 * 
 * @since 0.1
 * @author msiren
 *
 */
@Configuration
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class))
public class AutoConfiguration
{
	private static final Logger LOG = LoggerFactory.getLogger(AutoConfiguration.class);

	/**
	 * Default excluded paths.
	 * 
	 * @return Default {@link ExcludeAuthenticationFilter}
	 */
	@Bean
	@ConditionalOnMissingBean(name = "excludeFilter")
	public ExcludeAuthenticationFilter excludeFilter()
	{
		ExcludeAuthenticationFilter excludeAuthentication = new DefaultExcludeAuthenticationFilter();

		excludeAuthentication.addExcluded("/login**");
		excludeAuthentication.addExcluded("/logout");
		excludeAuthentication.addExcluded("/");

		return excludeAuthentication;
	}

	// Only if AliasFor annotation worked we would not need this method.
	private Pair<String[], RequestMethod> findPaths(Method m)
	{
		GetMapping gem = AnnotationUtils.findAnnotation(m, GetMapping.class);
		if (gem != null)
		{
			return Pair.of(gem.path(), RequestMethod.GET);
		}
		PostMapping pom = AnnotationUtils.findAnnotation(m, PostMapping.class);
		if (pom != null)
		{
			return Pair.of(pom.path(), RequestMethod.POST);
		}
		PutMapping pum = AnnotationUtils.findAnnotation(m, PutMapping.class);
		if (pum != null)
		{
			return Pair.of(pum.path(), RequestMethod.PUT);
		}
		DeleteMapping dem = AnnotationUtils.findAnnotation(m, DeleteMapping.class);
		if (dem != null)
		{
			return Pair.of(dem.path(), RequestMethod.DELETE);
		}
		PatchMapping pam = AnnotationUtils.findAnnotation(m, PatchMapping.class);
		if (pam != null)
		{
			return Pair.of(pam.path(), RequestMethod.PATCH);
		}
		return null;
	}

	/**
	 * Scan for all classes annotated with {@link Controller} and scan all their
	 * declared methods. If the method is annotated with {@link RequiresPermission}
	 * the associated path or paths are collected with the required permission into
	 * collection containing all path(s) requiring a permission.
	 * 
	 * @param applicationContext Application context
	 * @return Default {@link AuthorizationMatcher}
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthorizationMatcher authorizationMatcher(ApplicationContext applicationContext)
	{
		AuthorizationMatcher authorizationMatcher = new DefaultAuthorizationMatcher();
		WFUtil.setApplicationContext(applicationContext);

		LOG.trace("Scanning end point for required permission");
		if (applicationContext != null)
		{
			for (String beanName : applicationContext.getBeanNamesForAnnotation(Controller.class))
			{
				for (Method method : applicationContext.getAutowireCapableBeanFactory().getType(beanName).getDeclaredMethods())
				{
					RequiresPermission requiresPermission = method.getAnnotation(RequiresPermission.class);
					if (requiresPermission != null)
					{
						Pair<String[], RequestMethod> paths = findPaths(method);
						if (paths != null)
						{
							// Paths are returned as an array, this simply flattens them out
							for (String path : paths.getLeft())
							{
								LOG.trace("Adding path with required permission: ({}) [{}]({})", requiresPermission.value(), paths.getRight(), path);
								authorizationMatcher.append(AuthorizationMatcher.EndPointContainer.of(path, paths.getRight(), Permission.of(requiresPermission.value())));
							}
						}
					}
				}
			}
		}

		return authorizationMatcher;
	}

	/**
	 * Defining this bean makes it possible to access session from static method for
	 * example.
	 * 
	 * @return {@link RequestContextListener} instance
	 */
	@Bean
	@ConditionalOnMissingBean
	public RequestContextListener requestContextListener()
	{
		return new RequestContextListener();
	}
}
