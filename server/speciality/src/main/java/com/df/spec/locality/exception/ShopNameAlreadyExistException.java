package com.df.spec.locality.exception;

public class ShopNameAlreadyExistException extends SpecialityBaseException {

	private static final long serialVersionUID = 1L;

	public static final String DUPLICATE_REGION_ERROR = "RG100001";

	private static final String MESSAGE_FMT = "Shop with name %s already exist";

	private String shopName;

	public ShopNameAlreadyExistException(Throwable cause, String shopName) {
		super(cause, MESSAGE_FMT, shopName);
		this.shopName = shopName;
	}

	public String getShopName() {
		return shopName;
	}
}
