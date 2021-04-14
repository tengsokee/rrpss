package entity;

import java.io.Serializable;
import java.text.DecimalFormat;
public class OrderItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private double price;
	private int quantity;
	
	//constructor
	public OrderItem(String name, double price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double newPrice) {
		price = newPrice;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int newQuantity) {
		quantity = newQuantity;
	}
	
	public void incrementQuantity(int increaseValue) {
		quantity += increaseValue;
	}
	
	public void decrementQuantity(int decreaseValue) {
		quantity -= decreaseValue;
	}
	
	/**
	 * Formats and display the details of this order item,
	 * including information such as quantity, name and the price
	 * of the order item (price of each item * quantity)
	 */
	public void displayOrderItemDetails()
	{
		System.out.printf("%-5s", getQuantity() + "x");
		System.out.printf("%-40s", getName());
		System.out.printf("%40s%n", new DecimalFormat("$###,##0.00").format(getPrice() * getQuantity()));
	}
	
	/**
	 * Displays a summary of this order item,
	 * including quantity and item name
	 */
	public void displayOrderItemSummary()
	{
		System.out.printf("%-5s", getQuantity() + "x");
		System.out.printf("%-30s%n", getName());
	}
}
