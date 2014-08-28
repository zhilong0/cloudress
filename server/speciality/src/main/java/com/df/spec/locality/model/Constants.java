package com.df.spec.locality.model;

public class Constants {

	public static interface REGION {

		public static final String PROVINCE_PROPERTY = "province";

		public static final String CITY_PROPERTY = "city";

		public static final String DISTRICT_PROPERTY = "district";

	}

	public static interface SHOP {

		public static final String CODE = "code";

		public static final String NAME = "name";

		public static final String DISPLAY_NAME = "displayName";

		public static final String REGION_CODE = "regionCode";

		public static final String ADDRESS = "address";

	}
	
	public static interface SPECIALITY {

		public static final String CODE = "_id";

		public static final String NAME = "name";

		public static final String REGION_CODE = "regionCode";

	}

}
