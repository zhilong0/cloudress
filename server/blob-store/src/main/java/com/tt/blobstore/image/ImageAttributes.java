package com.tt.blobstore.image;

import java.io.Serializable;
import java.util.Date;

public final class ImageAttributes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private int width;

	private int heigth;

	private ImageFormat format;

	private String owner;

	private Date createdDate;

	ImageAttributes() {
	}

	public ImageAttributes(String imageName) {
		this.name = imageName;
	}

	public ImageAttributes(String imageName, int width, int heigth, ImageFormat format) {
		this.width = width;
		this.heigth = heigth;
		this.format = format;
		this.name = imageName;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeigth() {
		return heigth;
	}

	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

	public ImageFormat getFormat() {
		return format;
	}

	public void setFormat(ImageFormat format) {
		this.format = format;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
