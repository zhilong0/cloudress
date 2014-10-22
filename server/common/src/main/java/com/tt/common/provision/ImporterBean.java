package com.tt.common.provision;

import org.springframework.core.Ordered;

public interface ImporterBean extends Ordered {

	void execute(ProvisionContext context) throws Exception;

	String getGroupName();
}
