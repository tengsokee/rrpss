package restaurant;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import entity.Menu;
import entity.Order;
import entity.Promotion;
import entity.Reservation;
import entity.Staff;
import entity.Table;

public class RRPSS {
	// Initialisation
	public final static String RESERVATION_DAT = "reservation.dat";
	public final static String MENU_DAT = "menu.dat";
	public final static String PROMOTION_DAT = "promotion.dat";
	public final static String STAFF_DAT = "staff.dat";
	public final static String TABLE_DAT = "table.dat";
	public final static String CURRENT_ORDER_DAT = "currentO.dat";
	public final static String COMPLETED_ORDER_DAT = "completedO.dat";

	// Loading of database
	public static ArrayList<Reservation> reservationList = ((ArrayList<Reservation>) Database.read("reservation.dat"));
	public static ArrayList<Menu> menuList = (ArrayList<Menu>) Database.read("menu.dat");
	public static ArrayList<Promotion> promoList = (ArrayList<Promotion>) Database.read("promotion.dat");
	public static ArrayList<Staff> staffList = (ArrayList<Staff>) Database.read("staff.dat");
	public static ArrayList<Table> tableList = (ArrayList<Table>) Database.read("table.dat");
	public static ArrayList<Order> currentOrders = (ArrayList<Order>) Database.read("currentO.dat");
	public static ArrayList<Order> completedOrders = (ArrayList<Order>) Database.read("completedO.dat");

	// Running of restaurant
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		int choice;
		do {
			ReservationManager.checkReservationExpiry(reservationList, tableList);
			ReservationManager.setTablesToReservedDuringShift(reservationList, tableList);
			if (TableManager.noVacatedTables())
				System.out.println("No available tables, no more new orders allowed!");
			System.out.println("+-------+");
			System.out.println("| RRPSS |");
			System.out.println("+-------+");
			System.out.println("1. Order Submenu");
			System.out.println("2. View table statuses");
			System.out.println("3. Print bill invoice");
			System.out.println("4. Print sales revenue");
			System.out.println("5. Manage menu/promotion items");
			System.out.println("6. Manage reservations");
			System.out.println("7. Manage staff details");

			System.out.println("Your choice: ");
			choice = scan.nextInt();
			switch (choice) {
			case 1:
				OrderManager.getOrderChoice();
				break;
			case 2:
				TableManager.userInterface();
				break;
			case 3:
				OrderManager.makePayment();
				break;
			case 4:
				OrderManager.displaySaleRevenue();
				break;
			case 5:
				MenuManager.userInterface();
				break;
			case 6:
				ReservationManager.userInterface();
				break;
			case 7:
				StaffManager.userInterface();
				break;
			default:
				break;
			}
		} while (choice >= 1 && choice <= 7);
		System.out.println("Restaurant closed!");
	}

	// SHARED METHODS
	public static boolean gotY() {
		Scanner scan = new Scanner(System.in);
		String response = scan.nextLine();
		response = response.strip().toUpperCase();
		return response.equals("Y");
	}

	// Prints error message
	public static void errorMessage(String errorIssue, boolean tryAgain) {
		if (tryAgain)
			System.out.printf("Invalid %s, please try again: \n", errorIssue);
		else
			System.out.printf("Invalid %s! \n", errorIssue);
	}

	// Check if the pax is in validated range
	public static int validatedPax() {
		Scanner scan = new Scanner(System.in);
		int input = -1;
		boolean valid = false;
		do {
			try {
				input = scan.nextInt();
				if (input >= 1 && input <= 10)
					valid = true;
				else
					errorMessage("pax", true);
			} catch (InputMismatchException e) {
				errorMessage("input", true);
				scan.nextLine();
			}
		} while (!valid);
		return input;
	}
}
