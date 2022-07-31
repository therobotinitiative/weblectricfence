package com.orbital3d.web.weblectric.exclude;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orbital3d.web.security.weblectricfence.exclude.DefaultExcludeAuthenticationFilter;
import com.orbital3d.web.security.weblectricfence.exclude.ExcludeAuthenticationFilter;

public class TestDefaultExcludeFilter {
	private ExcludeAuthenticationFilter excludeFilter = null;

	@BeforeEach
	public void initTest() {
		excludeFilter = new DefaultExcludeAuthenticationFilter();
	}

	@Test
	public void testAddValidURI() {
		excludeFilter.addExcluded("/login", null);
		excludeFilter.addExcluded("/login*", null);
		excludeFilter.addExcluded("/login**", null);
		excludeFilter.addExcluded("/login/**", null);
		excludeFilter.addExcluded("/uri/{parameter1}", null);
		excludeFilter.addExcluded("/uri/param_value", null);
	}

	@Test
	public void testInvalidURI() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded(null, null);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded("", null);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded(" ", null);
		});
	}

	@Test
	public void testIgnoreRM1() {
		excludeFilter.addExcluded("/uri", RequestMethod.DELETE);
		Assertions.assertTrue(excludeFilter.isExcluded("/uri"));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.DELETE));
		Assertions.assertFalse(excludeFilter.isExcluded("/uri", RequestMethod.GET));
		Assertions.assertFalse(excludeFilter.isExcluded("/uri", RequestMethod.HEAD));
		Assertions.assertFalse(excludeFilter.isExcluded("/uri", RequestMethod.OPTIONS));
		Assertions.assertFalse(excludeFilter.isExcluded("/uri", RequestMethod.PATCH));
		Assertions.assertFalse(excludeFilter.isExcluded("/uri", RequestMethod.POST));
		Assertions.assertFalse(excludeFilter.isExcluded("/uri", RequestMethod.PUT));
		Assertions.assertFalse(excludeFilter.isExcluded("/uri", RequestMethod.TRACE));
	}

	@Test
	public void testIgnoreRM2() {
		excludeFilter.addExcluded("/uri", null);
		Assertions.assertTrue(excludeFilter.isExcluded("/uri"));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.DELETE));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.GET));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.HEAD));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.OPTIONS));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.PATCH));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.POST));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.PUT));
		Assertions.assertTrue(excludeFilter.isExcluded("/uri", RequestMethod.TRACE));
	}

	@Test
	public void testPathAlreadyExcluded1() {
		excludeFilter.addExcluded("/path", null);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded("/path", RequestMethod.GET);
		});
	}

	@Test
	public void testPathAlreadyExcluded2() {
		excludeFilter.addExcluded("/path", RequestMethod.GET);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded("/path", null);
		});
	}

	@Test
	public void testExcludeDifferentRM() {
		excludeFilter.addExcluded("/path", RequestMethod.DELETE);
		excludeFilter.addExcluded("/path", RequestMethod.GET);
		excludeFilter.addExcluded("/path", RequestMethod.HEAD);
		excludeFilter.addExcluded("/path", RequestMethod.OPTIONS);
		excludeFilter.addExcluded("/path", RequestMethod.PATCH);
		excludeFilter.addExcluded("/path", RequestMethod.POST);
		excludeFilter.addExcluded("/path", RequestMethod.PUT);
		excludeFilter.addExcluded("/path", RequestMethod.TRACE);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded("/path", RequestMethod.PATCH);
		});
	}

	@Test
	public void testAddMapOk() {
		Map<String, RequestMethod> e = new HashMap<>();
		e.put("/path1", RequestMethod.GET);
		e.put("/path1", RequestMethod.POST);
		e.put("/path2", RequestMethod.GET);
		e.put("/path2/{param}", RequestMethod.GET);
		excludeFilter.addExcluded(e);
	}

	@Test
	public void testAddMapFaulty1() {
		Map<String, RequestMethod> excludeMap = new HashMap<>();
		excludeMap.put("/path1", RequestMethod.GET);
		excludeMap.put("/path2", RequestMethod.GET);
		excludeMap.put("/path2/{param}", RequestMethod.GET);
		excludeFilter.addExcluded(excludeMap);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded(excludeMap);
		});
	}

	@Test
	public void testAddMapFaulty2() {
		Map<String, RequestMethod> excludeMap = new HashMap<>();
		excludeMap.put("/path1", RequestMethod.GET);
		excludeMap.put("/path2", RequestMethod.GET);
		excludeMap.put("/path2/{param}", RequestMethod.GET);
		excludeFilter.addExcluded(excludeMap);
		excludeMap.put("/path1", null);
		excludeMap.put("/path2", RequestMethod.GET);
		excludeMap.put("/path2/{param}", RequestMethod.GET);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded(excludeMap);
		});
	}

	@Test
	public void testAddMapFaulty3() {
		Map<String, RequestMethod> excludeMap = new HashMap<>();
		excludeMap.put("/path1", null);
		excludeMap.put("/path2", RequestMethod.GET);
		excludeMap.put("/path2/{param}", RequestMethod.GET);
		excludeFilter.addExcluded(excludeMap);
		excludeMap.put("/path1", RequestMethod.GET);
		excludeMap.put("/path2", RequestMethod.GET);
		excludeMap.put("/path2/{param}", RequestMethod.GET);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded(excludeMap);
		});
	}

	@Test
	public void testAddMapFaulty4() {
		Map<String, RequestMethod> excludeMap = new HashMap<>();
		excludeMap.put("/path1", RequestMethod.GET);
		excludeMap.put("/path2", RequestMethod.GET);
		excludeMap.put("/path2/{param}", RequestMethod.GET);
		excludeFilter.addExcluded(excludeMap);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded("/path2", null);
		});
	}

	@Test
	public void testAddMapFaulty5() {
		Map<String, RequestMethod> excludeMap = new HashMap<>();
		excludeMap.put("/path1", RequestMethod.GET);
		excludeMap.put("/path2", RequestMethod.GET);
		excludeMap.put("/path2/{param}", RequestMethod.GET);
		excludeFilter.addExcluded(excludeMap);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			excludeFilter.addExcluded("/path2", RequestMethod.GET);
		});
	}
}
