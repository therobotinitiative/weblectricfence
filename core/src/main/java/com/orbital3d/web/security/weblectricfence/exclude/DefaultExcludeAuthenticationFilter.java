package com.orbital3d.web.security.weblectricfence.exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Default implementation of {@link ExcludeAuthenticationFilter}. Default implementation
 * supports ANT style paths.
 * 
 * @since 0.1
 * @author msiren
 *
 */
public class DefaultExcludeAuthenticationFilter implements ExcludeAuthenticationFilter
{
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	private Map<String, RequestMethod> excludedPaths = new HashMap<>();

	@Override
	public boolean isExcluded(String URI)
	{
		return isExcluded(URI, null);
	}

	@Override
	public boolean isExcluded(String URI, RequestMethod requestMethod)
	{
		Optional<String> foundURI = excludedPaths.keySet().stream().filter(path -> antPathMatcher.match(path, URI)).findFirst();
		// URI matches, check the request method if defined
		if (foundURI.isPresent())
		{
			RequestMethod pathRequestMethod = excludedPaths.get(URI);
			// Check if the request method matches if it is not null
			if (pathRequestMethod != null)
			{
				return pathRequestMethod.equals(requestMethod);
			}
			return true;
		}
		return false;
	}

	@Override
	public void addExcluded(String URI)
	{
		addExcluded(URI, null);
	}

	@Override
	public void addExcluded(String URI, RequestMethod requestMethod)
	{
		excludedPaths.put(URI, requestMethod);
	}

	@Override
	public void addExcluded(Map<String, RequestMethod> excludedPaths)
	{
		this.excludedPaths.putAll(excludedPaths);
	}

}
