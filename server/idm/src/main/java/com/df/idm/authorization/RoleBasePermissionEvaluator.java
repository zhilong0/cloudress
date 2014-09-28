package com.df.idm.authorization;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.df.idm.model.Permission;
import com.df.idm.model.Role;

public class RoleBasePermissionEvaluator implements PermissionEvaluator {

	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		if (authorities != null) {
			for (GrantedAuthority authority : authorities) {
				if (authority instanceof Role) {
					Role role = (Role) authority;
					List<Permission> permissions = role.getPermissions();
					if (permissions != null) {
						for (Permission per : permissions) {
							if (permission instanceof Permission) {
								if (((Permission) permission).getName().equalsIgnoreCase(per.getName())) {
									return true;
								}
							} else if (permission instanceof String) {
								if (((String) permission).equalsIgnoreCase(per.getName())) {
									return true;
								}
							}
						}
					}
				} else if (authority instanceof Permission) {
					Permission per = (Permission) authority;
					if (permission instanceof Permission) {
						if (((Permission) permission).getName().equalsIgnoreCase(per.getName())) {
							return true;
						} else if (permission instanceof String) {
							if (((String) permission).equalsIgnoreCase(per.getName())) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return hasPermission(authentication, null, permission);
	}
}
