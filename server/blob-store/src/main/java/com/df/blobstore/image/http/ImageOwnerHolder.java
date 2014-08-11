package com.df.blobstore.image.http;

public class ImageOwnerHolder {

	private static ThreadLocal<String> imageOwners;

	private static ThreadLocal<String> inheritableImageOwners;

	static {
		imageOwners = new ThreadLocal<String>();
		inheritableImageOwners = new InheritableThreadLocal<String>();
	}

	public static void reset() {
		imageOwners.remove();
		inheritableImageOwners.remove();
	}

	public static String getImageOwner() {
		String imageOwner = imageOwners.get();
		if (imageOwner == null) {
			imageOwner = inheritableImageOwners.get();
		}
		return imageOwner;
	}

	public static void setImageOwner(String imageOwner) {
		setImageOwner(imageOwner, false);
	}

	public static void setImageOwner(String imageOwner, boolean inheritable) {
		if (imageOwner == null) {
			reset();
		} else {
			if (inheritable) {
				inheritableImageOwners.set(imageOwner);
				imageOwners.remove();
			} else {
				imageOwners.set(imageOwner);
				inheritableImageOwners.remove();
			}
		}
	}
}
