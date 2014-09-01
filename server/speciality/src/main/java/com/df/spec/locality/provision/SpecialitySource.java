package com.df.spec.locality.provision;

import java.util.ArrayList;
import java.util.List;

import com.df.spec.locality.model.Region;

public class SpecialitySource {

	private Region region;

	private String imageBaseLocation;

	private List<SpecialityInfo> specialities = new ArrayList<SpecialityInfo>();

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getImageBaseLocation() {
		return imageBaseLocation;
	}

	public void setImageBaseLocation(String imageBaseLocation) {
		this.imageBaseLocation = imageBaseLocation;
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

	}
}
