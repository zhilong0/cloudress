package com.df.idm.service.impl;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.df.common.utils.StringUtils;
import com.df.idm.common.validation.exception.ValidationException;
import com.df.idm.dao.Property;
import com.df.idm.dao.UserDao;
import com.df.idm.exception.UserException;
import com.df.idm.model.Constants;
import com.df.idm.model.ExternalUserReference;
import com.df.idm.model.User;
import com.df.idm.model.ExternalUserReference.Provider;
import com.df.idm.registration.UserRegistrationTokenVerfier;
import com.df.idm.registration.message.RegistrationMessageNotifier;
import com.df.idm.service.contract.UserManagementService;
import com.df.idm.validation.group.UserConstraintGroup.CreateUser;

public class UserManagementServiceImpl implements UserManagementService {

	private UserDao userDao;

	private PasswordEncoder passwordEncoder;

	private UserRegistrationTokenVerfier registrationVerifier;

	private Validator validator;

	private RegistrationMessageNotifier messageNotifier;

	public UserManagementServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setRegistrationVerifier(UserRegistrationTokenVerfier registrationVerifier) {
		this.registrationVerifier = registrationVerifier;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void setRegistrationMessageNotifier(RegistrationMessageNotifier messageNotifier) {
		this.messageNotifier = messageNotifier;
	}

	public User createUser(User user) {
		Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class, CreateUser.class);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		User found = userDao.getUserByEmail(user.getEmail());
		if (found != null) {
			throw UserException.userEmailAlreadyExist(user.getEmail());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEmailVerified(false);
		user.setCellphoneVerified(false);
		userDao.insertUser(user);
		String emailToken = registrationVerifier.generateEmailRegistrationToken(user);
		messageNotifier.sendVerificationEmail(user, emailToken);
		return user;
	}

	public User updateUser(User user) {
		Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		int updated = userDao.updateUser(user);
		if (updated == 0) {
			throw UserException.userIdNotFound(user.getId());
		}
		return user;
	}

	public void updatePassword(String userId, String oldPassword, String newPassword) {
		User user = userDao.getUserByCode(userId);
		if (user == null) {
			throw UserException.userIdNotFound(userId);
		}

		String encodedOldPassword = passwordEncoder.encode(oldPassword);
		if (!user.getPassword().equals(encodedOldPassword)) {
			throw UserException.userPasswordNotMatch(userId);
		}
		String encodedPassword = passwordEncoder.encode(newPassword);
		userDao.updateUserPassword(userId, encodedPassword);
	}

	@Override
	public User getUserByExternalId(String externalId, Provider provider) {
		return userDao.getUserByExternalId(externalId, provider);
	}

	@Override
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Override
	public User getUserByCellphone(String cellPhone) {
		return userDao.getUserByCellphone(cellPhone);
	}

	@Override
	public User createUserByEmail(String email, String password) {
		User newUser = User.newUserByEmail(email, password);
		Set<ConstraintViolation<User>> violations = validator.validate(newUser, Default.class, CreateUser.class);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		User found = userDao.getUserByEmail(email);
		if (found != null) {
			throw UserException.userEmailAlreadyExist(email);
		}

		newUser.setCreatedTime(new Date());
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setEmailVerified(false);
		newUser.setCellphoneVerified(false);
		userDao.insertUser(newUser);
		String token = registrationVerifier.generateEmailRegistrationToken(newUser);
		messageNotifier.sendVerificationEmail(newUser, token);
		newUser.cleanPassword();
		return newUser;
	}

	@Override
	public User createUserByCellphone(String cellphone, String password) {
		User newUser = User.newUserByCellphone(cellphone, password);
		Set<ConstraintViolation<User>> violations = validator.validate(newUser, Default.class, CreateUser.class);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		User found = userDao.getUserByCellphone(cellphone);
		if (found != null) {
			throw UserException.userCellphoneAlreadyExist(cellphone);
		}
		newUser.setCreatedTime(new Date());
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setEmailVerified(false);
		newUser.setCellphoneVerified(false);
		userDao.insertUser(newUser);
		String token = registrationVerifier.generateCellphoneRegistrationToken(newUser);
		messageNotifier.sendVerificationShortMessage(newUser, token);
		newUser.cleanPassword();
		return newUser;
	}

	@Override
	public User createUserByCode(String userCode, String password) {
		User newUser = User.newUserByCode(userCode, password);
		Set<ConstraintViolation<User>> violations = validator.validate(newUser, Default.class, CreateUser.class);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		User found = userDao.getUserByCode(userCode);
		if (found != null) {
			throw UserException.userCodeAlreadyExist(userCode);
		}
		newUser.setCreatedTime(new Date());
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		userDao.insertUser(newUser);
		newUser.cleanPassword();
		return newUser;
	}

	@Override
	public User createUserByExternalUser(ExternalUserReference externalUser) {
		User newUser = new User();
		newUser.setExternalUserReference(externalUser);
		newUser.setCode(StringUtils.UUID());
		newUser.setCreatedTime(new Date());
		userDao.insertUser(newUser);
		return newUser;
	}

	@Override
	public User getUserById(String userId) {
		return userDao.getUserById(userId);
	}

	@Override
	public User getUserByCode(String userCode) {
		return userDao.getUserByCode(userCode);
	}

	@Override
	public void disableUser(String userId) {
		Property<Boolean> p1 = new Property<Boolean>(Constants.USER.IS_DISABLED_PROPERTY, true);
		Property<Date> p2 = new Property<Date>(Constants.USER.CHANGE_TIME_PROPERTY, new Date());
		int updated = userDao.updateUserProperties(userId, p1, p2);
		if (updated == 0) {
			throw UserException.userIdNotFound(userId);
		}
	}

	@Override
	public User verifyEmailRegistrationToken(String token) {
		String email = registrationVerifier.verifyEmailRegistrationToken(token);
		if (email == null) {
			return null;
		}
		User found = userDao.getUserByEmail(email);
		if (found == null) {
			return null;
		}

		if (found.isEmailVerified()) {
			return found;
		}
		found.setEmailVerified(true);
		userDao.updateUserProperties(found.getId(), new Property<Boolean>(Constants.USER.IS_EMAIL_VERIFIED_PROPERTY, true));
		found.cleanPassword();
		return found;
	}

	@Override
	public boolean verifyCellphoneRegistrationToken(String cellphone, String token) {
		boolean isCellphoneVerified = registrationVerifier.verifyCellphoneRegistrationToken(cellphone, token);
		if (!isCellphoneVerified) {
			return false;
		}
		User found = userDao.getUserByCellphone(cellphone);
		if (found == null) {
			return false;
		}

		if (found.isCellphoneVerified()) {
			return true;
		}
		found.setEmailVerified(true);
		userDao.updateUserProperties(found.getId(), new Property<Boolean>(Constants.USER.CELL_PHONE_PROPERTY, true));
		return true;
	}

	@Override
	public void unLockUser(String userId) {
		Property<Boolean> p1 = new Property<Boolean>(Constants.USER.IS_LOCKED_PROPERTY, false);
		Property<Date> p2 = new Property<Date>(Constants.USER.CHANGE_TIME_PROPERTY, new Date());
		int updated = userDao.updateUserProperties(userId, p1, p2);
		if (updated == 0) {
			throw UserException.userIdNotFound(userId);
		}
	}

	@Override
	public void lockUser(String userId) {
		Property<Boolean> p1 = new Property<Boolean>(Constants.USER.IS_LOCKED_PROPERTY, true);
		Property<Date> p2 = new Property<Date>(Constants.USER.CHANGE_TIME_PROPERTY, new Date());
		int updated = userDao.updateUserProperties(userId, p1, p2);
		if (updated == 0) {
			throw UserException.userIdNotFound(userId);
		}
	}

	@Override
	public void updateUserLastLogin(String userId, Date lastLoginDate) {
		int updated = userDao.updateUserProperties(userId, new Property<Date>(Constants.USER.LAST_LOGIN_PROPERTY, lastLoginDate));
		if (updated == 0) {
			throw UserException.userIdNotFound(userId);
		}
	}
}
