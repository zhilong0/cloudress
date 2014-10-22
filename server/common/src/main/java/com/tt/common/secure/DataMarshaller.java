package com.tt.common.secure;

public interface DataMarshaller {

	public byte[] seal(byte[] data);

	public byte[] disclose(byte[] securedData);
}
