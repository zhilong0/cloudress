package com.df.spec.locality.service;

import com.df.spec.locality.model.Approvable;

public interface ApprovableService {

	boolean approve(Approvable approvable, String approver);

	boolean reject(Approvable approvable, String approver, String rejectReason);
}
