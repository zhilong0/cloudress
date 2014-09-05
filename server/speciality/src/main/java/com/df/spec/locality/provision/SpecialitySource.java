package com.df.spec.locality.provision;

import java.util.ArrayList;
import java.util.List;

import com.df.spec.locality.model.Region;

public class SpecialitySource {

	private Region region;

	private String imageBaseUri;

	private List<SpecialityInfo> specialities = new ArrayList<SpecialityInfo>();

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getImageBaseUri() {
		return imageBaseUri;
	}

	public void setImageBaseUri(String imageBaseUri) {
		this.imageBaseUri = imageBaseUri;
	}

	public List<SpecialityInfo> getSpecialities() {
		return specialities;
	}

	public void setSpecialities(List<SpecialityInfo> specialities) {
		this.specialities = specialities;
	}

	public static class SpecialityInfo {

		private String name;

		private String description;

		private int rank;

		private String[] images;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String[] getImages() {
			return images;
		}

		public void setImages(String[] images) {
			this.images = images;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

	}
}
