package com.df.idm.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.df.idm.dao.UserDao;
import com.df.idm.model.ExternalUserReference;
import com.df.idm.model.ExternalUserReference.Provider;
import com.df.idm.model.User;

@Transactional
public class UserDaoTest extends IdmBaseTest {

	@Autowired
	private UserDao userDao;

	@Test
	public void testCreateAndFindUser() {
		User user = new User();
		user.setCode("sala223@msn.com");
		user.setEmail("sala223@msn.com");
		user.setPassword("123456");
		try {
			userDao.insertUser(user);
			User found = userDao.getUserByCode("sala223@msn.com");
			TestCase.assertNotNull(found);
		} finally {
			if (user.getId() != null) {
				userDao.deleteUserById(user.getId());
			}
		}
	}

	@Test
	public void testFindUserByMail() {
		User user = new User();
		String email = "sala223@msn.com";
		user.setEmail(email);
		user.setCode(email);
		user.setPassword("123456");
		try {
			userDao.insertUser(user);
			User found = userDao.getUserByEmail(email);
			TestCase.assertNotNull(found);
		} finally {
			userDao.deleteUserByCode(email);
		}
	}

	@Test
	public void testFindUserByWeiboAccount() {
		User user = new User();
		String weiboAccount = "sala223";
		user.setEmail("randomId");
		user.setPassword("123456");
		user.setExternalUserReference(new ExternalUserReference(Provider.SINA, weiboAccount));
		try {
			userDao.insertUser(user);
			TestCase.assertNotNull(userDao.getUserByExternalId(weiboAccount, Provider.SINA));
		} finally {
			if (user.getId() != null) {
				userDao.deleteUserById(user.getId());
			}
		}
	}

	@Test
	public void testFindUserByCellphone() {
		User user = new User();
		String telephone = "13121992122";
		user.setCode(telephone);
		user.setPassword("123456");
		user.setCellphone(telephone);
		try {
			userDao.insertUser(user);
			TestCase.assertNotNull(userDao.getUserByCellphone(telephone));
		} finally {
			if (user.getId() != null) {
				userDao.deleteUserById(user.getId());
			}
		}

	}
}
