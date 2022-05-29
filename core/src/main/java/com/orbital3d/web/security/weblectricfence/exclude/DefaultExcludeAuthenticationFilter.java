package com.orbital3d.web.security.weblectricfence.exclude;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Default implementation of {@link ExcludeAuthenticationFilter}. Default
 * implementation supports ANT style paths.
 * 
 * @since 0.1
 * @author msiren
 *
 */
public class DefaultExcludeAuthenticationFilter implements ExcludeAuthenticationFilter
{
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	private Map<String, Set<RequestMethod>> excludedPaths = new HashMap<>();

	@Override
	public boolean isExcluded(String path)
	{
		return isExcluded(path, null);
	}

	@Override
	public boolean isExcluded(String path, RequestMethod requestMethod)
	{
		Optional<String> foundPath = excludedPaths.keySet().stream().filter(excludedPath -> antPathMatcher.match(excludedPath, path)).findFirst();
		// URI matches, check the request method if defined
		if (foundPath.isPresent())
		{
			// If the path is present, but the request method is ignored ie. null
			if (requestMethod == null)
			{
				return true;
			}
			Set<RequestMethod> pathRequestMethod = excludedPaths.get(path);
			// Check if the request method matches if it is not null
			if (pathRequestMethod != null)
			{
				return pathRequestMethod.contains(requestMethod) || pathRequestMethod.contains(null);
			}
			return true;
		}
		return false;
	}

	@Override
	public void addExcluded(String path, RequestMethod requestMethod)
	{
		if (StringUtils.isBlank(path))
		{
			throw new IllegalArgumentException("URI must be ANT style path");
		}
		// Map the path if it is not yet mapped
		if (!excludedPaths.containsKey(path))
		{
			excludedPaths.put(path, Stream.of(requestMethod).collect(Collectors.toCollection(HashSet::new)));
			return;
		}
		// Check validity
		Set<RequestMethod> methods = excludedPaths.get(path);
		if ((requestMethod == null && !methods.isEmpty()) || (methods.contains(null) && requestMethod != null) || methods.contains(requestMethod))
		{
			throw new IllegalArgumentException("Path already excluded");
		}
		methods.add(requestMethod);
	}

	@Override
	public void addExcluded(Map<String, RequestMethod> excludedPaths)
	{
		excludedPaths.forEach((path, requestMethod) -> {
			addExcluded(path, requestMethod);
		});
	}

}
