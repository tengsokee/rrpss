package entity;

import java.io.Serializable;

public class Menu implements Serializable {
	private String name;
	private double price;
	private String description;

	private enum Category {
		SoupBase, Meat, Vegetable, Seafood, Beverages, Dessert, Others
	}

	private String category;

	public Menu() {
	}

	public Menu(String n, double e, String d, String c) {
		name = n;
		price = e;
		description = d;
		category = c;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public void setItem(String name, double price, String description, String category) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
