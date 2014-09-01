package com.df.spec.locality.provision;

import java.io.InputStream;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.df.common.provision.AbstractImporterBean;
import com.df.common.provision.ProvisionContext;
import com.df.spec.locality.service.RegionService;

public class RegionImporter extends AbstractImporterBean implements ResourceLoaderAware {

	private RegionService regionService;

	private String csvResourceName = "classpath:region.csv";

	private ResourceLoader resourceLoader;

	public RegionImporter(int order, String groupName, RegionService regionService) {
		super(order, groupName);
		this.regionService = regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setCsvResourceName(String csvResourceName) {
		this.csvResourceName = csvResourceName;
	}

	@Override
	public void execute(ProvisionContext context) throws Exception {
		Assert.notNull(regionService);
		Resource resource = resourceLoader.getResource(csvResourceName);
		InputStream in = resource.getInputStream();
		regionService.importFromCSV(in, true);
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
