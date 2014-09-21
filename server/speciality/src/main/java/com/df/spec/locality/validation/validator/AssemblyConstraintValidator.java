package com.df.spec.locality.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.df.spec.locality.model.Campaign;
import com.df.spec.locality.validation.constraint.AssemblyConstraint;

public class AssemblyConstraintValidator implements ConstraintValidator<AssemblyConstraint, Campaign> {

	@Override
	public void initialize(AssemblyConstraint constraintAnnotation) {
	}

	@Override
	public boolean isValid(Campaign value, ConstraintValidatorContext context) {
		boolean requireAssembly = value.isRequireAssembly();
		boolean isValid = true;
		if (requireAssembly) {
			context.disableDefaultConstraintViolation();
			if (value.getAssemblyTime() == null) {
				isValid = false;
				context.buildConstraintViolationWithTemplate("{campaign.assembly.assemblyTime.NotNull}").addConstraintViolation();
			}
			if (value.getAssemblyLocation() == null || value.getAssemblyLocation().getAddress() == null) {
				isValid = false;
				context.buildConstraintViolationWithTemplate("{campaign.assembly.assemblyLocation.NotNull}").addConstraintViolation();
			}
			if (value.getContact() == null) {
				isValid = false;
				context.buildConstraintViolationWithTemplate("{campaign.assembly.contact.NotNull}").addConstraintViolation();
			}
			if (value.getContactCellphone() == null) {
				isValid = false;
				context.buildConstraintViolationWithTemplate("{campaign.assembly.cellphone.NotNull}").addConstraintViolation();
			}
			if (value.getAssemblyTime() != null && value.getValidTo() != null) {
				if (!value.getAssemblyTime().after(value.getValidTo())) {
					isValid = false;
					context.buildConstraintViolationWithTemplate("{campaign.assembly.assemblyTime.Future}").addConstraintViolation();
				}
			}
		}
		return isValid;
	}

}
