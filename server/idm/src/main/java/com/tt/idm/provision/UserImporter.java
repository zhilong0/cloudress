package com.tt.idm.provision;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tt.common.provision.AbstractImporterBean;
import com.tt.common.provision.ProvisionContext;
import com.tt.common.utils.StringUtils;
import com.tt.idm.model.User;
import com.tt.idm.service.contract.UserManagementService;

public class UserImporter extends AbstractImporterBean implements ResourceLoaderAware {
	private List<String> resourceNames;

	private ResourceLoader resourceLoader;

	@Autowired
	private UserManagementService userManagementService;

	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(UserImporter.class);

	public UserImporter(int order, String groupName, ObjectMapper objectMapper) {
		super(order, groupName);
		this.objectMapper = objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setUserManagementService(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	public void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames;
	}

	@Override
	public void execute(ProvisionContext context) throws Exception {
		Assert.notNull(objectMapper);
		Assert.notNull(userManagementService);
		if (resourceNames == null) {
			logger.warn("resourceNames is not specified, ignore user import");
			return;
		}
		for (String resourceName : resourceNames) {
			logger.debug("import user from {}", resourceName);
			Resource resource = resourceLoader.getResource(resourceName);
			InputStream in = resource.getInputStream();
			User[] users = objectMapper.readValue(in, User[].class);
			for (User user : users) {
				String password = user.getPassword();
				String code = user.getCode();
				User found = userManagementService.getUserByCode(user.getCode());
				if (found == null) {
					if (StringUtils.isValidEmail(code)) {
						found = userManagementService.createUserByEmail(code, user.getPassword());
					} else if (StringUtils.isValidCellPhone(code)) {
						continue;
					} else {
						found = userManagementService.createUserByCode(code, user.getPassword());
					}
				} else {
					found = userManagementService.updateUser(user);
				}
				userManagementService.updatePassword(found.getCode(), password);
			}
		}

	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
