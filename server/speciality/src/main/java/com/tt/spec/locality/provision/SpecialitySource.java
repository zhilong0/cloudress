package com.tt.spec.locality.provision;

import java.util.ArrayList;
import java.util.List;

public class SpecialitySource {

	private String regionCode;

	private String imageBaseUri;

	private List<SpecialityInfo> specialities = new ArrayList<SpecialityInfo>();

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
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

		private int startMonth;

		private int endMonth;

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

		public int getStartMonth() {
			return startMonth;
		}

		public void setStartMonth(int startMonth) {
			this.startMonth = startMonth;
		}

		public int getEndMonth() {
			return endMonth;
		}

		public void setEndMonth(int endMonth) {
			this.endMonth = endMonth;
		}
	}
}
