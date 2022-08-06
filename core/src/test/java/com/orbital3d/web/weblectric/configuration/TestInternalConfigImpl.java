package com.orbital3d.web.weblectric.configuration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanInitializationException;

import com.orbital3d.web.security.weblectricfence.configuration.FenceConfig;
import com.orbital3d.web.security.weblectricfence.configuration.impl.InternalConfigImpl;

class TestInternalConfigImpl {
	private InternalConfigImpl i;
	private FenceConfig c = mock(FenceConfig.class);

	@BeforeEach
	void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		i = new InternalConfigImpl();
		Field f = InternalConfigImpl.class.getDeclaredField("weConfig");
		f.setAccessible(true);
		f.set(i, c);
	}

	@Test
	void testValidPaths() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		String[] paths = new String[] { "/path", "/path/", "/ant/**/**", "/multiple/parts/to/secure",
				"/multiple/parts/to/secure/", "/multiple/parts/to/secure/**/**", "path/", "path",
				"path/can/have/multiple/parts", "/path/can/have/multiple/parts", "/path/can/have/multiple/parts/",
				"path/can/have/multiple/parts/", "/path/can/have/multiple/parts/**/**" };
		for (String path : paths) {
			when(c.secureContextRoot()).thenReturn(path);
			Method m = InternalConfigImpl.class.getDeclaredMethod("prepareConfiguration");
			m.setAccessible(true);
			m.invoke(i);
		}
	}

	@Test
	void testInvqlidParams() {
		String[] paths = new String[] { "{}", "@&%", "/{}", "/@&%", "/{}/", "/@&%/" };
		for (String path : paths) {
			when(c.secureContextRoot()).thenReturn(path);
			assertThrows(BeanInitializationException.class, () -> {
				Method m = InternalConfigImpl.class.getDeclaredMethod("prepareConfiguration");
				m.setAccessible(true);
				try {
					m.invoke(i);
				} catch (InvocationTargetException e) {
					throw e.getCause();
				}
			});
		}
	}

	@Test
	void testBlankParams() {
		String[] paths = new String[] { "", " ", null };
		for (String path : paths) {
			when(c.secureContextRoot()).thenReturn(path);
			assertThrows(IllegalArgumentException.class, () -> {
				Method m = InternalConfigImpl.class.getDeclaredMethod("prepareConfiguration");
				m.setAccessible(true);
				try {
					m.invoke(i);
				} catch (InvocationTargetException e) {
					throw e.getCause();
				}
			});
		}
	}
}
