package com.andbase.demo.model;


public class City implements Comparable<City> {

	private int id;
	private String name;
	private String firstLetter;
	

	public City() {
		super();
	}

	public City(int id, String name, String firstLetter) {
		super();
		this.id = id;
		this.name = name;
		this.firstLetter = firstLetter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}
	

	@Override
	public int compareTo(City another) {
		if (this.getFirstLetter().equals("@")
				|| another.getFirstLetter().equals("#")) {
			return -1;
		} else if (this.getFirstLetter().equals("#")
				|| another.getFirstLetter().equals("@")) {
			return 1;
		} else {
			return this.getFirstLetter().compareTo(another.getFirstLetter());
		}
	}

}
