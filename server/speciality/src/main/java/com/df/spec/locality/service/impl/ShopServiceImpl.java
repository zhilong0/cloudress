package com.df.spec.locality.service.impl;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.util.Assert;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageService;
import com.df.spec.locality.dao.ShopDao;
import com.df.spec.locality.exception.ShopErrorCode;
import com.df.spec.locality.exception.SpecialityBaseException;
import com.df.spec.locality.exception.validation.ValidationException;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;
import com.df.spec.locality.service.OperationPermissionEvaluator;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.ShopService;

public class ShopServiceImpl implements ShopService {

	private ShopDao shopDao;

	private RegionService regionService;

	private GeoService geoService;

	private ImageService imageService;

	private Validator validator;

	private OperationPermissionEvaluator permissionEvaluator;

	public ShopServiceImpl(ShopDao shopDao, RegionService regionService, GeoService geoService, ImageService imageService,
			OperationPermissionEvaluator permissionEvaluator, Validator validator) {
		this.shopDao = shopDao;
		this.regionService = regionService;
		this.geoService = geoService;
		this.imageService = imageService;
		this.validator = validator;
		this.permissionEvaluator = permissionEvaluator;
	}

	public void setServiceOperationPermissionEvaluator(OperationPermissionEvaluator serviceOperationPermissionEvaluator) {
		this.permissionEvaluator = serviceOperationPermissionEvaluator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
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
	public Shop addShop(Shop newShop) {
		Set<ConstraintViolation<Shop>> violations = validator.validate(newShop);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		Region region = regionService.getRegionByCode(newShop.getRegionCode(), true);
		if (newShop.getLocation().getCoordinate() == null) {
			String address = newShop.getLocation().getAddress();
			Coordinate coordiate = geoService.lookupCoordinate(address, region);
			if (coordiate == null) {
				throw new SpecialityBaseException(ShopErrorCode.SHOP_LOCATE_ERROR, "Cannot locate location for address %s", address);
			}
			newShop.getLocation().setCoordinate(coordiate);
		}
		newShop.setCreatedTime(new Date());
		newShop.setChangedTime(null);
		if (newShop.getImage() != null) {
			ImageAttributes imageAttributes = imageService.getImageAttributes(new ImageKey(newShop.getImage()));
			if (imageAttributes != null) {
				newShop.getImageSet().addImage(newShop.getImage(), imageAttributes);
			}
		}
		if (permissionEvaluator.canAddShop(newShop.getCreatedBy())) {
			newShop.approve(newShop.getCreatedBy());
		} else {
			newShop.reset();
		}
		shopDao.addShop(newShop, region);
		return newShop;
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
			Coordinate coordiate = geoService.lookupCoordinate(shop.getAddress(), region);
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
	public List<Shop> getWaitList(int offset, int limit) {
		return shopDao.getWaitList(offset, limit);
	}

	@Override
	public List<Shop> getMyShops(String userCode) {
		return shopDao.getShopListByCreatedBy(userCode);
	}
}
