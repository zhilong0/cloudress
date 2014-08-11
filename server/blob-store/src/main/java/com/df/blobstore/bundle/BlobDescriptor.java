package com.df.blobstore.bundle;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class BlobDescriptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private static Set<Class<?>> supportedAttributeType = new HashSet<Class<?>>();

	static {
		supportedAttributeType.add(String.class);
		supportedAttributeType.add(Integer.class);
		supportedAttributeType.add(Byte.class);
		supportedAttributeType.add(Short.class);
		supportedAttributeType.add(Long.class);
		supportedAttributeType.add(Float.class);
		supportedAttributeType.add(Double.class);
		supportedAttributeType.add(Date.class);
		supportedAttributeType.add(byte[].class);
		supportedAttributeType.add(int[].class);
		supportedAttributeType.add(float[].class);
		supportedAttributeType.add(double[].class);
		supportedAttributeType.add(short[].class);
		supportedAttributeType.add(long[].class);
		supportedAttributeType.add(String[].class);
	}

	public Object getAttribute(String attribute) {
		return attributes.get(attribute);
	}

	public void setAttribute(String attributeName, String value) {
		attributes.put(attributeName, (String) value);
	}

	public void setAttribute(String attributeName, int value) {
		attributes.put(attributeName, (Integer) value);
	}

	public void setAttribute(String attributeName, byte value) {
		attributes.put(attributeName, (Byte) value);
	}

	public void setAttribute(String attributeName, short value) {
		attributes.put(attributeName, (Short) value);
	}

	public void setAttribute(String attributeName, long value) {
		attributes.put(attributeName, (Long) value);
	}

	public void setAttribute(String attributeName, float value) {
		attributes.put(attributeName, (Float) value);
	}

	public void setAttribute(String attributeName, double value) {
		attributes.put(attributeName, (Double) value);
	}

	public void setAttribute(String attributeName, Date value) {
		attributes.put(attributeName, (Date) value);
	}

	public void setAttribute(String attributeName, byte[] value) {
		attributes.put(attributeName, value);
	}

	public void setAttribute(String attributeName, int[] value) {
		attributes.put(attributeName, value);
	}

	public void setAttribute(String attributeName, float[] value) {
		attributes.put(attributeName, value);
	}

	public void setAttribute(String attributeName, double[] value) {
		attributes.put(attributeName, value);
	}

	public void setAttribute(String attributeName, short[] value) {
		attributes.put(attributeName, value);
	}

	public void setAttribute(String attributeName, long[] value) {
		attributes.put(attributeName, value);
	}

	public void setAttribute(String attributeName, String[] value) {
		attributes.put(attributeName, value);
	}

	public void setAttribute(String attributeName, Object value) {
		if (value != null) {
			if (supportedAttributeType.contains(value.getClass())) {
			} else {
				throw new UnsupportedAttributeTypeException(value.getClass());
			}
			attributes.put(attributeName, value);
		} else {
			attributes.put(attributeName, null);
		}
	}

	public String[] getAttributeNames() {
		return attributes.keySet().toArray(new String[0]);
	}
}
