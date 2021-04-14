package restaurant;
/*
 * Code done by: Lam Xin Yi, Jeslyn
 * */

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import entity.Table;

public class TableManager extends RRPSS {
	public static void userInterface() {
		Scanner scan = new Scanner(System.in);
		int choice;
		do {
			ReservationManager.checkReservationExpiry(reservationList, tableList);
			ReservationManager.setTablesToReservedDuringShift(reservationList, tableList);
			System.out.println("+---------------+");
			System.out.println("| Table Manager |");
			System.out.println("+---------------+");
			System.out.println("1. List All Tables");
			System.out.println("2. List Vacated Tables");
			System.out.println("3. List Occupied Tables");
			System.out.println("4. List Reserved Tables");
			System.out.println("5. Back");
			System.out.println("Your choice: ");
			choice = scan.nextInt();
			switch (choice) {
			case 1:
				listAllTables();
				break;
			case 2:
				listVacatedTables(true);
				break;
			case 3:
				listOccupiedTables(true);
				break;
			case 4:
				listReservedTables(true);
				break;
			default:
				break;
			}
		} while (choice >= 1 && choice <= 4);
	}

	public static void listAllTables() {
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			System.out.println(currentTable);
		}
		listVacatedTables(false);
		listOccupiedTables(false);
		listReservedTables(false);
	}

	public static void listVacatedTables(boolean toPrint) {
		int count = 0;
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			if (currentTable.isVacated()) {
				if (toPrint)
					System.out.println(currentTable);
				count++;
			}
		}
		System.out.println(String.format("Number of vacated tables: %d", count));
	}

	public static void listOccupiedTables(boolean toPrint) {
		int count = 0;
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			if (currentTable.isOccupied()) {
				if (toPrint)
					System.out.println(currentTable);
				count++;
			}
		}
		System.out.println(String.format("Number of occupied tables: %d", count));
	}

	public static void listReservedTables(boolean toPrint) {
		int count = 0;
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			if (currentTable.isReserved()) {
				if (toPrint)
					System.out.println(currentTable);
				count++;
			}
		}
		System.out.println(String.format("Number of reserved tables: %d", count));
	}

	public static boolean noVacatedTables() {
		int count = 0;
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			if (currentTable.isVacated()) {
				count++;
			}
		}
		return count == 0;
	}

	public static int allocateTable(int customerPax, ArrayList<Table> tableList) {
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			int tableMaxPax = currentTable.getPax();
			if (tableMaxPax >= customerPax && currentTable.isVacated())
				return currentTable.getId();
		}
		return -1;
	}

	public static int allocateTable(int customerPax) {
		Scanner scan = new Scanner(System.in);
		int tableId = -1;
		System.out.println("Automatically allocate table? [Y/N]");
		if (RRPSS.gotY()) {
			tableId = allocateTable(customerPax, tableList);
		} else {
			listVacatedTables(true);
			System.out.println("Enter tableId: ");
			tableId = validatedTableId();
		}
		return tableId;
	}

	public static int validatedTableId() {
		Scanner scan = new Scanner(System.in);
		int input = -1;
		boolean valid = false;
		do {
			try {
				input = scan.nextInt();
				for (int index = 0; index < tableList.size(); index++) {
					Table currentTable = tableList.get(index);
					if (currentTable.getId() == input) {
						valid = true;
					}
				}
				if (!valid)
					errorMessage("table id", true);
			} catch (InputMismatchException e) {
				errorMessage("input", true);
				scan.nextLine();
			}
		} while (!valid);
		return input;
	}

	public static Table getTableIndexWithId(int tableId) {
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			int currentId = currentTable.getId();
			if (currentId == tableId) {
				return currentTable;
			}
		}
		return null;
	}

	public static void setTableIdToOccupied(int tableId) {
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			int currentId = currentTable.getId();
			if (currentId == tableId)
				tableList.get(index).setOccupied();

		}
		Database.save(TABLE_DAT, tableList);
	}

	public static void setTableIdToVacated(int tableId) {
		for (int index = 0; index < tableList.size(); index++) {
			Table currentTable = tableList.get(index);
			int currentId = currentTable.getId();
			if (currentId == tableId)
				tableList.get(index).setVacated();
		}
		Database.save(TABLE_DAT, tableList);

	}

}