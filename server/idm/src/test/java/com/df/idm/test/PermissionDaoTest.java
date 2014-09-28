package com.df.idm.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.df.idm.dao.PermissionDao;
import com.df.idm.model.Permission;

public class PermissionDaoTest extends IdmBaseTest {

	@Autowired
	private PermissionDao permissionDao;

	@Test
	public void testCreateFindDeletePermission() {
		Permission permission = new Permission("testDomain", "name1");
		try {
			permissionDao.insert(permission);
			Assert.notNull(permissionDao.find(permission.getDomain(), permission.getName()));
		} finally {
			permissionDao.delete(permission.getDomain(), permission.getName());
			Assert.isNull(permissionDao.find(permission.getDomain(), permission.getName()));
		}
	}

	@Test
	public void testUpdatePermission() {
		Permission permission = new Permission("testDomain", "name1");
		permission.setDescription("desc1");
		try {
			permissionDao.insert(permission);
			permission.setDescription("desc2");
			permissionDao.update(permission);
			TestCase.assertEquals(permission.getDescription(), "desc2");
		} finally {
			permissionDao.delete(permission.getDomain(), permission.getName());
		}
	}

	@Test
	public void testGetPermissionListByDomain() {
		Permission permission1 = new Permission("testDomain", "name1");
		Permission permission2 = new Permission("testDomain", "name2");
		Permission permission3 = new Permission("testDomain", "name3");
		Permission permission4 = new Permission("testDomain2", "name3");
		try {
			permissionDao.insert(permission1);
			permissionDao.insert(permission2);
			permissionDao.insert(permission3);
			permissionDao.insert(permission4);
			TestCase.assertEquals(3, permissionDao.getPermissionList(permission1.getDomain()).size());
		} finally {
			permissionDao.delete(permission1.getDomain(), permission1.getName());
			permissionDao.delete(permission2.getDomain(), permission2.getName());
			permissionDao.delete(permission3.getDomain(), permission3.getName());
			permissionDao.delete(permission4.getDomain(), permission4.getName());
		}
	}

	@Test
	public void testGetPermissionList() {
		Permission permission1 = new Permission("domain1", "name1");
		Permission permission2 = new Permission("domain2", "name2");
		Permission permission3 = new Permission("domain3", "name3");
		Permission permission4 = new Permission("domain4", "name4");
		try {
			permissionDao.insert(permission1);
			permissionDao.insert(permission2);
			permissionDao.insert(permission3);
			permissionDao.insert(permission4);
			ArrayList<Permission> query = new ArrayList<Permission>();
			query.add(permission4);
			query.add(permission2);
			TestCase.assertEquals(2, permissionDao.getPermissionList(query).size());
		} finally {
			permissionDao.delete(permission1.getDomain(), permission1.getName());
			permissionDao.delete(permission2.getDomain(), permission2.getName());
			permissionDao.delete(permission3.getDomain(), permission3.getName());
			permissionDao.delete(permission4.getDomain(), permission4.getName());
		}
	}
}
