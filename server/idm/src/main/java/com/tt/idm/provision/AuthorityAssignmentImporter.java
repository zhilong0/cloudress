package com.tt.idm.provision;

import java.io.InputStream;
import java.util.List;

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
import com.tt.idm.model.User;
import com.tt.idm.service.contract.UserAuthorityService;

public class AuthorityAssignmentImporter extends AbstractImporterBean implements ResourceLoaderAware {

	private String[] resourceNames;

	private ResourceLoader resourceLoader;

	private UserAuthorityService userAuthorityService;

	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(AuthorityAssignmentImporter.class);

	public AuthorityAssignmentImporter(int order, String groupName, ObjectMapper objectMapper, UserAuthorityService userAuthorityService) {
		super(order, groupName);
		this.objectMapper = objectMapper;
		this.userAuthorityService = userAuthorityService;
	}

	public void setUserAuthorityService(UserAuthorityService userAuthorityService) {
		this.userAuthorityService = userAuthorityService;
	}

	public void setResourceNames(String[] resourceNames) {
		this.resourceNames = resourceNames;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void execute(ProvisionContext context) throws Exception {
		Assert.notNull(userAuthorityService);
		Assert.notNull(objectMapper);
		if (resourceNames == null) {
			logger.warn("resourceNames is not specified, ignore user authority assignment import");
			return;
		}
		for (String resourceName : resourceNames) {
			logger.debug("import user authority from {}", resourceName);
			Resource resource = resourceLoader.getResource(resourceName);
			InputStream in = resource.getInputStream();
			User[] users = objectMapper.readValue(in, User[].class);
			for (User user : users) {
				List<Role> roles = user.getRoles();
				for (Role role : roles) {
					userAuthorityService.assign(user.getCode(), role.getName());
				}
			}
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
