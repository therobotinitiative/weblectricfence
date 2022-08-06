package com.orbital3d.web.security.weblectricfence.configuration.impl;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.orbital3d.web.security.weblectricfence.configuration.FenceConfig;
import com.orbital3d.web.security.weblectricfence.configuration.InternalConfig;

@Component
public class InternalConfigImpl implements InternalConfig {
	@Autowired
	private FenceConfig weConfig;

	private String secureContextRoot = null;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	private static final String pathEnd = "**/**";

	@PostConstruct
	protected void prepareConfiguration() {
		// Modify context root path to ANT style path
		String configuredContextRoot = weConfig.secureContextRoot();
		if (StringUtils.isBlank(configuredContextRoot)) {
			throw new IllegalArgumentException("Secure mst not be blank");
		}
		if (!configuredContextRoot.startsWith(AntPathMatcher.DEFAULT_PATH_SEPARATOR)) {
			configuredContextRoot = AntPathMatcher.DEFAULT_PATH_SEPARATOR + configuredContextRoot;
		}
		if (!configuredContextRoot.endsWith(AntPathMatcher.DEFAULT_PATH_SEPARATOR)
				&& !configuredContextRoot.endsWith(InternalConfigImpl.pathEnd)) {
			configuredContextRoot += AntPathMatcher.DEFAULT_PATH_SEPARATOR;
		}
		if (!configuredContextRoot.endsWith(InternalConfigImpl.pathEnd)) {
			configuredContextRoot += InternalConfigImpl.pathEnd;
		}
		if (!Pattern.matches("\\/[a-z0-9/]+\\/\\*\\*\\/\\*\\*", configuredContextRoot)) {
			throw new BeanInitializationException("wrong format");
		}
		if (!pathMatcher.isPattern(configuredContextRoot)) {
			throw new BeanInitializationException("Secure context root MUST be ANT style path");
		}
		secureContextRoot = configuredContextRoot;
	}

	@Override
	public String secureContextRoot() {
		return secureContextRoot;
	}

}
