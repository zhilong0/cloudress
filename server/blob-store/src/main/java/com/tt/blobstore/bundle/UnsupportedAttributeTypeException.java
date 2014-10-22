package com.tt.blobstore.bundle;

public class UnsupportedAttributeTypeException extends BlobStoreException {

	private static final long serialVersionUID = 1L;

	private Class<?> attributeType;

	public UnsupportedAttributeTypeException(Class<?> attributeType) {
		super("Attribute Type %s is not supported", attributeType.getCanonicalName());
		this.attributeType = attributeType;
	}

	public Class<?> getAttributeType() {
		return attributeType;
	}
}
