package com.tt.spec.locality.service.impl;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tt.spec.locality.model.Shop;
import com.tt.spec.locality.model.Speciality;
import com.tt.spec.locality.service.OperationPermissionEvaluator;

public class OperationPermissionEvaluatorImpl implements OperationPermissionEvaluator {

	private PermissionEvaluator permissionEvaluator;

	public static final String DOMAIN_NAME = "SPECIALITY";

	public static final String MASTER_DATA_IMPORT = "MASTER_DATA_IMPORT";

	public static final String MASTER_DATA_APPROVAL = "MASTER_DATA_APPROVAL";

	public static final String MASTER_DATA_EDIT = "MASTER_DATA_EDIT";

	public OperationPermissionEvaluatorImpl(PermissionEvaluator permissionEvaluator) {
		this.permissionEvaluator = permissionEvaluator;
	}

	@Override
	public boolean canAddSpeciality(String userCode) {
		Authentication authentication = this.getAuthentication(userCode);
		return permissionEvaluator.hasPermission(authentication, DOMAIN_NAME, MASTER_DATA_IMPORT);
	}

	@Override
	public boolean canApprove(String userCode) {
		Authentication authentication = this.getAuthentication(userCode);
		return permissionEvaluator.hasPermission(authentication, DOMAIN_NAME, MASTER_DATA_APPROVAL);
	}

	@Override
	public boolean canAddShop(String userCode) {
		Authentication authentication = this.getAuthentication(userCode);
		return permissionEvaluator.hasPermission(authentication, DOMAIN_NAME, MASTER_DATA_IMPORT);
	}

	protected Authentication getAuthentication(String userCode) {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			throw new IllegalStateException("no security context");
		}
		Authentication authentication = context.getAuthentication();
		if (!authentication.getName().equals(userCode)) {
			throw new IllegalStateException("current security context is " + authentication.getName());
		}
		return authentication;
	}

	@Override
	public boolean canEditSpeciality(String userCode, Speciality speciality) {
		Authentication authentication = this.getAuthentication(userCode);
		return permissionEvaluator.hasPermission(authentication, DOMAIN_NAME, MASTER_DATA_EDIT);
	}

	@Override
	public boolean canEditShop(String userCode, Shop shop) {
		Authentication authentication = this.getAuthentication(userCode);
		return permissionEvaluator.hasPermission(authentication, DOMAIN_NAME, MASTER_DATA_EDIT);
	}

}
