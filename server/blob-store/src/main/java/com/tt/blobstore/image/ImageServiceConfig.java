package com.tt.blobstore.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageServiceConfig {

	private List<ThumbnailDef> thumbnailDefs = new ArrayList<ThumbnailDef>();

	public ImageServiceConfig saveThumbnail(int width, int heigth) {
		ThumbnailDef thumbnailDef = new ThumbnailDef(width, heigth);
		if (!thumbnailDefs.contains(thumbnailDef)) {
			thumbnailDefs.add(thumbnailDef);
		}
		return this;
	}

	public List<ThumbnailDef> getThumbnailDefs() {
		return Collections.unmodifiableList(thumbnailDefs);
	}

	public static class ThumbnailDef {

		private int width;

		private int heigth;

		public ThumbnailDef(int width, int heigth) {
			this.width = width;
			this.heigth = heigth;
		}

		public int getWidth() {
			return width;
		}

		public int getHeigth() {
			return heigth;
		}

		@Override
		public int hashCode() {
			int prime = 31;
			int result = 1;
			result = prime * result + heigth;
			result = prime * result + width;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			ThumbnailDef other = (ThumbnailDef) obj;
			if (heigth != other.heigth)
				return false;
			if (width != other.width)
				return false;
			return true;
		}

	}
}
