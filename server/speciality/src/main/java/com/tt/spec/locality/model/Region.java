package com.tt.spec.locality.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "regions", noClassnameStored = true)
@Indexes(@Index(value = "province,city,district", unique = true))
public class Region implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	String code;

	private String province;

	private String city;

	private String district;

	Region() {
	}

	public Region(String province, String city) {
		this(province, city, null);
	}

	public Region(String province, String city, String district) {
		this(null, province, city, district);
	}

	public Region(String code, String province, String city, String district) {
		this.province = province;
		this.district = district;
		this.city = city;
		this.code = code;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((district == null) ? 0 : district.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Region other = (Region) obj;
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (district == null) {
			if (other.district != null) {
				return false;
			}
		} else if (!district.equals(other.district)) {
			return false;
		}
		if (province == null) {
			if (other.province != null) {
				return false;
			}
		} else if (!province.equals(other.province)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "[" + this.getProvince() + "," + this.getCity() + "," + this.getDistrict() + "]";
	}

}
