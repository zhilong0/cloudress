package com.tt.common.provision;

public abstract class AbstractImporterBean implements ImporterBean {

	private int order;

	private String groupName;

	public AbstractImporterBean(int order, String groupName) {
		this.order = order;
		this.groupName = groupName;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	public String getGroupName() {
		return groupName;
	}

}
