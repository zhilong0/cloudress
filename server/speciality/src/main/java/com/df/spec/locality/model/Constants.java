package com.df.spec.locality.model;

public class Constants {

	public static interface REGION {

		public static final String CODE_PROPERTY = "_id";

		public static final String PROVINCE_PROPERTY = "province";

		public static final String CITY_PROPERTY = "city";

		public static final String DISTRICT_PROPERTY = "district";

	}

	public static interface APPROVABLE {

		public static final String APPROVED_BY = "approval.approvedBy";

		public static final String APPROVED_TIME = "approval.approvedTime";

		public static final String STATUS = "approval.status";

		public static final String REJECT_REASON = "approval.rejectReason";

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

	public static interface SPECIALITY extends APPROVABLE {

		public static final String CODE = "_id";

		public static final String NAME = "name";

		public static final String REGION_CODE = "regionCode";

		public static final String DESCRIPTION = "description";

		public static final String RANK = "rank";

		public static final String IMAGE_SET = "imageSet";

		public static final String START_MONTH = "startMonth";

		public static final String END_MONTH = "endMonth";

		public static final String CREATED_BY = "createdBy";

		public static final String CREATED_TIME = "createdTime";
		
		public static final String APPROVAL = "approval";

	}

	public static interface COMMENT {

		public static final String ID = "_id";

		public static final String OBJECT_ID = "commentObject.objectId";

		public static final String OBJECT_TYPE = "commentObject.objectType";

		public static final String USER_ID = "userId";

		public static final String WRITE_DATE = "writeDate";

	}

	public static interface PARTICIPANT_JOURNAL {

		public static final String ID = "_id";

		public static final String USER_ID = "userId";

		public static final String CAMPAIGN_ID = "campaignId";

		public static final String PARTICIPATE_TIME = "participateTime";

		public static final String DEPART_COUNT = "departCount";

	}

	public static interface PARTICIPANT {

		public static final String USER_ID = "userId";

		public static final String PARTICIPATE_TIME = "participateTime";

	}

	public static interface CAMPAIGN {

		public static final String ID = "_id";

		public static final String VERSION = "_version";

		public static final String SPONSOR = "sponsor";

		public static final String TITLE = "subject";

		public static final String REGION_CODE = "regionCode";

		public static final String PUBLISH_DATE = "publishDate";

		public static final String SHOP_CODE = "shopCode";

		public static final String START_TIME = "startTime";

		public static final String END_TIME = "endTime";

		public static final String PARTICIPANT_LIMIT = "participantLimit";

		public static final String CONTACT = "contact";

		public static final String CELLPHONE = "cellphone";

		public static final String IS_CANCELLED = "isCancelled";

		public static final String REQUIRE_ASSEMBLY = "requireAssembly";

		public static final String LOCATION = "location";

		public static final String ASSEMBLY_TIME = "assemblyTime";

		public static final String TYPE = "type";

		public static final String CONTENT = "desc";

		public static final String APPLY_DEADLINE = "applyDeadline";

		public static final String IMAGE_SET = "imageSet";

		public static final String PARTICIPANTS = "participants";

		public static final String PARTICIPANT_USER_ID = "participants.userId";

	}

}
