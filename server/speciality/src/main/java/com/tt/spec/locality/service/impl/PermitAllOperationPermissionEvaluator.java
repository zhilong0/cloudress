package com.tt.spec.locality.service.impl;

import com.tt.spec.locality.model.Shop;
import com.tt.spec.locality.model.Speciality;
import com.tt.spec.locality.service.OperationPermissionEvaluator;

public class PermitAllOperationPermissionEvaluator implements OperationPermissionEvaluator {

	@Override
	public boolean canAddSpeciality(String userCode) {
		return true;
	}

	@Override
	public boolean canEditSpeciality(String userCode, Speciality speciality) {
		return true;
	}

	@Override
	public boolean canApprove(String userCode) {
		return true;
	}

	@Override
	public boolean canAddShop(String userCode) {
		return true;
	}

	@Override
	public boolean canEditShop(String userCode, Shop shop) {
		return true;
	}

}
