package com.tt.spec.locality.service;

import com.tt.spec.locality.model.Approvable;

public interface ApprovableService {

	boolean approve(Approvable approvable, String approver);

	boolean reject(Approvable approvable, String approver, String rejectReason);
}
