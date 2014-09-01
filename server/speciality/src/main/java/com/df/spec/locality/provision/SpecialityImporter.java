package com.df.spec.locality.provision;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.df.common.provision.AbstractImporterBean;
import com.df.common.provision.ProvisionContext;
import com.df.spec.locality.model.ImageSet;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.provision.SpecialitySource.SpecialityInfo;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.SpecialityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

public class SpecialityImporter extends AbstractImporterBean implements ResourceLoaderAware {

	@Autowired
	private SpecialityService specialityService;

	@Autowired
	private RegionService regionService;

	private ObjectMapper objectMapper;

	private List<String> resourceNames;

	private ResourceLoader resourceLoader;

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

	@Override
	public void execute(ProvisionContext context) throws Exception {
		Assert.notNull(regionService);
		Assert.notNull(specialityService);
		Assert.notNull(objectMapper);
		if (resourceNames == null || resourceNames.size() == 0) {
			return;
		}
		for (String jsonResourceName : resourceNames) {
			Resource resource = resourceLoader.getResource(jsonResourceName);
			SpecialitySource source = objectMapper.readValue(resource.getInputStream(), SpecialitySource.class);
			Region region = source.getRegion();
			Region found = regionService.findRegion(region.getProvince(), region.getCity(), region.getDistrict());
			if (found == null) {
				logger.warn("Region {} does not exist, ignore resource {}", region, jsonResourceName);
			} else {
				String imageBaseLocation = source.getImageBaseLocation();
				List<SpecialityInfo> specialities = source.getSpecialities();
				for (SpecialityInfo speciality : specialities) {
					Speciality spec = specialityService.findSpeciality(found.getCode(), speciality.getName());
					if (spec == null) {
						spec = new Speciality();
						spec.setName(speciality.getName());
						spec.setDescription(speciality.getDescription());
						specialityService.addSpeciality(spec, found.getCode());
					} else { 
						logger.info("speciality {} in region {} already exist,update it", spec.getName(), found);
						spec.setDescription(speciality.getDescription());
						specialityService.update(spec);
					}

					String[] images = speciality.getImages();
					ImageSet imageSet = spec.getImageSet();
					for (String image : images) {
						String[] parts = image.split("/|\\\\");
						String imageName = parts[parts.length - 1];
						if (imageSet.hasImageWithName(imageName)) {
							continue;
						}
						File imageFile = new File(imageBaseLocation, image);
						specialityService.uploadSpecialityImage(spec.getCode(), Files.asByteSource(imageFile).read(), imageName);
					}
				}
			}
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
