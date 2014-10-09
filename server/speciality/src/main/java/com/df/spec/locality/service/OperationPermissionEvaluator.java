package com.df.spec.locality.service;

import com.df.spec.locality.model.Shop;
import com.df.spec.locality.model.Speciality;

public interface OperationPermissionEvaluator {

	boolean canAddSpeciality(String userCode);

	boolean canEditSpeciality(String userCode, Speciality speciality);

	boolean canApprove(String userCode);

	boolean canAddShop(String userCode);

	boolean canEditShop(String userCode, Shop shop);

}
