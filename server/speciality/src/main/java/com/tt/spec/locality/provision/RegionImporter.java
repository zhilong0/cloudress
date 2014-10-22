package com.tt.spec.locality.provision;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.tt.common.provision.AbstractImporterBean;
import com.tt.common.provision.ProvisionContext;
import com.tt.spec.locality.service.RegionService;

public class RegionImporter extends AbstractImporterBean implements ResourceLoaderAware {

	private RegionService regionService;

	private String resourceName = "classpath:region.csv";

	private ResourceLoader resourceLoader;

	private static final Logger logger = LoggerFactory.getLogger(RegionImporter.class);

	public RegionImporter(int order, String groupName, RegionService regionService) {
		super(order, groupName);
		this.regionService = regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public void execute(ProvisionContext context) throws Exception {
		Assert.notNull(regionService);
		if (resourceName == null) {
			logger.warn("ResourceName is not specified, ignore region data import");
			return;
		}
		Resource resource = resourceLoader.getResource(resourceName);
		InputStream in = resource.getInputStream();
		regionService.importFromCSV(in, true);
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
