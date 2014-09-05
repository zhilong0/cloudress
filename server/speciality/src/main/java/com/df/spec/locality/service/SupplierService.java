package com.df.spec.locality.service;

import java.util.List;

import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.model.view.Supplier;

public interface SupplierService {

	List<Supplier> getSpecialitySupplier(String specialityCode, Coordinate currentCoordinate);

}
