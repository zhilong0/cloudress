package com.df.spec.locality.model;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "participantJournals", noClassnameStored = true)
@Indexes(@Index(value = "userId,campaignId", unique = true))
public class ParticipantJournal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	ObjectId id;

	private String userId;

	private String campaignId;

	private Date participateTime;

	private int departCount;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public Date getParticipateTime() {
		return participateTime;
	}

	public void setParticipateTime(Date participateTime) {
		this.participateTime = participateTime;
	}

	public int getDepartCount() {
		return departCount;
	}

	public void increaseDepartCount() {
		this.departCount++;
	}

	public void decreaseDepartCount() {
		this.departCount--;
		if (this.departCount < 0) {
			this.departCount = 0;
		}
	}
}
