package com.tt.spec.locality.dao.mongo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.cache.EntityCache;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.ImmutableList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.tt.spec.locality.dao.CampaignDao;
import com.tt.spec.locality.model.Campaign;
import com.tt.spec.locality.model.CampaignType;
import com.tt.spec.locality.model.Constants;
import com.tt.spec.locality.model.Participant;
import com.tt.spec.locality.model.ParticipantJournal;
import com.tt.spec.locality.model.SpecialityGroupPurcharse;

public class CampaignDaoImpl implements CampaignDao {

	private DatastoreImpl ds;

	public CampaignDaoImpl(Datastore ds) {
		if (!(ds instanceof DatastoreImpl)) {
			throw new IllegalArgumentException("ds must be instance of " + DatastoreImpl.class.getCanonicalName());
		}
		this.ds = (DatastoreImpl) ds;
		this.initTypes();
	}

	public CampaignDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		this(new DatastoreImpl(morphia, mongoClient, dbName));
	}

	protected Datastore getDataStore() {
		return ds;
	}

	protected void initTypes() {
		this.ds.getMapper().addMappedClass(SpecialityGroupPurcharse.class);
	}

	protected <T> QueryResults<T> find(final Query<T> q) {
		return q;
	}

	protected Class<? extends Campaign> getCampaignClassByType(CampaignType type) {
		if (type == CampaignType.SpecialityGroupPurcharse) {
			return SpecialityGroupPurcharse.class;
		} else {
			throw new IllegalArgumentException("Invalid campaign type");
		}
	}

	protected Campaign readCampaign(DBObject dbObject, EntityCache entityCache) {
		String campaginType = (String) dbObject.get(Constants.CAMPAIGN.TYPE);
		CampaignType type = CampaignType.valueOf(campaginType);
		return this.ds.getMapper().fromDBObject(this.getCampaignClassByType(type), dbObject, entityCache);
	}

	@Override
	public void insert(Campaign campaign) {
		Assert.notNull(campaign.getSponsor());
		Assert.notNull(campaign.getSubject());
		Assert.notNull(campaign.getStartTime());
		Assert.notNull(campaign.getEndTime());
		Assert.notNull(campaign.getDesc());
		Assert.notNull(campaign.getRegionCode());
		Assert.notNull(campaign.getType());
		if (campaign.getPublishDate() == null) {
			campaign.setPublishDate(new Date());
		}
		if (campaign.isRequireAssembly()) {
			campaign.setApplyDeadLine(campaign.getStartTime());
		}
		this.getDataStore().save(campaign, WriteConcern.JOURNALED);
	}

	@Override
	public List<Campaign> queryValidCampaignByType(String regionCode, CampaignType type, Date currentDate, int offset, int limit) {
		Class<? extends Campaign> campaignClazz = getCampaignClassByType(type);
		Query<? extends Campaign> query = this.getDataStore().createQuery(campaignClazz);
		query.filter(Constants.CAMPAIGN.REGION_CODE, regionCode);
		query.filter(Constants.CAMPAIGN.TYPE, type.name());
		query.filter(Constants.CAMPAIGN.APPLY_DEADLINE + " >", currentDate);
		query.filter(Constants.CAMPAIGN.IS_CANCELLED, false);
		query.order("-" + Constants.CAMPAIGN.PUBLISH_DATE);
		query.offset(offset);
		query.limit(limit);
		return ImmutableList.copyOf(this.find(query));
	}

	@Override
	public List<Campaign> queryValidCampaign(String regionCode, Date currentDate, int offset, int limit) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		DBCollection collection = this.getDataStore().getCollection(SpecialityGroupPurcharse.class);
		BasicDBObject where = new BasicDBObject();
		where.append(Constants.CAMPAIGN.REGION_CODE, regionCode);
		where.append(Constants.CAMPAIGN.IS_CANCELLED, false);
		BasicDBObject gtApplyDeadline = new BasicDBObject();
		gtApplyDeadline.append("$gt", currentDate);
		where.append(Constants.CAMPAIGN.APPLY_DEADLINE, gtApplyDeadline);
		DBCursor cursor = collection.find(where);
		cursor.skip(offset);
		cursor.limit(limit);
		Iterator<DBObject> iterator = cursor.iterator();
		EntityCache ec = this.ds.getMapper().createEntityCache();
		while (iterator.hasNext()) {
			DBObject dbObject = iterator.next();
			campaigns.add(this.readCampaign(dbObject, ec));
		}
		return campaigns;
	}

	@Override
	public Campaign getCampaign(String campaignId) {
		CampaignType[] types = CampaignType.values();
		for (CampaignType type : types) {
			Campaign found = this.getDataStore().get(this.getCampaignClassByType(type), new ObjectId(campaignId));
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	@Override
	public ParticipantJournal getCampaignParticipantJournal(String campaignId, String userId) {
		Query<ParticipantJournal> query = this.getDataStore().createQuery(ParticipantJournal.class);
		query.filter(Constants.PARTICIPANT_JOURNAL.CAMPAIGN_ID, campaignId);
		query.filter(Constants.PARTICIPANT_JOURNAL.USER_ID, userId);
		return query.get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean addParticipant(Campaign campaign, String userId) {
		boolean isSucceed = false;
		Participant participant = new Participant(userId);
		if (!campaign.addParticipant(participant)) {
			return false;
		}
		try {
			Query<Campaign> query = (Query<Campaign>) this.getDataStore().createQuery(campaign.getClass());
			query.filter(Constants.CAMPAIGN.ID, new ObjectId(campaign.getId()));
			UpdateOperations<Campaign> updateOperations = (UpdateOperations<Campaign>) this.getDataStore().createUpdateOperations(campaign.getClass());
			updateOperations.set(Constants.CAMPAIGN.PARTICIPANTS, campaign.getParticipants());
			isSucceed = this.getDataStore().updateFirst(query, updateOperations, false).getUpdatedCount() > 0;
			return isSucceed;
		} finally {
			if (!isSucceed) {
				campaign.removeParticipant(userId);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean removeParticipant(Campaign campaign, String userId) {
		boolean isSucceed = false;
		Participant participant = campaign.getParticipant(userId);
		if (!campaign.removeParticipant(userId)) {
			return false;
		}
		try {
			Query<Campaign> query = (Query<Campaign>) this.getDataStore().createQuery(campaign.getClass());
			query.filter(Constants.CAMPAIGN.ID, new ObjectId(campaign.getId()));
			UpdateOperations<Campaign> updateOperations = (UpdateOperations<Campaign>) this.getDataStore().createUpdateOperations(campaign.getClass());
			updateOperations.set(Constants.CAMPAIGN.PARTICIPANTS, campaign.getParticipants());
			isSucceed = this.getDataStore().updateFirst(query, updateOperations, false).getUpdatedCount() > 0;
			return isSucceed;
		} finally {
			if (!isSucceed) {
				campaign.addParticipant(participant);
			}
		}
	}

	@Override
	public boolean update(Campaign campaign) {
		@SuppressWarnings("unchecked")
		Query<Campaign> query = (Query<Campaign>) this.getDataStore().createQuery(campaign.getClass());
		query.filter(Constants.CAMPAIGN.ID, new ObjectId(campaign.getId()));
		Field field = ReflectionUtils.findField(Campaign.class, Constants.CAMPAIGN.VERSION);
		field.setAccessible(true);
		Long oldValue = new Long(0);
		try {
			oldValue = (Long) field.get(campaign);
			field.set(campaign, oldValue + 1);
		} catch (Throwable ex) {
			// should not reach here.
		}
		query.filter(Constants.CAMPAIGN.VERSION, oldValue);
		return this.getDataStore().updateFirst(query, campaign, false).getUpdatedExisting();
	}

	@Override
	public ParticipantJournal getParticipantJournal(String campaignId, String userId) {
		Query<ParticipantJournal> query = (Query<ParticipantJournal>) this.getDataStore().createQuery(ParticipantJournal.class);
		query.filter(Constants.PARTICIPANT_JOURNAL.CAMPAIGN_ID, campaignId);
		query.filter(Constants.PARTICIPANT_JOURNAL.USER_ID, userId);
		return query.get();
	}

	@Override
	public boolean createOrUpdate(ParticipantJournal journal) {
		Query<ParticipantJournal> query = this.getDataStore().createQuery(ParticipantJournal.class);
		query.filter(Constants.PARTICIPANT_JOURNAL.CAMPAIGN_ID, journal.getCampaignId());
		query.filter(Constants.PARTICIPANT_JOURNAL.USER_ID, journal.getUserId());
		UpdateOperations<ParticipantJournal> updateOperations = this.getDataStore().createUpdateOperations(ParticipantJournal.class);
		updateOperations.set(Constants.PARTICIPANT_JOURNAL.DEPART_COUNT, journal.getDepartCount());
		updateOperations.set(Constants.PARTICIPANT_JOURNAL.PARTICIPATE_TIME, journal.getParticipateTime());
		return this.getDataStore().update(query, updateOperations).getUpdatedCount() > 0;
	}

	@Override
	public List<Campaign> getParticipantCampaignList(String userId, boolean includeCancelled, boolean includeExpired) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		DBCollection collection = this.getDataStore().getCollection(SpecialityGroupPurcharse.class);
		BasicDBObject where = new BasicDBObject();
		Date now = new Date();
		where.append(Constants.CAMPAIGN.PARTICIPANT_USER_ID, userId);
		if (!includeCancelled) {
			where.append(Constants.CAMPAIGN.IS_CANCELLED, false);
		}
		if (!includeExpired) {
			BasicDBObject gtApplyDeadline = new BasicDBObject();
			gtApplyDeadline.append("$gt", now);
			where.append(Constants.CAMPAIGN.APPLY_DEADLINE, gtApplyDeadline);
		}
		DBCursor cursor = collection.find(where);
		Iterator<DBObject> iterator = cursor.iterator();
		EntityCache ec = this.ds.getMapper().createEntityCache();
		while (iterator.hasNext()) {
			DBObject dbObject = iterator.next();
			campaigns.add(this.readCampaign(dbObject, ec));
		}
		return campaigns;
	}

	@Override
	public List<Campaign> getPublishedCampaignList(String userId, boolean includeCancelled, boolean includeExpired) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		DBCollection collection = this.getDataStore().getCollection(SpecialityGroupPurcharse.class);
		BasicDBObject where = new BasicDBObject();
		Date now = new Date();
		where.append(Constants.CAMPAIGN.SPONSOR, userId);
		if (!includeCancelled) {
			where.append(Constants.CAMPAIGN.IS_CANCELLED, false);
		}
		if (!includeExpired) {
			BasicDBObject gtApplyDeadline = new BasicDBObject();
			gtApplyDeadline.append("$gt", now);
			where.append(Constants.CAMPAIGN.APPLY_DEADLINE, gtApplyDeadline);
		}
		DBCursor cursor = collection.find(where);
		Iterator<DBObject> iterator = cursor.iterator();
		EntityCache ec = this.ds.getMapper().createEntityCache();
		while (iterator.hasNext()) {
			DBObject dbObject = iterator.next();
			campaigns.add(this.readCampaign(dbObject, ec));
		}
		return campaigns;
	}

	@Override
	public boolean delete(String campaignId) {
		DBCollection collection = this.getDataStore().getCollection(SpecialityGroupPurcharse.class);
		DBObject query = new BasicDBObject();
		query.put(Constants.CAMPAIGN.ID, new ObjectId(campaignId));
		return collection.remove(query, WriteConcern.JOURNALED).getN() == 1;
	}
}
