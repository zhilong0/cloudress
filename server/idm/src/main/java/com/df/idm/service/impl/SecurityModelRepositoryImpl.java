package com.df.idm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.df.idm.common.validation.exception.ValidationException;
import com.df.idm.dao.PermissionDao;
import com.df.idm.dao.RoleDao;
import com.df.idm.exception.PermissionAlreadyExistException;
import com.df.idm.exception.RoleException;
import com.df.idm.model.Permission;
import com.df.idm.model.Role;
import com.df.idm.service.contract.SecurityModelRepository;
import com.mongodb.DuplicateKeyException;

public class SecurityModelRepositoryImpl implements SecurityModelRepository {

	private RoleDao roleDao;

	private PermissionDao permissionDao;

	private Validator validator;

	private static final Logger logger = LoggerFactory.getLogger(SecurityModelRepository.class);

	public SecurityModelRepositoryImpl(Validator validator, RoleDao roleDao, PermissionDao permissionDao) {
		this.roleDao = roleDao;
		this.validator = validator;
		this.permissionDao = permissionDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public void setPermissionDao(PermissionDao permissionDao) {
		this.permissionDao = permissionDao;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@Override
	public Role addRole(Role role) {
		List<Permission> permissions = role.getPermissions();
		permissions = permissionDao.getPermissionList(permissions);
		role.setPermissions(permissions);
		Role found = roleDao.findRole(role.getName());
		if (found == null) {
			try {
				roleDao.insertRole(role);
				return role;
			} catch (DuplicateKeyException ex) {
				logger.error("failed to add role", ex);
				throw RoleException.roleWithNameAlreadyExist(role.getName());
			}
		} else {
			throw RoleException.roleWithNameAlreadyExist(role.getName());
		}
	}

	@Override
	public boolean updateRole(Role role) {
		List<Permission> permissions = role.getPermissions();
		Role found = this.getRole(role.getName());
		if (found == null) {
			return false;
		}

		permissions = permissionDao.getPermissionList(permissions);
		found.setPermissions(permissions);
		found.setDescription(role.getDescription());
		ArrayList<Role> roles = new ArrayList<Role>();
		roles.add(found);
		return roleDao.bulkUpdate(roles) == 1;
	}

	@Override
	public Permission addPermission(Permission permission) {
		Set<ConstraintViolation<Permission>> violations = validator.validate(permission);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		Permission found = permissionDao.find(permission.getDomain(), permission.getName());
		if (found == null) {
			try {
				permissionDao.insert(permission);
				return found;
			} catch (DuplicateKeyException ex) {
				throw new PermissionAlreadyExistException(ex, permission);
			}
		} else {
			throw new PermissionAlreadyExistException(null, permission);
		}
	}

	@Override
	public boolean removePermission(Permission permission) {
		boolean isSucceed = permissionDao.delete(permission.getDomain(), permission.getName());
		if (isSucceed) {
			List<Role> roles = this.getRoleWithPermission(permission);
			for (Role role : roles) {
				role.removePermission(permission);
			}
			return roleDao.bulkUpdate(roles) == roles.size();
		} else {
			return false;
		}
	}

	@Override
	public List<Permission> getPermissionList(String domain) {
		return permissionDao.getPermissionList(domain);
	}

	@Override
	public List<Role> getRoleList() {
		return roleDao.getRoleList();
	}

	@Override
	public Role getRole(String roleName) {
		return roleDao.findRole(roleName);
	}

	public List<Role> getRoleWithPermission(Permission permission) {
		List<String> roles = roleDao.getRoleIdentifierListWithPermission(permission);
		return roleDao.findRoles(roles.toArray(new String[0]));
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return permissionDao.find(permission.getDomain(), permission.getName()) != null;
	}

	@Override
	public boolean updatePermissionDescription(Permission permission) {
		return permissionDao.update(permission);
	}

}
