package com.df.spec.locality.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;

@Entity(value = "campaigns", noClassnameStored = true)
public class SpecialityGroupPurcharse extends Campaign {

	private static final long serialVersionUID = 1L;

	private float discount;

	private int minimumParticipants;

	private List<String> specialities = new ArrayList<String>();

	public SpecialityGroupPurcharse() {
		this.setType(CampaignType.SpecialityGroupPurcharse.name());
	}

	public List<String> getSpecialities() {
		return Collections.unmodifiableList(specialities);
	}

	public boolean addSpeciality(String speciality) {
		return this.specialities.add(speciality);
	}

	public boolean removeSpeciality(String speciality) {
		return this.specialities.remove(speciality);
	}

	public void setMinimumParticipants(int minimumParticipants) {
		this.minimumParticipants = minimumParticipants;
	}

	public int getMinimumParticipants() {
		return minimumParticipants;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}
}
