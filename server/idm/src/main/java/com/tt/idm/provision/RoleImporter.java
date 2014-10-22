package com.tt.idm.provision;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tt.common.provision.AbstractImporterBean;
import com.tt.common.provision.ProvisionContext;
import com.tt.idm.model.Role;
import com.tt.idm.service.contract.SecurityModelRepository;

public class RoleImporter extends AbstractImporterBean implements ResourceLoaderAware {

	private String[] resourceNames;

	private ResourceLoader resourceLoader;

	private SecurityModelRepository modelRepository;

	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(RoleImporter.class);

	public RoleImporter(int order, String groupName, ObjectMapper objectMapper, SecurityModelRepository repository) {
		super(order, groupName);
		this.objectMapper = objectMapper;
		this.setSecurityModelRepository(repository);
	}

	public void setResourceNames(String[] resourceNames) {
		this.resourceNames = resourceNames;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setSecurityModelRepository(SecurityModelRepository repository) {
		this.modelRepository = repository;
	}

	@Override
	public void execute(ProvisionContext context) throws Exception {
		Assert.notNull(modelRepository);
		Assert.notNull(objectMapper);
		if (resourceNames == null) {
			logger.warn("resourceNames is not specified, ignore role model import");
			return;
		}
		for (String resourceName : resourceNames) {
			logger.debug("import role model from {}", resourceName);
			Resource resource = resourceLoader.getResource(resourceName);
			InputStream in = resource.getInputStream();
			Role[] roles = objectMapper.readValue(in, Role[].class);
			for (Role role : roles) {
				Role found = modelRepository.getRole(role.getName());
				if (found == null) {
					modelRepository.addRole(role);
				} else {
					found.setDescription(role.getDescription());
					found.setPermissions(role.getPermissions());
					modelRepository.updateRole(found);
				}
			}
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
