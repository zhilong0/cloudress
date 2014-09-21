package com.df.spec.locality.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Version;

import com.df.spec.locality.validation.constraint.AssemblyConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;

@AssemblyConstraint
public abstract class Campaign implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	ObjectId id;

	@NotEmpty(message = "{campaign.sponsor.NotEmpty}")
	private String sponsor;

	@NotEmpty(message = "{campaign.title.NotEmpty}")
	private String subject;

	private Date publishDate;

	@NotEmpty(message = "{campaign.region.NotEmpty}")
	private String regionCode;

	private String shopCode;

	private Date startTime;

	@NotNull(message = "{campaign.validTo.NotNull}")
	@Future(message = "{campaign.validTo.Future}")
	private Date endTime;

	private int participantLimit;

	private String contact;

	private String contactCellphone;

	private boolean isCancelled;

	@NotEmpty(message = "{campaign.content.NotEmpty}")
	private String desc;

	private boolean requireAssembly;

	private Location assemblyLocation;

	private Date assemblyTime;

	private Date lastModified;

	private Date applyDeadline;

	@NotEmpty(message = "{campaign.type.NotEmpty}")
	private String type;

	private List<Participant> participants = new ArrayList<Participant>();

	private ImageSet imageSet = new ImageSet();

	@Version
	@JsonIgnore
	long _version;

	public String getId() {
		if (id != null) {
			return id.toHexString();
		} else {
			return null;
		}
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean IsShopCampaign() {
		return shopCode != null;
	}

	public boolean isApplyOutDate() {
		Date now = new Date();
		return now.before(applyDeadline);
	}

	public int getParticipantCount() {
		return this.participants.size();
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getParticipantLimit() {
		return participantLimit;
	}

	public void setParticipantLimit(int participantLimit) {
		this.participantLimit = participantLimit;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactCellphone() {
		return contactCellphone;
	}

	public void setContactCellphone(String cellphone) {
		this.contactCellphone = cellphone;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}

	public boolean isRequireAssembly() {
		return requireAssembly;
	}

	public void setRequireAssembly(boolean requireAssembly) {
		this.requireAssembly = requireAssembly;
	}

	public Location getAssemblyLocation() {
		return assemblyLocation;
	}

	public String getAssemblyAddress() {
		return assemblyLocation == null ? null : assemblyLocation.getAddress();
	}

	public void setAssemblyLocation(Location assemblyLocation) {
		this.assemblyLocation = assemblyLocation;
	}

	public Date getAssemblyTime() {
		return assemblyTime;
	}

	public void setAssemblyTime(Date assemblyTime) {
		this.assemblyTime = assemblyTime;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public ImageSet getImageSet() {
		return imageSet;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public boolean addParticipant(Participant participant) {
		if (this.getParticipantLimit() <= participants.size()) {
			throw new IllegalStateException("Exceed participant limit");
		}
		for (Participant par : participants) {
			if (par.getUserId().equals(participant.getUserId())) {
				return false;
			}
		}
		return participants.add(participant);
	}

	public boolean removeParticipant(String userId) {
		for (Participant par : participants) {
			if (par.getUserId().equals(userId)) {
				return participants.remove(par);
			}
		}
		return false;
	}

	public Participant getParticipant(String userId) {
		for (Participant par : participants) {
			if (par.getUserId().equals(userId)) {
				return par;
			}
		}
		return null;
	}

	public Date getApplyDeadline() {
		return applyDeadline;
	}

	public void setApplyDeadLine(Date applyDeadline) {
		this.applyDeadline = applyDeadline;
	}
}
