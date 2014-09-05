package com.df.spec.locality.exception;

public class DuplicateShopException extends SpecialityBaseException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE_FMT = "Shop with name %s at address %s already exist";

	private String shopName;
	
	private String address;

	public DuplicateShopException(Throwable cause, String shopName, String address) {
		super(cause, MESSAGE_FMT, shopName,address);
		this.shopName = shopName;
		this.address = address;
		this.setErrorCode(ShopErrorCode.DUPLICATE_SHOP_NAME_WITH_ADDRESS); 
	}

	public String getShopName() {
		return shopName;
	}

	public String getAddress() {
		return address;
	}
}
