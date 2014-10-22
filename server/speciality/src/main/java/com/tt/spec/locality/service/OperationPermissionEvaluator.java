package com.tt.spec.locality.service;

import com.tt.spec.locality.model.Shop;
import com.tt.spec.locality.model.Speciality;

public interface OperationPermissionEvaluator {

	boolean canAddSpeciality(String userCode);

	boolean canEditSpeciality(String userCode, Speciality speciality);

	boolean canApprove(String userCode);

	boolean canAddShop(String userCode);

	boolean canEditShop(String userCode, Shop shop);

}
