package com.df.spec.locality.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.util.Assert;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageService;
import com.df.blobstore.image.http.ImageDetails;
import com.df.spec.locality.dao.ShopDao;
import com.df.spec.locality.exception.DuplicateShopException;
import com.df.spec.locality.exception.ShopErrorCode;
import com.df.spec.locality.exception.SpecialityBaseException;
import com.df.spec.locality.exception.validation.ValidationException;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.Product;
import com.df.spec.locality.model.ImageSet;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;
import com.df.spec.locality.service.OperationPermissionEvaluator;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.ShopService;

public class ShopServiceImpl implements ShopService {

	private ShopDao shopDao;

	private RegionService regionService;

	private GeoService geoService;

	private Validator validator;

	private ImageService imageService;

	private OperationPermissionEvaluator permissionEvaluator;

	public ShopServiceImpl(ShopDao shopDao, RegionService regionService, GeoService geoService, OperationPermissionEvaluator permissionEvaluator,
			Validator validator) {
		this.shopDao = shopDao;
		this.regionService = regionService;
		this.geoService = geoService;
		this.validator = validator;
		this.permissionEvaluator = permissionEvaluator;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
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
		if (shopDao.find(newShop.getName(), newShop.getAddress()) != null) {
			throw new DuplicateShopException(null, newShop.getName(), newShop.getAddress());
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
		newShop.setScore(5);
		if (newShop.getImage() != null) {
			if (imageService != null) {
				ImageAttributes imageAttributes = imageService.getImageAttributes(new ImageKey(newShop.getImage()));
				if (imageAttributes != null) {
					newShop.getImageSet().addImage(0, newShop.getImage(), imageAttributes);
				}
			} else {
				newShop.getImageSet().addImage(0, newShop.getImage(), null);
			}
		}
		if (permissionEvaluator.canAddShop(newShop.getCreatedBy())) {
			newShop.approved(newShop.getCreatedBy());
		} else {
			newShop.waitToApprove();
		}
		shopDao.add(newShop);
		List<Product> products = newShop.getProducts();
		for (Product product : products) {
			shopDao.addProduct(newShop.getCode(), product);
		}
		return newShop;
	}

	@Override
	public Shop getShopByCode(String shopCode, boolean throwException) {
		Shop found = shopDao.findById(Shop.class, shopCode);
		if (found == null && throwException) {
			throw new SpecialityBaseException(ShopErrorCode.SHOP_WITH_CODE_NOT_FOUND, "Shop with code %s not found", shopCode);
		}
		return found;
	}

	@Override
	public List<Shop> getShopListSellSpeciality(String specialityCode) {
		List<String> shopCodeList = shopDao.getShopCodeListBySpeciality(specialityCode, 0, -1);
		return shopDao.getShopList(shopCodeList, true);
	}

	@Override
	public Shop find(String shopName, String address) {
		return shopDao.find(shopName, address);
	}

	@Override
	public void update(Shop shop) {
		Assert.notNull(shop.getCode());
		Shop found = this.getShopByCode(shop.getCode(), true);
		Region region = regionService.getRegionByCode(found.getRegionCode(), true);
		if (!found.getAddress().equals(shop.getAddress())) {
			Coordinate coordiate = geoService.lookupCoordinate(shop.getAddress(), region);
			found.getLocation().setCoordinate(coordiate);
			found.getLocation().setAddress(shop.getAddress());
		}
		found.setDescription(shop.getDescription());
		found.setBusinessHour(shop.getBusinessHour());
		found.setContact(shop.getContact());
		found.setTelephone(shop.getTelephone());
		ImageSet imageSet = shop.getImageSet();
		found.getImageSet().clear();
		for (ImageDetails img : imageSet.getImages()) {
			found.getImageSet().addImage(img);
		}
		shopDao.update(found);
		List<Product> products = shopDao.getProductList(shop.getCode());
		List<Product> newProducts = shop.getProducts();
		for (Product product : products) {
			boolean shouldDelete = true;
			for (Product newProduct : newProducts) {
				if (product.getSpecialityCode().equals(newProduct.getSpecialityCode())) {
					shouldDelete = false;
				}
			}
			if (shouldDelete) {
				shopDao.deleteById(Product.class, product.getId());
			}
		}
		for (Product newProduct : newProducts) {
			boolean shouldAdd = true;
			for (Product product : products) {
				if (product.getSpecialityCode().equals(newProduct.getSpecialityCode())) {
					shouldAdd = false;
				}
			}
			if (shouldAdd) {
				shopDao.addProduct(shop.getCode(), newProduct);
			}
		}
	}

	@Override
	public List<Shop> getWaitToApproveShopList(int offset, int limit) {
		return shopDao.getWaitToApproveShopList(offset, limit);
	}

	@Override
	public List<Shop> getMyShops(String userCode) {
		return shopDao.getShopListByCreatedBy(userCode);
	}

	@Override
	public void disable(String shopCode) {
		if (!shopDao.disable(shopCode)) {
			throw new SpecialityBaseException(ShopErrorCode.SHOP_WITH_CODE_NOT_FOUND, "Shop with code %s not found", shopCode);
		}
	}

	@Override
	public void enable(String shopCode) {
		shopDao.enable(shopCode);
	}

	@Override
	public Product addProduct(String shopCode, Product product) {
		this.getShopByCode(shopCode, true);
		Product found = shopDao.getProduct(shopCode, product.getSpecialityCode());
		if (found != null) {
			if (!found.isDeleted()) {
				String msg = "speciality %s is already in selling list";
				throw new SpecialityBaseException(ShopErrorCode.SPECIALITY_ALREADY_IN_SELLING_LIST, msg, product.getSpecialityCode());
			} else {
				HashMap<String, Object> updates = new HashMap<String, Object>();
				updates.put(Constants.PRODUCT.IS_DELETED, false);
				shopDao.update(Product.class, product.getId(), updates);
			}
		}
		product = shopDao.addProduct(shopCode, product);
		return product;
	}

	@Override
	public boolean deleteProduct(String shopCode, String productId) {
		Product found = shopDao.findById(Product.class, productId);
		Shop shop = this.getShopByCode(shopCode, true);
		if (found != null && !found.isDeleted()) {
			if (shop != null) {
				shopDao.markProductAsDelete(productId);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Product> getProductList(String shopCode) {
		return shopDao.getProductList(shopCode);
	}

	@Override
	public List<Shop> getShopListInRegion(String regionCode, int offset, int limit) {
		return shopDao.getShopListInRegion(regionCode, offset, limit);
	}

	@Override
	public boolean updateImageSet(String shopCode, String[] imageIds, boolean isAdd) {
		if (isAdd) {
			List<ImageDetails> images = new ArrayList<ImageDetails>();
			for (String imageId : imageIds) {
				ImageAttributes imageAttributes = null;
				if (imageService != null) {
					imageAttributes = imageService.getImageAttributes(new ImageKey(imageId));
				}
				images.add(new ImageDetails(imageId, imageAttributes));
			}
			return shopDao.addImages(shopCode, images.toArray(new ImageDetails[0]));
		} else {
			return shopDao.deleteImages(shopCode, imageIds);
		}
	}
}
