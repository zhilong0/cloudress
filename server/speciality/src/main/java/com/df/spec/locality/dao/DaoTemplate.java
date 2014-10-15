package com.df.spec.locality.dao;

import java.util.Map;

public interface DaoTemplate {

	<T> T add(T entity);

	<T> boolean update(Class<T> type, Object entityId, Map<String, Object> properties);

	<T> boolean deleteById(Class<T> type, Object entityId);

	<T> T findById(Class<T> type, Object entityId);

}
