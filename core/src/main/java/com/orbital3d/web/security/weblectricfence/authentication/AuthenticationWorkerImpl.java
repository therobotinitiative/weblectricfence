package com.orbital3d.web.security.weblectricfence.authentication;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.type.Subject;
import com.orbital3d.web.security.weblectricfence.type.UserIdentity;
import com.orbital3d.web.security.weblectricfence.util.HashUtil;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

@Component
public class AuthenticationWorkerImpl implements AuthenticationWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationWorkerImpl.class);

	@Autowired(required = true)
	private Authenticator authenticator;

	public void authenticate(String userName, String password) throws AuthenticationException
	{
		// Authenticate
		UserIdentity identity = authenticator.authenticate(userName, password);

		// Generate tokens
		try
		{
			String authenticationToken = Base64.getEncoder().encodeToString(HashUtil.generateShortToken());
			String refreshToken = Base64.getEncoder().encodeToString(HashUtil.generateToken());
			WFUtil.setSubject(Subject.of(identity, null, authenticationToken, refreshToken));
		}
		catch (NoSuchAlgorithmException e)
		{
			LOG.error("Missing suitable algorithm", e);
		}
	}
}
