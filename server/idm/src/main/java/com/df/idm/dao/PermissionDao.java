package com.df.idm.dao;

import java.util.List;

import com.df.idm.model.Permission;

public interface PermissionDao {

	Permission insert(Permission permission);

	boolean delete(String domain, String name);

	Permission find(String domain, String name);

	boolean update(Permission permssion);

	List<Permission> getPermissionList(String domain);

	List<Permission> getPermissionList(List<Permission> permssions);
}
