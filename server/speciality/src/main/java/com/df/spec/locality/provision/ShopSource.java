package com.df.spec.locality.provision;

import java.util.ArrayList;
import java.util.List;

import com.df.spec.locality.model.Region;

public class ShopSource {

	private Region region;

	private List<ShopInfo> shops;

	private String imageBaseUri;

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setShops(List<ShopInfo> shops) {
		this.shops = shops;
	}

	public Region getRegion() {
		return region;
	}

	public List<ShopInfo> getShops() {
		return shops;
	}

	public String getImageBaseUri() {
		return imageBaseUri;
	}

	public void setImageBaseUri(String imageBaseUri) {
		this.imageBaseUri = imageBaseUri;
	}

	public static class ShopInfo {

		private String name;

		private String address;

		private String description;

		private String contact;

		private String telephone;

		private String[] images;

		private String businessHour;

		private List<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();

		ShopInfo() {
		}

		public ShopInfo(String name, String address) {
			this.name = name;
			this.address = address;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBusinessHour() {
			return businessHour;
		}

		public void setBusinessHour(String businessHour) {
			this.businessHour = businessHour;
		}

		public String getContact() {
			return contact;
		}

		public void setContact(String contact) {
			this.contact = contact;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public List<GoodsInfo> getGoodsList() {
			return goodsList;
		}

		public void setGoodsList(List<GoodsInfo> goodsList) {
			this.goodsList = goodsList;
		}

		public String[] getImages() {
			return images;
		}

		public void setImages(String[] images) {
			this.images = images;
		}
	}

	public static class GoodsInfo {

		private String specialityName;

		public String getSpecialityName() {
			return specialityName;
		}

		public void setSpecialityName(String specialityName) {
			this.specialityName = specialityName;
		}
	}
}
