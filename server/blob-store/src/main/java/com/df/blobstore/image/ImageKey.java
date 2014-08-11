package com.df.blobstore.image;

import java.io.Serializable;

/**
 * A unique key to represents a image which is stored by blob store.
 */
public final class ImageKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;

	public ImageKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return getKey();
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ImageKey other = (ImageKey) obj;
		if (key == null) {
			return other.key == null;
		} else {
			return key.equals(other.key);
		}
	}

}
