package restaurant;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import entity.Menu;
import entity.Order;
import entity.OrderItem;

public class OrderManager extends RRPSS {
	private static String CurrentOfilename = "currentO.dat";
	private static String CompletedOfilename = "completedO.dat";
	private static ArrayList<Order> completedOrders = (ArrayList) Database.read(CompletedOfilename);
	private static ArrayList<Order> currentOrders = (ArrayList) Database.read(CurrentOfilename);

	private enum OrderSubmenuState {
		ViewCurrentOrders, CreateOrder, AddItemToOrder, RemoveItemFromOrder, MakePayment
	}

	private static OrderManager orderManager = null;
	private static Scanner sc = new Scanner(System.in);

	public OrderManager() {
		completedOrders = (ArrayList) Database.read(CompletedOfilename);
		currentOrders = (ArrayList) Database.read(CurrentOfilename);
	}

	public static OrderManager getOrderManager() {
		if (orderManager == null) {
			orderManager = new OrderManager();
		}

		return orderManager;
	}

	public static void main(String[] args) {
		getOrderChoice();
	}

	public static void displayOrderSubmenuOptions() {
		System.out.println("==================================================");
		System.out.println("|                  Order Submenu                 |");
		System.out.println("==================================================");

		System.out.println("0. Return to main menu");
		System.out.println("1. View current orders");
		System.out.println("2. Create order");
		System.out.println("3. Add item to existing order");
		System.out.println("4. Remove item from existing order");
	}

//method to call other methods in order sub menu
	public static int getOrderChoice() {
		int maxOrderChoices = OrderSubmenuState.values().length;
		int orderChoice = -1;
		do {
			displayOrderSubmenuOptions();

			do {

				try {
					System.out.printf("%nPlease enter your choice (0-%d): ", maxOrderChoices);
					orderChoice = sc.nextInt();
					sc.nextLine();
				} catch (InputMismatchException ex) {
					System.out.println("Invalid input! Please try again..");
					sc.nextLine(); // Clear the garbage input
					continue;
				} catch (Exception ex) {
					System.out.println("Invalid input! Please try again..");
					sc.nextLine(); // Clear the garbage input
					continue;
				}

				if (orderChoice < 0 || orderChoice > maxOrderChoices)
					System.out.println("Invalid choice! Please try again..");

			} while (orderChoice < 0 || orderChoice > maxOrderChoices);

			if (orderChoice == 0)
				return orderChoice; // Go back to main menu
			else {
				switch (OrderSubmenuState.values()[orderChoice - 1]) {
				case ViewCurrentOrders:
					viewCurrentOrders();
					break;

				case CreateOrder:
					createOrder();
					break;

				case AddItemToOrder:
					addItemToOrder();
					break;

				case RemoveItemFromOrder:
					removeItemFromOrder();
					break;
				}
			}
			return orderChoice;
		} while (orderChoice == 0);

	}

	private static void viewCurrentOrders() {
		if (currentOrders.isEmpty()) {
			System.out.print("\nWell, there are no orders taken at the moment!");
			System.out.println(" Try taking a new order? :-)");
			return;
		}
		System.out.println("**************************************** Orders ***************************************");
		int currOrderNo = 1;

		for (Order order : currentOrders) {
			System.out.printf("%n%-5s", "(" + (currOrderNo++) + ")");
			order.displayOrderDetails();
		}

		System.out.println("**************************************** Orders *************************************** ");
	}

	public static void createOrder() {
		try {
			System.out.println("Enter staff id: ");
			int staffID = StaffManager.inputStaffId();
			System.out.println("Enter the number of guest(s): ");
			int numOfPpl = validatedPax();
			int tableID = TableManager.allocateTable(numOfPpl);
			TableManager.setTableIdToOccupied(tableID);

			Calendar currentTime = GregorianCalendar.getInstance();
			Date currentDateTime = currentTime.getTime();
			currentTime.setTime(currentDateTime);

			Timestamp timestamp = new Timestamp(currentDateTime.getTime());
			String timestampStr = timestamp.toString();
			System.out.println(timestampStr);
			timestampStr = timestampStr.replaceAll("[^0-9]", ""); // replace all non digits with empty char

			String orderID = timestampStr + tableID;

			Order newOrder = new Order(staffID, tableID, numOfPpl, 0.0, 0.0, orderID, currentTime);

			System.out.println("\nWhat would you like to have?");

			Menu selectedItem = null;

			do {
				selectedItem = MenuManager.selectedItem();
				if (selectedItem != null) {
					System.out.print("Enter the quantity for this item: ");
					int itemQuantity = sc.nextInt();

					if (itemQuantity < 1) {
						System.out.println("\nInvalid input!" + " Minimum quantity is 1..");
						continue;
					}

					newOrder.addItemToOrder(selectedItem, itemQuantity);
				}

			} while (selectedItem != null);
			System.out.println(newOrder.getOrderID());

			if (newOrder.getNumberOfOrderItems() == 0) {
				System.out.print("\nCannot create an order with 0 items!");
				System.out.println(" Failed to create order, please try again..");
				return;
			} else {
				currentOrders.add(newOrder);
				System.out.println("\nSucessfully created new order!");
				System.out.println(currentOrders);
				Database.save(CurrentOfilename, currentOrders);
			}
		} catch (InputMismatchException ex) {
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to create order, please try again..");

			sc.nextLine();
			return;
		} catch (Exception ex) {
			System.out.println("Failed to create order, please try again..");

			sc.nextLine();
			return;
		}
	}

	private static void addItemToOrder() {
		if (currentOrders.isEmpty()) {
			System.out.println("There are no orders at the moment.");
			System.out.println("Do you want to create a new order?");
			return;
		}

		try {
			int orderIndex = 0;
			int maxOrders = currentOrders.size();

			System.out.println();
			System.out.printf("%5s%-25s", "", "Order ID");
			System.out.printf("%-15s", "Staff ID");
			System.out.printf("%-15s%n", "Table Number");

			for (Order order : currentOrders) {
				System.out.printf("%-5s", "(" + (++orderIndex) + ")");
				order.displayOrderSummary();
			}

			System.out.println("Select the order to add to (0 to cancel): ");
			int selectedOrderIndex = sc.nextInt();

			if (selectedOrderIndex == 0) {
				System.out.println("\nNothing to be updated!");
				return;
			}

			// Valid values from 1 to maxOrders
			if (selectedOrderIndex < 1 || selectedOrderIndex > maxOrders) {
				System.out.println("Invalid input! Failed to update order, try again!");
				return;
			}

			Order updateOrder = currentOrders.get(selectedOrderIndex - 1);
			System.out.println("What would you like to add to the order?");

			Menu selectedItem = null;
			selectedItem = MenuManager.selectedItem();
			if (selectedItem != null) {
				System.out.print("Enter the quantity for this item: ");
				int itemQuantity = sc.nextInt();

				if (itemQuantity < 1) {
					System.out.println("\nInvalid input!" + " Minimum quantity is 1..");
				} else {
					updateOrder.addItemToOrder(selectedItem, itemQuantity);
					System.out.printf("%nSuccessfully added \"%dx" + " %s\" to the order!%n", itemQuantity,
							selectedItem.getName());
				}
			}
		} catch (InputMismatchException ex) {
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to add item to order, please try again!");

			sc.nextLine();
			return;
		} catch (Exception ex) {
			System.out.println("Failed to add item to order, please try again!");

			sc.nextLine();
			return;
		}
	}

	private static void removeItemFromOrder() {
		if (currentOrders.isEmpty()) {
			System.out.println("There are no orders at the moment.");
			System.out.println("Do you want to create a new order?");
			return;
		}

		try {
			int orderIndex = 0;
			int maxOrders = currentOrders.size();

			System.out.printf("%5s%-25s", "", "Order ID");
			System.out.printf("%5s%-15s", "", "Staff ID");
			System.out.printf("%5s%-15s", "", "Table Number");
			System.out.println();

			for (Order order : currentOrders) {
				System.out.printf("%-5s", "(" + (++orderIndex) + ")");
				order.displayOrderSummary();
			}

			System.out.println("Select the order to remove from (0 to cancel): ");
			int selectedOrderIndex = sc.nextInt();

			if (selectedOrderIndex == 0) {
				System.out.println("Nothing to be updated!");
				return;
			}

			if (selectedOrderIndex < 1 || selectedOrderIndex > maxOrders) {
				System.out.println("Invalid input! Failed to update order, try again!");
				return;
			}

			Order updateOrder = currentOrders.get(selectedOrderIndex - 1);
			System.out.println("Which item would you like to remove from the order?");

			List<OrderItem> orderItems = updateOrder.getOrderItems();

			int orderItemIndex = 0;
			int maxOrderItems = orderItems.size();

			for (OrderItem orderItem : orderItems) {
				System.out.printf("%-5s", "(" + (++orderItemIndex) + ")");
				orderItem.displayOrderItemSummary();
			}

			System.out.println("Select the order item to remove from (0 to cancel): ");
			int selectedOrderItemIndex = sc.nextInt();

			if (selectedOrderItemIndex == 0) {
				System.out.println("There was nothing updated!");
				return;
			}

			if (selectedOrderItemIndex < 1 || selectedOrderItemIndex > maxOrderItems) {
				System.out.println("Invalid input! Failed to update order, try again!");
				return;
			}

			OrderItem removedOrderItem = orderItems.get(selectedOrderItemIndex - 1);

			if (removedOrderItem != null) {
				int maxItemQuantity = updateOrder.getOrderItemQuantity(removedOrderItem.getName());

				System.out.print("Enter the number of quantity to be removed");
				int itemQuantity = sc.nextInt();

				if (itemQuantity < 1) {
					System.out.println("Invalid input! Minimum quantity to be removed must be at least 1.");
					System.out.println("Failed to remove item from order, please try again!");
				} else if (itemQuantity > maxItemQuantity) {
					System.out.printf("Invalid input! Maximum quantity that can be removed is %d. \n", maxItemQuantity);
					System.out.println("Failed to remove item from order, please try again!");
				} else {
					updateOrder.removeItemFromOrder(removedOrderItem.getName(), itemQuantity);
					System.out.printf("\nSuccessfully removed " + "%dx " + "%s " + "from the order!%n", itemQuantity,
							removedOrderItem.getName());

					if (updateOrder.getNumberOfOrderItems() == 0) {
						System.out.print("\nThe order is now empty, removing order!");
						currentOrders.remove(updateOrder);
						return;

					}
				}
			}
		} catch (InputMismatchException ex) {
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to add item to order, please try again!");

			sc.nextLine();
			return;
		} catch (Exception ex) {
			System.out.println("Failed to add item to order, please try again!");

			sc.nextLine();
			return;
		}
	}

	public static void makePayment() {
		if (currentOrders.isEmpty()) {
			System.out.println("There are no existing orders at the moment!");
			return;
		}

		int orderIndex = 0;
		int maxOrders = currentOrders.size();
		System.out.printf("%5s%-25s", "", "Order ID");
		System.out.printf("%-15s", "Staff ID");
		System.out.printf("%-15s%n", "Table Number");

		for (Order order : currentOrders) {
			System.out.printf("%-5s", "(" + (++orderIndex) + ")");
			order.displayOrderSummary();
		}

		System.out.println("Select the order to make payment for (input 0 to cancel): ");
		int selectedOrderIndex = sc.nextInt();

		if (selectedOrderIndex == 0) {
			System.out.println("There was no payment made!");
			return;
		}

		if (selectedOrderIndex < 1 || selectedOrderIndex > maxOrders) {
			System.out.println("This is not a valid input. No payment made, try again.");
			return;
		}

		Order paymentForOrder = currentOrders.get(selectedOrderIndex - 1);
		paymentForOrder.displayOrderInvoice();
		int currentTableId = paymentForOrder.getId();
		TableManager.setTableIdToVacated(currentTableId);

		// Include change status of table from occupied to free

		completedOrders.add(paymentForOrder);
		Database.save(CompletedOfilename, completedOrders);
		currentOrders.remove(paymentForOrder);
		Database.save(CurrentOfilename, currentOrders);
	}

	public static void displaySaleRevenue() {
		if (completedOrders.isEmpty()) {
			System.out.println("\nNo completed orders! Nothing to be displayed..");
			return;
		}

		try {
			System.out.println("\n(1) Display sale revenue report by day");
			System.out.println("(2) Display sale revenue report by month");

			System.out.print("\nPlease select the mode of display" + " (0 to cancel): ");

			int displayMode = sc.nextInt();

			if (displayMode == 0) {
				System.out.println("\nNothing to be displayed!");
				return;
			}

			if (displayMode < 1 || displayMode > 2) {
				System.out.println("\nInvalid choice, valid options are (1) & (2)!");
				System.out.println("Failed to display sale revenue report, please try again..");
				return;
			}

			if (displayMode == 1)
				displaySalesRevenueDaily();
			else
				displaySalesRevenueMonthly();
		} catch (InputMismatchException ex) {
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to display sale revenue report, please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		} catch (Exception ex) {
			System.out.println("Failed to display sale revenue report, please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}

	public static void displaySalesRevenueDaily() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			sdf.setLenient(false);

			System.out.println("\nDisplaying sale revenue report by day..");
			System.out.print("Enter date (dd/mm/yyyy): ");
			String saleRevenueDateStr = sc.next();

			Date saleRevenueDate = sdf.parse(saleRevenueDateStr);
			Calendar saleRevenueCal = GregorianCalendar.getInstance();
			saleRevenueCal.setTime(saleRevenueDate);

			Calendar currentInstant = GregorianCalendar.getInstance();
			Date currentDateTime = currentInstant.getTime();
			currentInstant.setTime(currentDateTime);
			currentInstant.set(Calendar.HOUR_OF_DAY, 23);
			currentInstant.set(Calendar.MINUTE, 59);

			if (saleRevenueCal.after(currentInstant)) {
				System.out.print("\nInvalid date! ");
				System.out.println("Failed to check sale revenue report, please try again..");
				System.out.println("NOTE: You can only check sale revenue reports for previous days!");
				return;
			}

			double overallRevenue = 0.0;

			for (Order order : completedOrders) {
				Calendar orderDateTime = order.getOrderDateTime();

				if ((orderDateTime.get(Calendar.YEAR) == saleRevenueCal.get(Calendar.YEAR))
						&& (orderDateTime.get(Calendar.MONTH) == saleRevenueCal.get(Calendar.MONTH))
						&& (orderDateTime.get(Calendar.DAY_OF_MONTH) == saleRevenueCal.get(Calendar.DAY_OF_MONTH))) {
					overallRevenue += order.getNettPrice();
					order.displayOrderInvoice();
				}
			}

			SimpleDateFormat saleRevenueDateFormat;
			saleRevenueDateFormat = new SimpleDateFormat("E, dd/MM/yyyy");

			if (overallRevenue == 0.0) {
				System.out.println("\nThere are no sales made on the selected day, \""
						+ saleRevenueDateFormat.format(saleRevenueDate) + "\"");
			} else {
				System.out.println("\nTotal sales for" + " \"" + saleRevenueDateFormat.format(saleRevenueDate) + "\": "
						+ new DecimalFormat("$###,##0.00").format(overallRevenue));
			}
		}

		catch (ParseException ex) {
			System.out.print("\nInvalid date input! ");
			System.out.println("Failed to display sale revenue report," + " please try again..");
			System.out.println("NOTE: Date entered should" + " be in dd/mm/yyyy, e.g. 25/10/2014!");
			return;
		}

		catch (InputMismatchException ex) {
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to display sale revenue report," + " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}

		catch (Exception ex) {
			System.out.println("\nFailed to display sale revenue report," + " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}

	/*
	 * requires an input from the staff to see which month you want to display ->
	 */
	public static void displaySalesRevenueMonthly() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("MM/yyyy");
			sdf.setLenient(false);

			System.out.println("\nDisplaying sale revenue report by month..");
			System.out.print("Enter month (mm/yyyy): ");
			String saleRevenueDateStr = sc.next();

			Date saleRevenueMonth = sdf.parse(saleRevenueDateStr);
			Calendar saleRevenueCal = GregorianCalendar.getInstance();
			saleRevenueCal.setTime(saleRevenueMonth);

			Calendar currentInstant = GregorianCalendar.getInstance();
			Date currentDateTime = currentInstant.getTime();
			currentInstant.setTime(currentDateTime);
			currentInstant.set(Calendar.DAY_OF_MONTH, 0);

			if (saleRevenueCal.get(Calendar.YEAR) > currentInstant.get(Calendar.YEAR)) {
				System.out.print("\nInvalid month! ");
				System.out.println("Failed to check sale revenue report, please try again..");
				System.out.println("NOTE: You can only check sale revenue reports for previous/current month!");
				return;
			}
			if (saleRevenueCal.get(Calendar.YEAR) == currentInstant.get(Calendar.YEAR)) {
				if ((saleRevenueCal.get(Calendar.MONTH) - 1) > currentInstant.get(Calendar.MONTH)) {
					System.out.print("\nInvalid month! ");
					System.out.println("Failed to check sale revenue report, please try again..");
					System.out.println("NOTE: You can only check sale revenue reports for previous/current month!");
					return;
				}
			}

			double[] overallRevenue = new double[31];
			double totalRevenue = 0.0;

			for (Order order : completedOrders) {
				Calendar orderDateTime = order.getOrderDateTime();

				if ((orderDateTime.get(Calendar.YEAR) == saleRevenueCal.get(Calendar.YEAR))
						&& (orderDateTime.get(Calendar.MONTH) == saleRevenueCal.get(Calendar.MONTH))) {
					overallRevenue[orderDateTime.get(Calendar.DAY_OF_MONTH) - 1] += order.getNettPrice();

					totalRevenue += order.getNettPrice();
				}
			}

			SimpleDateFormat saleRevenueDateFormat;
			saleRevenueDateFormat = new SimpleDateFormat("MMMM yyyy");

			if (totalRevenue == 0.0) {
				System.out.println("\nThere are no sales made on the selected month, \""
						+ saleRevenueDateFormat.format(saleRevenueCal.getTime()) + "\"!");
				return;
			}

			int minDay = 0, maxDay = 0;
			double minDayRevenue = 1000, maxDayRevenue = 0;

			for (int currDay = 0; currDay < overallRevenue.length; currDay++) {
				if (overallRevenue[currDay] == 0)
					continue;

				if (overallRevenue[currDay] >= maxDayRevenue) {
					maxDayRevenue = overallRevenue[currDay];
					maxDay = currDay;
				}

				if (overallRevenue[currDay] <= minDayRevenue) {
					minDayRevenue = overallRevenue[currDay];
					minDay = currDay;
				}
			}

			saleRevenueDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			saleRevenueCal.set(Calendar.DAY_OF_MONTH, maxDay + 1);
			System.out.printf("%nHighest sales for the month: \"%s\"\t",
					saleRevenueDateFormat.format(saleRevenueCal.getTime()));
			System.out.printf("Revenue for the day: %s%n", new DecimalFormat("$###,##0.00").format(maxDayRevenue));

			saleRevenueCal.set(Calendar.DAY_OF_MONTH, minDay + 1);
			System.out.printf("Lowest sales for the month: \"%s\"\t",
					saleRevenueDateFormat.format(saleRevenueCal.getTime()));
			System.out.printf("Revenue for the day: %s%n", new DecimalFormat("$###,##0.00").format(minDayRevenue));

			saleRevenueDateFormat = new SimpleDateFormat("MMM yyyy");
			System.out.printf("%nTotal sales for the month \"%s\": %s%n",
					saleRevenueDateFormat.format(saleRevenueCal.getTime()),
					new DecimalFormat("$###,##0.00").format(totalRevenue));
		} catch (ParseException ex) {
			System.out.print("\nInvalid month input! ");
			System.out.println("Failed to display sale revenue report, please try again..");
			System.out.println("NOTE: Month entered should be in mm/yyyy, e.g. 10/2014!");
			return;
		} catch (InputMismatchException ex) {
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to display sale revenue report, please try again..");

			sc.nextLine();
			return;
		} catch (Exception ex) {
			System.out.println(ex.toString());
			System.out.println("\nFailed to display sale revenue report, please try again..");

			sc.nextLine();
			return;
		}
	}

}