package com.tt.idm.model;

public interface Constants {

	public static interface PERMISSION {

		public static final String ID_PROPERTY = "_id";

		public static final String DOMAIN_PROPERTY = "domain";

		public static final String NAME_PROPERTY = "name";

		public static final String DESCRIPTION_PROPERTY = "description";
	}

	public static interface ROLE {

		public static final String NAME_PROPERTY = "name";

		public static final String PERMISSIONS_PROPERTY = "permissions";

		public static final String DESCRIPTION_PROPERTY = "description";

	}

	public static interface USER {

		public static final String ID_PROPERTY = "_id";

		public static final String EMAIL_PROPERTY = "email";

		public static final String CODE_PROPERTY = "code";

		public static final String NICK_NAME_PROPERTY = "nickName";

		public static final String CELL_PHONE_PROPERTY = "cellphone";

		public static final String PASSWORD_PROPERTY = "password";

		public static final String EXTERNAL_USER_ID_PROPERTY = "externalUserReference.externalId";

		public static final String EXTERNAL_USER_PROVIDER_PROPERTY = "externalUserReference.provider";

		public static final String AGE_PROPERTY = "age";

		public static final String GENDER_PROPERTY = "gender";

		public static final String FIRST_NAME_PROPERTY = "firstName";

		public static final String LAST_NAME_PROPERTY = "lastName";

		public static final String CREATED_TIME_PROPERTY = "createdTime";

		public static final String CHANGED_TIME_PROPERTY = "changedTime";

		public static final String LAST_LOGIN_PROPERTY = "lastLogin";

		public static final String IS_EMAIL_VERIFIED_PROPERTY = "isEmailVerified";

		public static final String IS_CELLPHONE_VERIFIED = "isCellphoneVerified";

		public static final String IS_DISABLED_PROPERTY = "isDisabled";

		public static final String ROLES_PROPERTY = "roles";

		public static final String IS_LOCKED_PROPERTY = "isLocked";

	}

}
