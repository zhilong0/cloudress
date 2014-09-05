package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.df.spec.locality.geo.CoordType;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.model.view.Supplier;
import com.df.spec.locality.service.SupplierService;

@Path("/suppliers")
@Produces("application/json;charset=UTF-8")
@Component
public class SupplierResources {

	private SupplierService supplierService;

	private GeoService geoService;

	public SupplierResources(GeoService geoService, SupplierService supplierService) {
		this.geoService = geoService;
		this.supplierService = supplierService;
	}

	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	public void setGeoService(GeoService geoService) {
		this.geoService = geoService;
	}

	@GET
	@Path("/{specialityCode}")
	public List<Supplier> getSpecialitySuppliers(@PathParam("specialityCode") String specialityCode, @QueryParam("coordtype") String coordtype,
			@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude) {
		CoordType type = CoordType.BD09LL;
		if (CoordType.WGS84LL.name().toLowerCase().equals(coordtype)) {
			type = CoordType.WGS84LL;
		} else if (CoordType.GCJ02LL.name().toLowerCase().equals(coordtype)) {
			type = CoordType.GCJ02LL;
		}
		Coordinate coordinate = new Coordinate(type, latitude, longitude);
		if (type != CoordType.BD09LL) {
			coordinate = geoService.convertCoordinate(coordinate, CoordType.BD09LL);
		}
		return supplierService.getSpecialitySupplier(specialityCode, coordinate);
	}
}