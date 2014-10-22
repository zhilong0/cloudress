package com.tt.spec.locality.model;

import java.io.Serializable;
import java.util.Date;

public abstract class Approvable implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Status {
		WAIT_FOR_APPROVE, APPROVED, REJECTED
	}

	private String approvedBy;

	private Date approvedTime;

	private Status status;

	private String rejectReason;

	public String getApprovedBy() {
		return approvedBy;
	}

	public Date getApprovedTime() {
		return approvedTime;
	}

	public Status getStatus() {
		return status;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public void approved(String approver) {
		this.approvedBy = approver;
		this.approvedTime = new Date();
		this.status = Status.APPROVED;
	}

	public void rejected(String approver, String rejectReason) {
		this.approvedBy = approver;
		this.approvedTime = new Date();
		this.status = Status.APPROVED;
		this.rejectReason = rejectReason;
	}

	public void waitToApprove() {
		this.approvedBy = null;
		this.approvedTime = null;
		this.status = Status.WAIT_FOR_APPROVE;
		this.rejectReason = null;
	}
}
