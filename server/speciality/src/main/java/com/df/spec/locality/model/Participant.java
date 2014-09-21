package com.df.spec.locality.model;

import java.io.Serializable;
import java.util.Date;

public class Participant implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private Date participateTime;

	Participant() {
	}

	public Participant(String userId) {
		this.userId = userId;
		this.participateTime = new Date();
	}

	public Participant(String userId, Date participateTime) {
		this.userId = userId;
		this.participateTime = participateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getParticipateTime() {
		return participateTime;
	}

	public void setParticipateTime(Date participateTime) {
		this.participateTime = participateTime;
	}

}
