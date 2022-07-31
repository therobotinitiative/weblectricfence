package com.orbital3d.web.security.weblectricfence.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orbital3d.web.security.weblectricfence.exception.AuthorizationException;
import com.orbital3d.web.security.weblectricfence.type.Permission;
import com.orbital3d.web.security.weblectricfence.type.WebLectricSubject;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

@Component
public class AuthorizationWorkerImpl implements AuthorizationWorker {
	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationWorkerImpl.class);

	@Autowired(required = true)
	private AuthorizationWorker.Authorizer authorizer;

	@Override
	public void authorize(Permission permission) throws AuthorizationException {
		WebLectricSubject subject = WFUtil.getSubject();
		if (subject.getPermissions() == null) {
			LOG.info("Gathering permissions for {}", subject.getIdentity());
			authorizer.gatherPermissions(subject);
		}
		authorizer.authorize(subject, permission);
	}

	@Override
	public void unauthorize(WebLectricSubject subject) {
		subject.setPermissions(null);
	}

}
