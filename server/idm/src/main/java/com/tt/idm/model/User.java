package com.tt.idm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.tt.idm.validation.group.UserConstraintGroup.CreateUser;
import com.tt.idm.validation.group.UserConstraintGroup.CreateUserByCellPhone;
import com.tt.idm.validation.group.UserConstraintGroup.CreateUserByEmail;

@Entity(value = "users", noClassnameStored = true)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	ObjectId id;

	@Size(message = "{user.firstName.Size}", max = 128)
	private String firstName;

	@Size(message = "{user.lastName.Size}", max = 128)
	private String lastName;

	@NotEmpty(message = "{user.password.NotEmpty}", groups = { CreateUser.class })
	@Size(message = "{user.password.Size}", min = 6, max = 30, groups = { CreateUser.class })
	private String password;

	@Size(message = "{user.nickName.Size}", max = 128)
	private String nickName;

	private int age;

	@Size(message = "{user.cellPhone.Size}", max = 20)
	@NotEmpty(message = "{user.cellPhone.NotEmpty}", groups = { CreateUserByCellPhone.class })
	@Indexed(name = "user_cellphone_idx", unique = true, sparse = true)
	private String cellphone;

	private String profileImage;

	private Gender gender;

	@Email(message = "{user.email.Email}")
	@NotEmpty(message = "{user.email.NotEmpty}", groups = { CreateUserByEmail.class })
	@Size(message = "{user.email.Size}", max = 128)
	@Indexed(name = "user_email_idx", unique = true, sparse = true)
	private String email;

	@NotEmpty(message = "{user.code.NotEmpty}")
	@Size(message = "{user.code.Size}", max = 64)
	@Indexed(name = "user_code_idx", unique = true, dropDups = false)
	private String code;

	private boolean isLocked;

	private Date createdTime;

	private Date changedTime;

	private Date lastLogin;

	private boolean isEmailVerified;

	private boolean isCellphoneVerified;

	private boolean isDisabled;

	@Reference(value = "roles", idOnly = true, ignoreMissing = true)
	private List<Role> roles = new ArrayList<Role>();

	@Embedded
	private ExternalUserReference externalUserReference;

	public User() {
	}

	public static User newUserByEmail(String email, String password) {
		User user = new User();
		user.code = email;
		user.email = email;
		user.password = password;
		return user;
	}

	public static User newUserByCode(String code, String password) {
		User user = new User();
		user.code = code;
		user.password = password;
		return user;
	}

	public static User newUserByCellphone(String cellphone, String password) {
		User user = new User();
		user.code = cellphone;
		user.cellphone = cellphone;
		user.password = password;
		return user;
	}

	public String getId() {
		if (id != null) {
			return id.toHexString();
		} else {
			return null;
		}
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setExternalUserReference(ExternalUserReference externalReference) {
		this.externalUserReference = externalReference;
	}

	public ExternalUserReference getExternalUserReference() {
		return externalUserReference;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getChangedTime() {
		return changedTime;
	}

	public void setChangedTime(Date changedTime) {
		this.changedTime = changedTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public boolean isCellphoneVerified() {
		return isCellphoneVerified;
	}

	public void setCellphoneVerified(boolean isCellphoneVerified) {
		this.isCellphoneVerified = isCellphoneVerified;
	}

	public static enum Gender {
		MALE, FEMALE
	}

	public void cleanPassword() {
		this.password = null;
	}

	public void addRole(Role role) {
		if (!this.roles.contains(role)) {
			this.roles.add(role);
		}
	}

	public boolean removeRole(String roleName) {
		for (Role role : roles) {
			if (role.getName().equals(roleName)) {
				return roles.remove(role);
			}
		}
		return false;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
}
