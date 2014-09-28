package com.df.idm.provision;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.df.common.provision.AbstractImporterBean;
import com.df.common.provision.ProvisionContext;
import com.df.idm.model.Permission;
import com.df.idm.service.contract.SecurityModelRepository;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;

public class PermissionImporter extends AbstractImporterBean implements ResourceLoaderAware {

	private List<String> resourceNames;

	private ResourceLoader resourceLoader;

	private SecurityModelRepository modelRepository;

	private static final Charset UTF8 = Charset.forName("utf-8");

	private static final Logger logger = LoggerFactory.getLogger(PermissionImporter.class);

	public PermissionImporter(int order, String groupName, SecurityModelRepository repository) {
		super(order, groupName);
		this.setSecurityModelRepository(repository);
	}

	public void setSecurityModelRepository(SecurityModelRepository repository) {
		this.modelRepository = repository;
	}

	public void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames;
	}

	@Override
	public void execute(ProvisionContext context) throws Exception {
		Assert.notNull(modelRepository);
		if (resourceNames == null) {
			logger.warn("resourceNames is not specified, ignore permission model import");
			return;
		}
		for (String resourceName : resourceNames) {
			logger.debug("import permision model from {}", resourceName);
			Resource resource = resourceLoader.getResource(resourceName);
			InputStream in = resource.getInputStream();
			Assert.notNull(in);
			Reader reader = new InputStreamReader(in, UTF8);
			CSVReaderBuilder<String[]> builder = new CSVReaderBuilder<String[]>(reader).strategy(CSVStrategy.UK_DEFAULT);
			CSVReader<String[]> csvReader = builder.entryParser(new DefaultCSVEntryParser()).build();
			List<String[]> entries;
			csvReader.readHeader();
			entries = csvReader.readAll();
			for (int i = 0; i < entries.size(); ++i) {
				String[] entry = entries.get(i);
				if (entry.length < 2) {
					logger.warn("Line {} is an invalid entry, domain, name must be provided.", i);
					continue;
				}
				Permission permission = new Permission(entry[0].trim(), entry[1].trim());
				if (entry.length >= 3) {
					permission.setDescription(entry[2]);
				}
				if (!modelRepository.hasPermission(permission)) {
					modelRepository.addPermission(permission);
				} else {
					logger.debug("permission {} already exist, update description");
					modelRepository.updatePermissionDescription(permission);
				}
			}
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
