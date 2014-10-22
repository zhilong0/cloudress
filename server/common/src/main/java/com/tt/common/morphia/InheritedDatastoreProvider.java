package com.tt.common.morphia;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.mapping.lazy.DatastoreProvider;

public class InheritedDatastoreProvider implements DatastoreProvider {

	private static final long serialVersionUID = 1L;

	private Datastore datastoreImpl;

	public InheritedDatastoreProvider(Datastore datastore) {
		this.datastoreImpl = datastore;
	}

	@Override
	public Datastore get() {
		return datastoreImpl;
	}

}
