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

		public static final String REGION_CODE = "regionCode";

		public static final String ADDRESS = "location.address";

		public static final String GOODS_LIST = "goodsList";

		public static final String DESCRIPTION = "description";

		public static final String COORDINATE = "location.coordinate";

		public static final String IMAGESET = "imageSet";

		public static final String TEL = "telephone";

		public static final String CONTACT = "contact";

		public static final String BUSINESS_HOUR = "businessHour";

		public static final String GOODS_SPECIALITY = "goodsList.specialityCode";

		public static final String COMMENT_OBJECT_TYPE = "shop";

	}

	public static interface SPECIALITY {

		public static final String CODE = "_id";

		public static final String NAME = "name";

		public static final String REGION_CODE = "regionCode";

		public static final String DESCRIPTION = "description";

		public static final String RANK = "rank";

		public static final String IMAGE_SET = "imageSet";

		public static final String START_MONTH = "startMonth";

		public static final String END_MONTH = "endMonth";

	}

	public static interface COMMENT {

		public static final String ID = "_id";

		public static final String OBJECT_ID = "commentObject.objectId";

		public static final String OBJECT_TYPE = "commentObject.objectType";

		public static final String USER_ID = "userId";

		public static final String WRITE_DATE = "writeDate";

	}

}
