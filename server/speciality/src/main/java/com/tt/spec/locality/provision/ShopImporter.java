package com.tt.spec.locality.provision;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.ImageService;
import com.tt.blobstore.image.http.ImageDetails;
import com.tt.common.provision.AbstractImporterBean;
import com.tt.common.provision.ProvisionContext;
import com.tt.idm.authentication.UserPropertyAuthenticationToken;
import com.tt.idm.model.Permission;
import com.tt.idm.model.Role;
import com.tt.spec.locality.exception.RegionErrorCode;
import com.tt.spec.locality.exception.SpecialityBaseException;
import com.tt.spec.locality.model.ImageSet;
import com.tt.spec.locality.model.Product;
import com.tt.spec.locality.model.Region;
import com.tt.spec.locality.model.Shop;
import com.tt.spec.locality.model.Speciality;
import com.tt.spec.locality.provision.ShopSource.ProductInfo;
import com.tt.spec.locality.provision.ShopSource.ShopInfo;
import com.tt.spec.locality.service.RegionService;
import com.tt.spec.locality.service.ShopService;
import com.tt.spec.locality.service.SpecialityService;
import com.tt.spec.locality.service.impl.OperationPermissionEvaluatorImpl;

public class ShopImporter extends AbstractImporterBean implements ResourceLoaderAware {

	@Autowired
	private ShopService shopService;

	@Autowired
	private ImageService imageService;

	private List<String> resourceNames;

	private ResourceLoader resourceLoader;

	private ObjectMapper objectMapper;

	@Autowired
	private RegionService regionService;

	@Autowired
	private SpecialityService specialityService;

	private String createdBy = "administrator";

	private static final Logger logger = LoggerFactory.getLogger(ShopImporter.class);

	public ShopImporter(int order, String groupName) {
		super(order, groupName);
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setSpecialityService(SpecialityService specialityService) {
		this.specialityService = specialityService;
	}

	public void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames;
	}

	protected Authentication mockAuthentication() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role("dummy");
		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(new Permission(OperationPermissionEvaluatorImpl.DOMAIN_NAME, OperationPermissionEvaluatorImpl.MASTER_DATA_IMPORT));
		role.setPermissions(permissions);
		roles.add(role);
		return new UserPropertyAuthenticationToken(createdBy, roles);
	}

	@Override
	public void execute(ProvisionContext context) throws Exception {
		Assert.notNull(shopService, "shopService must be set");
		Assert.notNull(regionService, "regionService must be set");
		Assert.notNull(resourceLoader, "resourceLoader must be set");
		Assert.notNull(objectMapper, "objectMapper must be set");
		Assert.notNull(specialityService, "spcialityService must be set");

		if (resourceNames == null) {
			logger.warn("resourceNames is not set, ignore shop data import");
			return;
		}
		for (String resourceName : resourceNames) {
			Resource resource = resourceLoader.getResource(resourceName);
			logger.debug("Import shop data from {}", resourceName);
			InputStream in = resource.getInputStream();
			ShopSource shopSource = objectMapper.readValue(in, ShopSource.class);
			importData(shopSource);
		}
	}

	protected void importData(ShopSource shopSource) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(mockAuthentication());
		SecurityContextHolder.setContext(context);
		Region reg = shopSource.getRegion();
		Region region = regionService.findRegion(reg.getProvince(), reg.getCity(), reg.getDistrict());
		if (region == null) {
			throw new SpecialityBaseException(RegionErrorCode.REGION_NOT_FOUND_ERROR, "region %s is not found", reg);
		}

		for (ShopInfo shopInfo : shopSource.getShops()) {
			Shop shop = shopService.find(shopInfo.getName(), shopInfo.getAddress());

			if (shop == null) {
				shop = new Shop(shopInfo.getName(), shopInfo.getAddress());
				shop.setRegionCode(region.getCode());
				shop.setContact(shopInfo.getContact());
				shop.setTelephone(shopInfo.getTelephone());
				shop.setDescription(shopInfo.getDescription());
				shop.setBusinessHour(shopInfo.getBusinessHour());
				shop.setCreatedBy(createdBy);
				shop = shopService.addShop(shop);
			} else {
				shop.getLocation().setAddress(shopInfo.getAddress());
				shop.setDescription(shopInfo.getDescription());
				shop.setContact(shopInfo.getContact());
				shop.setTelephone(shopInfo.getTelephone());
				shop.setBusinessHour(shopInfo.getBusinessHour());
			}
			List<ProductInfo> products = shopInfo.getProducts();
			if (products != null) {
				for (ProductInfo product : products) {
					Speciality speciality = specialityService.findSpeciality(region.getCode(), product.getSpecialityName());
					if (speciality != null) {
						shop.addProduct(new Product(speciality.getCode()));
					}
				}
			}
			shopService.update(shop);
			String[] images = shopInfo.getImages();
			ImageSet imageSet = shop.getImageSet();
			for (String image : images) {
				try {
					ImageSource imageSource = new ImageSource(shopSource.getImageBaseUri(), image);
					String imageName = imageSource.getImageName();
					if (imageSet.hasImageWithName(imageName)) {
						continue;
					}

					ImageKey key = imageService.uploadImage(new ByteArrayInputStream(imageSource.readToBytes()), null, imageName);
					shop.getImageSet().addImage(new ImageDetails(key.getKey(), imageService.getImageAttributes(key)));
					shopService.update(shop);
				} catch (IOException ex) {
					String msg = "Cannot import image %s for shop %s";
					logger.warn(String.format(msg, image, shop.getName()), ex);
				}
			}
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
}
