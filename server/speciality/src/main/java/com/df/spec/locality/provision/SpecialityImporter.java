package com.df.spec.locality.provision;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.df.common.provision.AbstractImporterBean;
import com.df.common.provision.ProvisionContext;
import com.df.idm.authentication.UserPropertyAuthenticationToken;
import com.df.idm.model.Permission;
import com.df.idm.model.Role;
import com.df.spec.locality.model.ImageSet;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.provision.SpecialitySource.SpecialityInfo;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.SpecialityService;
import com.df.spec.locality.service.impl.OperationPermissionEvaluatorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpecialityImporter extends AbstractImporterBean implements InitializingBean, ResourceLoaderAware {

	@Autowired
	private SpecialityService specialityService;

	@Autowired
	private RegionService regionService;

	private ObjectMapper objectMapper;

	private List<String> resourceNames;

	private ResourceLoader resourceLoader;

	private String createdBy = "administrator";

	private static final Logger logger = LoggerFactory.getLogger(SpecialityImporter.class);

	public SpecialityImporter(int order, String groupName) {
		super(order, groupName);
	}

	public void setSpecialityService(SpecialityService specialityService) {
		this.specialityService = specialityService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public void execute(ProvisionContext context) throws Exception {
		if (resourceNames == null || resourceNames.size() == 0) {
			return;
		}
		for (String resourceName : resourceNames) {
			try {
				Resource resource = resourceLoader.getResource(resourceName);
				SpecialitySource source = objectMapper.readValue(resource.getInputStream(), SpecialitySource.class);
				this.importData(source);
			} catch (Throwable ex) {
				logger.error("Failed to import speciality from resource " + resourceName, ex);
			}
		}
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

	protected void importData(SpecialitySource source) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(mockAuthentication());
		SecurityContextHolder.setContext(context);
		Region found = regionService.getRegionByCode(source.getRegionCode(), true);

		String imageBaseUri = source.getImageBaseUri();
		List<SpecialityInfo> specialities = source.getSpecialities();
		for (SpecialityInfo speciality : specialities) {
			Speciality spec = specialityService.findSpeciality(found.getCode(), speciality.getName());
			if (spec == null) {
				spec = new Speciality();
				spec.setName(speciality.getName());
				spec.setDescription(speciality.getDescription());
				spec.setRank(speciality.getRank());
				spec.setStartMonth(speciality.getStartMonth());
				spec.setEndMonth(speciality.getEndMonth());
				spec.setCreatedBy(createdBy);
				specialityService.addSpeciality(spec, found);
			} else {
				logger.info("speciality {} in region {} already exist,update it", spec.getName(), found);
				spec.setDescription(speciality.getDescription());
				spec.setRank(speciality.getRank());
				spec.setStartMonth(speciality.getStartMonth());
				spec.setEndMonth(speciality.getEndMonth());
				specialityService.update(spec);
			}

			String[] images = speciality.getImages();
			ImageSet imageSet = spec.getImageSet();
			for (String image : images) {
				try {
					ImageSource imageSource = new ImageSource(imageBaseUri, image);
					String imageName = imageSource.getImageName();
					if (imageSet.hasImageWithName(imageName)) {
						continue;
					}
					specialityService.uploadSpecialityImage(spec.getCode(), imageSource.readToBytes(), imageName);
				} catch (IOException ex) {
					String msg = "Cannot import image %s for speciality %s";
					logger.warn(String.format(msg, image, spec.getName()), ex);
				}
			}
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(regionService, "regionServie must be set");
		Assert.notNull(specialityService, "specialityService must be set");
		Assert.notNull(objectMapper, "objectMapper must be set");
		Assert.notNull(resourceLoader, "resourceLoader must be set");
	}

}
