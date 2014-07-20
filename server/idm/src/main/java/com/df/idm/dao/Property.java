package com.df.idm.dao;

public class Property<T> {

	private String name;

	private T value;

	public Property(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return value;
	}
}
