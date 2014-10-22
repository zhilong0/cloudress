package com.tt.spec.locality.service;

import java.util.List;

import com.tt.spec.locality.geo.Coordinate;
import com.tt.spec.locality.model.view.Supplier;

public interface SupplierService {

	List<Supplier> getSpecialitySupplier(String specialityCode, Coordinate currentCoordinate);

}
