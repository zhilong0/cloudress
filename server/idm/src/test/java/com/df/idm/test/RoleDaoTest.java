package com.df.idm.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.df.idm.dao.PermissionDao;
import com.df.idm.dao.RoleDao;
import com.df.idm.model.Permission;
import com.df.idm.model.Role;

public class RoleDaoTest extends IdmBaseTest {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PermissionDao permissionDao;

	@Test
	public void testCreateFindDeleteRole() {
		Role role = new Role("SYS_ADM");
		try {
			role.setDescription("test");
			roleDao.insertRole(role);
			Assert.notNull(roleDao.findRole("SYS_ADM"));
		} finally {
			roleDao.deleteRole("SYS_ADM");
			Assert.isNull(roleDao.findRole("SYS_ADM"));
		}
	}

	@Test
	public void testCreateRoleWithPermission() {
		Permission permission1 = new Permission("testDomain1", "name1");
		Permission permission2 = new Permission("testDomain2", "name2");
		Role role = new Role("SYS_ADM_2");
		try {
			permissionDao.insert(permission1);
			role.getPermissions().add(permission1);
			role.getPermissions().add(permission2);
			roleDao.insertRole(role);
			Role found = roleDao.findRole("SYS_ADM_2");
			Assert.notNull(found);
			TestCase.assertTrue(found.getPermissions().size() == 1);
		} finally {
			roleDao.deleteRole(role.getName());
			permissionDao.delete(permission1.getDomain(), permission1.getName());
		}
	}

	@Test
	public void testUpdateRole() {
		Permission permission1 = new Permission("testDomain1", "name1");
		Permission permission2 = new Permission("testDomain2", "name2");
		Role role = new Role("SYS_ADM_2");
		role.setDescription("desc");
		try {
			permissionDao.insert(permission1);
			permissionDao.insert(permission2);
			role.getPermissions().add(permission1);
			roleDao.insertRole(role);
			role.setDescription("desc2");
			role.getPermissions().add(permission2);
			ArrayList<Role> roles = new ArrayList<Role>();
			roles.add(role);
			roleDao.bulkUpdate(roles);
			Role found = roleDao.findRole("SYS_ADM_2");
			Assert.notNull(found);
			TestCase.assertTrue(found.getPermissions().size() == 2);
			TestCase.assertTrue(found.getDescription().equals("desc2"));
		} finally {
			roleDao.deleteRole(role.getName());
			permissionDao.delete(permission1.getDomain(), permission1.getName());
			permissionDao.delete(permission2.getDomain(), permission2.getName());
		}
	}

	@Test
	public void testGetRoleWithPermission() {
		Permission permission = new Permission("testDomain1", "name1");
		Role role1 = new Role("SYS_ADM_1");
		Role role2 = new Role("SYS_ADM_2");
		Role role3 = new Role("SYS_ADM_3");
		Role role4 = new Role("SYS_ADM_4");
		try {
			permissionDao.insert(permission);
			role1.getPermissions().add(permission);
			role4.getPermissions().add(permission);
			role3.getPermissions().add(permission);
			roleDao.insertRole(role1);
			roleDao.insertRole(role2);
			roleDao.insertRole(role3);
			roleDao.insertRole(role4);
			List<String> roles = roleDao.getRoleIdentifierListWithPermission(permission);
			TestCase.assertTrue(roles.contains(role1.getName()));
			TestCase.assertTrue(roles.contains(role3.getName()));
			TestCase.assertTrue(roles.contains(role4.getName()));
		} finally {
			permissionDao.delete(permission.getDomain(), permission.getName());
			roleDao.deleteRole(role1.getName());
			roleDao.deleteRole(role2.getName());
			roleDao.deleteRole(role3.getName());
			roleDao.deleteRole(role4.getName());
		}
	}

	@Test
	public void testFindRoles() {
		Permission permission = new Permission("testDomain1", "name1");
		Role role1 = new Role("SYS_ADM_1");
		Role role2 = new Role("SYS_ADM_2");
		Role role3 = new Role("SYS_ADM_3");
		Role role4 = new Role("SYS_ADM_4");
		try {
			permissionDao.insert(permission);
			role1.getPermissions().add(permission);
			role2.getPermissions().add(permission);
			role3.getPermissions().add(permission);
			role4.getPermissions().add(permission);
			roleDao.insertRole(role1);
			roleDao.insertRole(role2);
			roleDao.insertRole(role3);
			roleDao.insertRole(role4);
			List<Role> roles = roleDao.findRoles(role1.getName(), role2.getName(), role3.getName(), role4.getName());
			TestCase.assertTrue(roles.size() == 4);
			TestCase.assertTrue(roles.get(0).getPermissions().size() == 1);
			TestCase.assertTrue(roles.get(0).getPermissions().get(0).getName().equals("name1"));
		} finally {
			permissionDao.delete(permission.getDomain(), permission.getName());
			roleDao.deleteRole(role1.getName());
			roleDao.deleteRole(role2.getName());
			roleDao.deleteRole(role3.getName());
			roleDao.deleteRole(role4.getName());
		}
	}

}
