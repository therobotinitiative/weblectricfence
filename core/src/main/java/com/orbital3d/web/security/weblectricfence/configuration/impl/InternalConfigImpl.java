package com.orbital3d.web.security.weblectricfence.configuration.impl;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.orbital3d.web.security.weblectricfence.configuration.InternalConfig;
import com.orbital3d.web.security.weblectricfence.configuration.WEConfig;

@Component
public class InternalConfigImpl implements InternalConfig {
	@Autowired
	private WEConfig weConfig;

	private String secureContextRoot = null;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	@PostConstruct
	protected void prepareConfiguration() {
		// Modify context root path to ANT style path
		String configuredContextRoot = weConfig.secureContextRoot();
		if (StringUtils.isBlank(configuredContextRoot)) {
			throw new IllegalArgumentException("Secure mst not be blank");
		}
		if (!configuredContextRoot.startsWith("/")) {
			configuredContextRoot = "/" + configuredContextRoot;
		}
		if (!configuredContextRoot.endsWith("/") && !configuredContextRoot.endsWith("**/**")) {
			configuredContextRoot += "/";
		}
		if (!configuredContextRoot.endsWith("**/**")) {
			configuredContextRoot += "**/**";
		}
		if (!Pattern.matches("\\/[a-z0-9/]+\\/\\*\\*\\/\\*\\*", configuredContextRoot)) {
			throw new IllegalArgumentException("wrong format");
		}
		if (!pathMatcher.isPattern(configuredContextRoot)) {
			throw new RuntimeException("Secure context root MUST be ANT style path");
		}
		secureContextRoot = configuredContextRoot;
	}

	@Override
	public String secureContextRoot() {
		return secureContextRoot;
	}

}
