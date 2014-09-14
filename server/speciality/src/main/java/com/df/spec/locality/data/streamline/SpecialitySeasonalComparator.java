package com.df.spec.locality.data.streamline;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import com.df.spec.locality.model.Speciality;

public class SpecialitySeasonalComparator implements Comparator<Speciality> {

	private int currentMonth;

	public SpecialitySeasonalComparator() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		this.currentMonth = calendar.get(Calendar.MONTH)+1;
	}

	@Override
	public int compare(Speciality s1, Speciality s2) {
		if (s1.isSeasonal()) {
			if (s2.isSeasonal()) {
				if (isInSeason(s1)) {
					if (isInSeason(s2)) {
						return s1.getRank() - s2.getRank();
					} else {
						return 1;
					}
				} else {
					if (isInSeason(s2)) {
						return -1;
					} else {
						return s1.getRank() - s2.getRank();
					}
				}
			} else {
				if (isInSeason(s1)) {
					return s1.getRank() - s2.getRank();
				} else {
					return -1;
				}
			}
		} else {
			if (s2.isSeasonal()) {
				if (isInSeason(s2)) {
					return s1.getRank() - s2.getRank();
				} else {
					return 1;
				}
			} else {
				return s1.getRank() - s2.getRank();
			}
		}
	}

	private boolean isInSeason(Speciality speciality) {
		if (speciality.getStartMonth() == this.currentMonth) {
			return true;
		} else if (speciality.getStartMonth() > this.currentMonth) {
			if (speciality.getEndMonth() < speciality.getStartMonth()) {
				return speciality.getEndMonth() >= this.currentMonth;
			} else {
				return false;
			}
		} else {
			if (speciality.getEndMonth() > speciality.getStartMonth()) {
				return speciality.getEndMonth() >= this.currentMonth;
			} else {
				return true;
			}
		}
	}
}
