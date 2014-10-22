package com.tt.idm.validation.group;

public interface UserConstraintGroup {

	public static interface CreateUser {
	}

	public static interface CreateUserByEmail extends CreateUser {
	}

	public static interface CreateUserByCellPhone extends CreateUser {
	}

}
