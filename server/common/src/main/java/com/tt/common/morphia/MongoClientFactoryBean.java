package com.tt.common.morphia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoClientFactoryBean extends AbstractFactoryBean<MongoClient> {

	private List<ServerAddress> servers = new ArrayList<ServerAddress>();

	private List<MongoCredential> credentials = new ArrayList<MongoCredential>();

	private MongoClientOptions mongoClientOptions;

	@Override
	public Class<?> getObjectType() {
		return MongoClient.class;
	}

	@Override
	protected MongoClient createInstance() throws Exception {
		if (servers.size() > 0) {
			if (mongoClientOptions != null) {
				return new MongoClient(servers, credentials, mongoClientOptions);
			}
			return new MongoClient(servers, credentials);
		}
		return new MongoClient();
	}

	private void setServerAddress(String... serverAddresses) {
		try {
			servers.clear();
			for (String addr : serverAddresses) {
				String[] a = addr.split(":", 2);
				String host = a[0];
				if (a.length == 2) {
					servers.add(new ServerAddress(host, Integer.parseInt(a[1])));
				} else {
					servers.add(new ServerAddress(host, ServerAddress.defaultPort()));
				}
			}
		} catch (Exception ex) {
			throw new BeanCreationException("Error when setServerAddress ", ex);
		}
	}

	public void setAddresses(String... serverAddresses) {
		setServerAddress(serverAddresses);
	}

	public void setMongoClientOptions(MongoClientOptions mongoClientOptions) {
		this.mongoClientOptions = mongoClientOptions;
	}

	public void setCredentials(MongoCredential[] credentials) {
		if (credentials != null) {
			this.credentials = Arrays.asList(credentials);
		}
	}

}
