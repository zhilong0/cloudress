package com.tt.common.morphia;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

public class MongoClientOptionsFactoryBean extends AbstractFactoryBean<MongoClientOptions> {

	private int connectionTimeout = 1000 * 10;

	private int socketTimeout = 0;

	private boolean readSecondary = false;

	private WriteConcern writeConcern = WriteConcern.JOURNALED;

	@Override
	public Class<?> getObjectType() {
		return null;
	}

	@Override
	protected MongoClientOptions createInstance() throws Exception {
		Builder builder = MongoClientOptions.builder();
		builder.connectTimeout(this.connectionTimeout);
		builder.socketTimeout(this.socketTimeout);
		builder.writeConcern(writeConcern);
		if (readSecondary) {
			builder.readPreference(ReadPreference.secondaryPreferred());
		}
		return builder.build();
	}

	public void setConnectionTimeout(int timeout) {
		this.connectionTimeout = timeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public void setReadSecondary(boolean readSecondary) {
		this.readSecondary = readSecondary;
	}

	public void setWriteConcern(WriteConcern writeConcern) {
		this.writeConcern = writeConcern;
	}

}
