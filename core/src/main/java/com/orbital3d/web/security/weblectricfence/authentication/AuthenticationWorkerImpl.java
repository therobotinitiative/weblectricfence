package com.orbital3d.web.security.weblectricfence.authentication;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.exception.UnsupportedAuthenticationTokenException;
import com.orbital3d.web.security.weblectricfence.type.Subject;
import com.orbital3d.web.security.weblectricfence.type.UserIdentity;
import com.orbital3d.web.security.weblectricfence.util.HashUtil;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

@Component
public class AuthenticationWorkerImpl implements AuthenticationWorker {
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationWorkerImpl.class);

	@Autowired(required = true)
	List<Authenticator> authenticators;

	public void authenticate(AuthenticationToken token) throws AuthenticationException, LoginException {
		// Find first matching Authenticator or throw exception if not found
		Authenticator authenticator = null;
		try {
			authenticator = authenticators.stream().filter(auth -> auth.supports(token)).collect(Collectors.toList())
					.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new UnsupportedAuthenticationTokenException();
		}

		UserIdentity identity = authenticator.authenticate(token);

		if (identity == null) {
			throw new LoginException("Invalid UserIdentity");
		}

		// Generate tokens
		try {
			String authenticationToken = Base64.getEncoder().encodeToString(HashUtil.generateShortToken());
			String refreshToken = Base64.getEncoder().encodeToString(HashUtil.generateToken());
			WFUtil.setSubject(Subject.of(identity, null, authenticationToken, refreshToken));
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Missing suitable algorithm", e);
		}
	}
}
