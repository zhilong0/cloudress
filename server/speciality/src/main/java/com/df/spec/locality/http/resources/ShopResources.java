package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.df.spec.locality.model.Shop;

@Path("/shops")
@Produces("application/json;charset=UTF-8")
@Component
public class ShopResources {

	@GET
	@Path("/{code}")
	public List<Shop> getShopListBySpeciality(@QueryParam("spec") String specialityCode, double latitude, double longitude) {
		return null;
	}

	public void addShop(Shop shop) {

	}

}
