package com.df.spec.locality.service.impl;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.util.Assert;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageService;
import com.df.spec.locality.dao.ShopDao;
import com.df.spec.locality.exception.ShopErrorCode;
import com.df.spec.locality.exception.SpecialityBaseException;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;
import com.df.spec.locality.service.CommentService;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.ShopService;

public class ShopServiceImpl implements ShopService {

	private ShopDao shopDao;

	private RegionService regionService;

	private GeoService geoService;

	private ImageService imageService;

	private CommentService commentService;

	private static final String SHOP_OBJECT_TYPE = "shop";

	public ShopServiceImpl(ShopDao shopDao, RegionService regionService, GeoService geoService, ImageService imageService) {
		this.shopDao = shopDao;
		this.regionService = regionService;
		this.geoService = geoService;
		this.imageService = imageService;
	}

	public void setShopDao(ShopDao shopDao) {
		this.shopDao = shopDao;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setGeoService(GeoService geoService) {
		this.geoService = geoService;
	}

	@Override
	public void addShop(Shop newShop, String regionCode) {
		Assert.notNull(newShop.getName());
		Assert.notNull(newShop.getLocation());
		Assert.notNull(newShop.getLocation().getAddress());
		Region region = regionService.getRegionByCode(regionCode, true);
		String address = newShop.getLocation().getAddress();
		Coordinate coordiate = geoService.lookupCoordiate(address, region);
		if (coordiate == null) {
			throw new SpecialityBaseException(ShopErrorCode.SHOP_LOCATE_ERROR, "Cannot locate location for address %s", address);
		}
		newShop.getLocation().setCoordinate(coordiate);
		shopDao.addShop(newShop, region);
	}

	@Override
	public Shop getShopByCode(String shopCode, boolean throwException) {
		Shop found = shopDao.getShopByCode(shopCode);
		if (found == null && throwException) {
			throw new SpecialityBaseException(ShopErrorCode.SHOP_WITH_CODE_NOT_FOUND, "Cannot get shop with code %s", shopCode);
		}
		return found;
	}

	@Override
	public List<Shop> getShopListSellSpeciality(String specialityCode) {
		return shopDao.getShopListBySpeciality(specialityCode);
	}

	@Override
	public Shop findShop(String shopName, String address) {
		return shopDao.findShop(shopName, address);
	}

	@Override
	public void updateShop(Shop shop) {
		Assert.notNull(shop.getCode());
		Shop found = this.getShopByCode(shop.getCode(), true);
		Region region = regionService.getRegionByCode(found.getRegionCode(), true);
		if (!found.getAddress().equals(shop.getAddress())) {
			Coordinate coordiate = geoService.lookupCoordiate(shop.getAddress(), region);
			found.getLocation().setCoordinate(coordiate);
			found.getLocation().setAddress(shop.getAddress()); 
		}
		found.setDescription(shop.getDescription());
		found.setGoodsList(shop.getGoodsList());
		found.setBusinessHour(shop.getBusinessHour());
		found.setContact(shop.getContact());
		found.setTelephone(shop.getTelephone());
		shopDao.update(found);
	}

	@Override
	public String uploadShopImage(String shopCode, byte[] imageData, String imageName) {
		Shop found = this.getShopByCode(shopCode, true);
		ImageKey key = imageService.uploadImage(new ByteArrayInputStream(imageData), null, imageName);
		ImageAttributes imageAttributes = imageService.getImageAttributes(key);
		found.getImageSet().addImage(key.getKey(), imageAttributes);
		shopDao.update(found);
		return key.getKey();
	}

	@Override
	public void deleteShopImage(String shopCode, String imageId) {
		imageService.deleteImage(new ImageKey(imageId));
		Shop found = this.getShopByCode(shopCode, true);
		found.getImageSet().removeImage(imageId);
		shopDao.update(found);
	}

	@Override
	public List<Comment> getCommentList() {
		return null;
	}

}
