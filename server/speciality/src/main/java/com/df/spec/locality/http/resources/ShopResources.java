package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.http.ImageDetails;
import com.df.blobstore.image.http.ImageLinkCreator;
import com.df.spec.locality.model.ImageSet;
import com.df.spec.locality.model.Shop;
import com.df.spec.locality.service.ShopService;

@Path("/shops")
@Produces("application/json;charset=UTF-8")
@Component
public class ShopResources {

	private ShopService shopService;

	private ImageLinkCreator imageLinkCreator;

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}

	public void setImageLinkCreator(ImageLinkCreator imageLinkCreator) {
		this.imageLinkCreator = imageLinkCreator;
	}

	@GET
	@Path("/{shopCode}")
	public Shop getShopByCode(@PathParam("shopCode") String shopCode) {
		Shop shop = shopService.getShopByCode(shopCode, true);
		processImageLink(shop);
		return shop;
	}

	@POST
	@Path("/region/{regionCode}")
	public void addShop(Shop shop, @PathParam(value = "regionCode") String regionCode) {
		shopService.addShop(shop, regionCode);
	}

	@GET
	@Path("/speciality/{specialityCode}")
	public List<Shop> getShopListSellSpecaility(@PathParam(value = "specialityCode") String specialityCode) {
		List<Shop> shops = shopService.getShopListSellSpeciality(specialityCode);
		for (Shop shop : shops) {
			processImageLink(shop);
		}
		return shops;
	}

	protected void processImageLink(Shop shop) {
		ImageSet imageSet = shop.getImageSet();
		for (ImageDetails image : imageSet.getImages()) {
			String link = imageLinkCreator.createImageLink(new ImageKey(image.getImageId()), image.getAttributes());
			image.setImageLink(link);
		}
	}
}
