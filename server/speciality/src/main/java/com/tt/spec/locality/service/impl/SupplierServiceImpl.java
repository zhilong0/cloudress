package com.tt.spec.locality.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.tt.common.utils.MathUtils;
import com.tt.spec.locality.geo.Coordinate;
import com.tt.spec.locality.model.Shop;
import com.tt.spec.locality.model.view.Supplier;
import com.tt.spec.locality.service.ShopService;
import com.tt.spec.locality.service.SupplierService;

public class SupplierServiceImpl implements SupplierService {

	public final static double AVERAGE_RADIUS_OF_EARTH = 6371.001;

	private ShopService shopService;

	public SupplierServiceImpl(ShopService shopService) {
		this.shopService = shopService;
	}

	@Override
	public List<Supplier> getSpecialitySupplier(String specialityCode, Coordinate cc) {
		List<Shop> shops = shopService.getShopListSellSpeciality(specialityCode);
		ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
		for (Shop shop : shops) {
			Supplier supplier = new Supplier(shop);
			Coordinate coordinate = shop.getLocation().getCoordinate();
			if (coordinate.getLatitude() <= 0 && coordinate.getLongitude() <= 0) {
				supplier.setDistance(0);
			} else {
				int distance = MathUtils.calculateDistance(cc.getLatitude(), cc.getLongitude(), coordinate.getLatitude(), coordinate.getLongitude());
				supplier.setDistance(distance);
			}
			suppliers.add(supplier);
		}
		return suppliers;
	}
}
