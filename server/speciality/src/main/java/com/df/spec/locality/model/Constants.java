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

		public static final String ADDRESS = "address";

		public static final String GOODS_LIST = "goodsList";

		public static final String DESCRIPTION = "description";

		public static final String COORDINATE = "coordinate";
		
		public static final String IMAGESET = "imageSet";
		
		public static final String TEL = "telephone";

		public static final String CONTACT = "contact";
		
		public static final String BUSINESS_HOUR = "businessHour";
		
		public static final String GOODS_SPECIALITY = "goodsList.specialityCode";

	}

	public static interface SPECIALITY {

		public static final String CODE = "_id";

		public static final String NAME = "name";

		public static final String REGION_CODE = "regionCode";

		public static final String DESCRIPTION = "description";

		public static final String RANK = "rank";

		public static final String IMAGE_SET = "imageSet";

	}

}
