package com.orbital3d.web.security.weblectricfence.exclude;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

public interface ExcludeAuthenticationFilter
{
	void addExcluded(String URI);

	void addExcluded(String URI, RequestMethod requestMethod);

	void addExcluded(Map<String, RequestMethod> excludedPaths);

	boolean isExcluded(String URI);

	boolean isExcluded(String URI, RequestMethod method);
}
