package com.df.spec.locality.service.impl;

import com.df.spec.locality.model.Shop;
import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.service.OperationPermissionEvaluator;

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
	public boolean canApproveSpeciality(String userCode) {
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

	@Override
	public boolean canApproveShop(String userCode) {
		return true;
	}

}
