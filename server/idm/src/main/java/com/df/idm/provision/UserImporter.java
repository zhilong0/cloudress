package com.df.idm.provision;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.df.common.provision.AbstractImporterBean;
import com.df.common.provision.ProvisionContext;
import com.df.common.utils.StringUtils;
import com.df.idm.model.User;
import com.df.idm.service.contract.UserManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserImporter extends AbstractImporterBean implements ResourceLoaderAware {
	private List<String> resourceNames;

	private ResourceLoader resourceLoader;

	private UserManagementService userManagementService;

	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(UserImporter.class);

	public UserImporter(int order, String groupName, ObjectMapper objectMapper, UserManagementService userManagementService) {
		super(order, groupName);
		this.userManagementService = userManagementService;
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
						userManagementService.createUserByEmail(code, user.getPassword());
					} else if (StringUtils.isValidCellPhone(code)) {
						userManagementService.createUserByCellphone(code, user.getPassword());
					} else {
						userManagementService.createUserByCode(code, user.getPassword());
					}
				} else {
					found = userManagementService.updateUser(user);
				}
				userManagementService.updatePassword(found.getCode(),password);
			}
		}

	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
