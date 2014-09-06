package com.df.spec.locality.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.model.Shop;
import com.df.spec.locality.model.view.Supplier;
import com.df.spec.locality.service.ShopService;
import com.df.spec.locality.service.SupplierService;

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
			int distance = calculateDistance(cc.getLatitude(), cc.getLongitude(), coordinate.getLatitude(), coordinate.getLongitude());
			supplier.setDistance(distance);
			suppliers.add(supplier);
		}
		return suppliers;
	}

	public static int calculateDistance(double lat1, double lng1, double lat2, double lng2) {
		double latDistance = Math.toRadians(lat1 - lat2);
		double lngDistance = Math.toRadians(lng1 - lng2);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c * 1000));
	}

}
