package com.df.idm.test;

import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.df.idm.common.validation.exception.ValidationException;
import com.df.idm.dao.UserDao;
import com.df.idm.model.User;
import com.df.idm.service.contract.UserManagementService;

public class UserManagementServiceTest extends IdmBaseTest {

	@Autowired
	private UserManagementService ums;

	@Autowired
	private UserDao userDao;

	@Test(expected = ValidationException.class)
	public void testNewUserWithoutPassword() {
		LocaleContextHolder.setLocale(Locale.CHINESE);
		ums.createUserByEmail("sala223@msn.com", null);
	}

	@Test
	public void testCreateUserWithEmail() {
		User newUser = null;
		try {
			newUser = ums.createUserByEmail("sala223@msn.com", "123456");
			TestCase.assertNotNull(ums.getUserByEmail("sala223@msn.com"));
		} finally {
			userDao.deleteUserByCode(newUser.getCode());
		}
	}

	@Test
	public void testCreateUserWithCode() {
		User newUser = null;
		try {
			newUser = ums.createUserByCode("sala223", "123456");
			TestCase.assertNotNull(ums.getUserByCode("sala223"));
		} finally {
			userDao.deleteUserByCode(newUser.getCode());
		}
	}

	@Test
	public void testCreateUserWithCellphone() {
		User newUser = null;
		try {
			newUser = ums.createUserByCellphone("13621992125", "123456");
			TestCase.assertNotNull(ums.getUserByCellphone("13621992125"));
		} finally {
			userDao.deleteUserByCode(newUser.getCode());
		}
	}

	@Test(expected = ValidationException.class)
	public void testNewUserErrorEmailFormat() {
		ums.createUserByEmail("dfdd343", "123456");
	}

	@Test
	public void testUpdateUser() {
		User newUser = null;
		try {
			newUser = ums.createUserByCode("sala223", "123456");
			newUser.setNickName("dasy");
			newUser.setFirstName("xia");
			newUser.setLastName("pin");
			ums.updateUser(newUser);
			User found = ums.getUserByCode("sala223");
			TestCase.assertTrue(newUser.getNickName().equals(found.getNickName()));
			TestCase.assertTrue(newUser.getFirstName().equals(found.getFirstName()));
			TestCase.assertTrue(newUser.getLastName().equals(found.getLastName()));
		} finally {
			userDao.deleteUserByCode(newUser.getCode());
		}
	}
}
