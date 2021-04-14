package entity;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class Order implements Serializable {
	private static final double SERVICE_CHARGE = 1.1;
	private static final double GST = 1.07;
	private int staffID;
	private int tableNum;
	private int numOfPeople;
	private double grossPrice;
	private double nettPrice;
	private List<OrderItem> orderItems; // every order has a list of orderItem
	private String orderID;
	private Calendar orderDateTime;
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm");
	private static final LocalDateTime now = LocalDateTime.now();

	public Order(int staffID, int tableNum, int numOfPeople, double grossPrice, double nettPrice, String orderID,
			Calendar orderDateTime) {
		this.staffID = staffID;
		this.tableNum = tableNum;
		this.numOfPeople = numOfPeople;
		this.grossPrice = grossPrice;
		this.nettPrice = nettPrice;
		this.orderID = orderID;
		this.orderDateTime = orderDateTime;
		this.orderItems = new ArrayList<OrderItem>(); // storing an array list of object orderItem as an attribute into
														// object order
	}

	public Order(int staffID, int tableNum, int numOfPeople, double grossPrice, double nettPrice, String orderID,
			Calendar orderDateTime, List<OrderItem> orderItems) {
		this.staffID = staffID;
		this.tableNum = tableNum;
		this.numOfPeople = numOfPeople;
		this.grossPrice = grossPrice;
		this.nettPrice = nettPrice;
		this.orderID = orderID;
		this.orderDateTime = orderDateTime;

		if (orderItems != null)
			this.orderItems = orderItems;
		else
			this.orderItems = new ArrayList<OrderItem>();
	}

	public int getStaffID() {
		return staffID;
	}

	public int getId() {
		return tableNum;
	}

	public int getNumOfPeople() {
		return numOfPeople;
	}

	public double getGrossPrice() {
		return grossPrice;
	}

	public void setGrossPrice(double newGrossPrice) {
		grossPrice = newGrossPrice;
	}

	public double getNettPrice() {
		return nettPrice;
	}

	public void setNettPrice(double newNettPrice) {
		nettPrice = newNettPrice;
	}

	public String getOrderID() {
		return orderID;
	}

	public Calendar getOrderDateTime() {
		return orderDateTime;
	}

	public void addItemToOrder(Menu Menu, int quantity) {
		if (!orderItems.isEmpty()) // if array list is not empty
		{
			for (OrderItem orderItem : orderItems) {
				if (orderItem.getName() == Menu.getName()) {
					orderItem.incrementQuantity(quantity);
					recalculateOrderPrice();
					return; // if item is already in array list, increment and get out of method
				}
			}
		}
		OrderItem newOrderItem; //
		newOrderItem = new OrderItem(Menu.getName(), Menu.getPrice(), quantity);

		orderItems.add(newOrderItem);
		recalculateOrderPrice();
	}

	public void removeItemFromOrder(String itemName, int quantity) {
		if (getNumberOfOrderItems() == 0)
			return;

		Iterator<OrderItem> ordersIter = orderItems.iterator();
		OrderItem orderItem = null;

		while (ordersIter.hasNext()) {

			orderItem = ordersIter.next();

			if (orderItem.getName().equals(itemName)) {

				orderItem.decrementQuantity(quantity);

				if (orderItem.getQuantity() == 0)
					ordersIter.remove();

				break;
			}
		}

		recalculateOrderPrice();
	}

	public int getNumberOfOrderItems() {
		return orderItems.size();
	}

	public boolean isEmpty() {
		return orderItems.isEmpty();
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	private void recalculateOrderPrice() {
		if (getNumberOfOrderItems() == 0)
			return;
		double newGrossPrice = 0;
		for (OrderItem orderItem : orderItems) {
			newGrossPrice += (orderItem.getPrice() * orderItem.getQuantity());
		}

		setGrossPrice(newGrossPrice);

		double newNettPrice = 0;
		newNettPrice = newGrossPrice * SERVICE_CHARGE * GST;

		setNettPrice(newNettPrice);
	}

	public int getOrderItemQuantity(String itemName) {
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getName().equals(itemName))
				return orderItem.getQuantity();
		}
		return 0;
	}

	/**
	 * Display order details with proper formatting<br>
	 * Displayed information include order ID, staff ID, customer ID and the table
	 * number<br>
	 * The details of each order item included in the order is also displayed
	 */
	public void displayOrderDetails() {
		System.out.printf("%-35s", "Order ID: " + getOrderID());
		System.out.printf("%-17s", "Staff ID: " + getStaffID());
		System.out.printf("%-12s%n", "Table No: " + getId());

		int orderItemNo = 1;
		for (OrderItem orderItem : orderItems) {
			System.out.printf("%5s%-5s: ", "", ("(" + (orderItemNo++) + ")"));
			orderItem.displayOrderItemDetails();
		}
	}

	// getOrderID() getStaffID() getId()
	public void displayOrderSummary() {
		System.out.printf("%-25s", getOrderID());
		System.out.printf("%-15d", getStaffID());
		System.out.printf("%-15d%n", getId());
	}

	// Used for orderPayment();
	public void displayOrderInvoice() {
		System.out.println(
				"========================================= Order Invoice =========================================");

		System.out.printf("%n%-30s", "Order ID:" + getOrderID());
		System.out.printf("%-12s", "Staff ID:" + getStaffID());
		System.out.printf("%-31s", "Order Date:" + dtf.format(now));
		System.out.printf("%-11s", "Table No:" + getId());
		System.out.printf("%11s%n%n", "Pax:" + getNumOfPeople());

		int orderItemNo = 1;
		for (OrderItem orderItem : orderItems) {
			System.out.printf("%5s%-5s: ", "", ("(" + (orderItemNo++) + ")"));
			orderItem.displayOrderItemDetails();
		}

		System.out.println(
				"-------------------------------------------------------------------------------------------------");

		System.out.printf("%97s%n", "Subtotal: " + new DecimalFormat("$###,##0.00").format(getGrossPrice()));

		System.out.printf("%n%97s%n", "+10% Service Charge");
		System.out.printf("%97s%n", "+7% Goods & Service Tax");
		System.out.printf("%n%97s%n", "Total Payable: " + new DecimalFormat("$###,##0.00").format(getNettPrice()));

		System.out.println(
				"-------------------------------------------------------------------------------------------------");
		System.out.println("                             Thank you for dining with us!                             ");
		System.out.println(
				"========================================= Order Invoice =========================================");
	}
}
