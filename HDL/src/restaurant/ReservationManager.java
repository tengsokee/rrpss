package restaurant;

/*
 * Code done by: Lam Xin Yi, Jeslyn
 * */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import entity.Reservation;
import entity.Table;

public class ReservationManager extends RRPSS {
	public static void userInterface() {
		Scanner scan = new Scanner(System.in);
		int choice;
		do {
			checkReservationExpiry(reservationList, tableList);
			setTablesToReservedDuringShift(reservationList, tableList);
			System.out.println("+---------------------+");
			System.out.println("| Reservation Manager |");
			System.out.println("+---------------------+");
			System.out.println("1. Create Reservation");
			System.out.println("2. Remove Reservation");
			System.out.println("3. List All Reservations");
			System.out.println("4. Search for Reservations");
			System.out.println("5. Check In Reservation Guest");
			System.out.println("6. Back");
			System.out.println("Your choice: ");
			choice = scan.nextInt();
			switch (choice) {
			case 1:
				createReservation();
				break;
			case 2:
				removeReservation();
				break;
			case 3:
				listAllReservation();
				break;
			case 4:
				searchReservation();
				break;
			case 5:
				checkInReservation();
				break;
			default:
				break;
			}
		} while (choice >= 1 && choice <= 5);
	}

	public static void createReservation() {
		Scanner scan = new Scanner(System.in);
		final String DATE_FORMAT = "yyyy/MM/dd";
		final String TIME_FORMAT = "HH:mm";
		boolean errorExist = true;
		int contactNumber = -1;
		int pax = -1;
		String name = null;
		String date = null;
		String time = null;
		while (errorExist) {
			try {
				System.out.println("Enter the name of guest: ");
				name = scan.nextLine().toUpperCase();
				System.out.println("Enter the contact number of guest: ");
				contactNumber = validatedContactNumber();
				System.out.println("Enter the number of guest(s) coming: ");
				pax = validatedPax();
				System.out.println("Enter the date of reservation (yyyy/MM/dd): ");
				date = validatedDateTime(DATE_FORMAT);
				System.out.println("Enter the time of reservation (HH:mm): ");
				time = validatedDateTime(TIME_FORMAT);
				errorExist = false;
			} catch (Exception e) {
				errorMessage("input", true);
			}
		} // All information captured
		String[] dateList = date.split("/");
		String[] timeList = time.split(":");
		LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(dateList[0]), Integer.parseInt(dateList[1]),
				Integer.parseInt(dateList[2]), Integer.parseInt(timeList[0]), Integer.parseInt(timeList[1]));
		if (isInvalid(dateTime)) {
			return;
		} else {
			int tableId = TableManager.allocateTable(pax, tableList);
			if (tableId != -1) {
				Reservation reservation = new Reservation(dateTime, pax, name, contactNumber, tableId);
				reservationList.add(reservation);
				Database.save(RESERVATION_DAT, reservationList);
				System.out.println("Reservation has been added!");
			} else {
				errorMessage("table allocation, no table available", false);
			}
		}
	}

	public static void removeReservation() {
		listAllReservation();
		try {
			System.out.println("Enter the contact number for the reservation to remove: ");
			int contactNumber = validatedContactNumber();
			ArrayList<Integer> indexList = filterByContactNumber(reservationList, contactNumber);
			if (indexList.size() == 0) {
				errorMessage("contact number, no reservations found", false);
				return;
			}
			System.out.println("Reservation(s) found: ");
			for (int index = 0; index < indexList.size(); index++) {
				int savedInt = indexList.get(index);
				System.out.println(reservationList.get(savedInt));
				System.out.println("Enter [y] to remove reservation: ");
				if (gotY()) {
					reservationList.remove(savedInt);
				}
			}
			Database.save(RESERVATION_DAT, reservationList);
		} catch (IndexOutOfBoundsException e) {
			// If this exception appears, the list of reservations has been all deleted
			reservationList = null;
			Database.save(RESERVATION_DAT, reservationList);
		}
		System.out.println("Reservations removed!");
	}

	public static void listAllReservation() {
		if (reservationList.size() >= 1)
			for (int index = 0; index < reservationList.size(); index++)
				System.out.println(reservationList.get(index));
		else
			System.out.println("No reservations for now!");
	}

	public static void searchReservation() {
		System.out.println("Enter the contact number to search for: ");
		int contactNumber = validatedContactNumber();
		ArrayList<Integer> indexList = filterByContactNumber(reservationList, contactNumber);
		if (indexList.size() == 0) {
			System.out.println("No reservations found.");
			return;
		}
		System.out.println("Reservation(s) found: ");
		for (int index = 0; index < indexList.size(); index++) {
			int savedInt = indexList.get(index);
			System.out.println(reservationList.get(savedInt));
		}
	}

	public static void checkInReservation() {
		System.out.println("Enter the contact number for check in: ");
		int contactNumber = validatedContactNumber();
		ArrayList<Integer> indexList = filterByContactNumber(reservationList, contactNumber);
		if (indexList.size() == 0) {
			System.out.println("No reservations found.");
			return;
		}
		for (int index = 0; index < indexList.size(); index++) {
			int savedInt = indexList.get(index);
			Reservation currentReservation = reservationList.get(savedInt);
			int currentTableId = currentReservation.getTableId();
			String currentStatus = tableList.get(currentTableId).getStatus();
			if (currentStatus.equals("reserved")) {
				currentReservation.setIsCheckedIn(true);
			}
		}
		Database.save(TABLE_DAT, tableList);
		Database.save(RESERVATION_DAT, reservationList);
		System.out.println("Guest has been checked in!");
	}

// ==================================================================================
// Static methods
// ==================================================================================

	public static String validatedDateTime(final String FORMAT) {
		Scanner scan = new Scanner(System.in);
		String input = "nil";
		boolean valid = false;
		do {
			try {
				input = scan.nextLine();
				DateFormat dateTimeChecker = new SimpleDateFormat(FORMAT);
				dateTimeChecker.setLenient(false);
				dateTimeChecker.parse(input);
				valid = true;
			} catch (ParseException e) {
				errorMessage("date/time", true);
				scan.nextLine();
			}
		} while (!valid);
		return input;
	}

	public static int validatedContactNumber() {
		Scanner scan = new Scanner(System.in);
		int input = -1;
		boolean valid = false;
		do {
			try {
				input = scan.nextInt();
				if (input >= 80000000 && input <= 99999999)
					valid = true;
				else
					errorMessage("contact number", true);
			} catch (InputMismatchException e) {
				errorMessage("input", true);
				scan.nextLine();
			}
		} while (!valid);
		return input;
	}

	public static boolean isInvalid(LocalDateTime setTime) {
		final int MONTH = 1;
		final int MINUTE = 15;
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime maxTime = LocalDateTime.now().plusMonths(MONTH);
		String shift = checkCurrentShift(setTime.toLocalTime());
		if (shift == null) {
			errorMessage("timing, restautant not open then", false);
			return true;
		} else if (setTime.isAfter(maxTime)) {
			errorMessage("date, reservations can only be made one month in advance", false);
			return true;
		} else if (currentTime.isAfter(setTime.plusMinutes(MINUTE))) {
			errorMessage(
					"reservation detected as guest is late, reservation has been automaticallyremoved, they can down 20",
					false);
			return true;
		} else
			return false;
	}

	public static ArrayList<Integer> filterByContactNumber(ArrayList<Reservation> reservationList, int contactNumber) {
		ArrayList<Integer> filteredList = new ArrayList<Integer>();
		for (int index = 0; index < reservationList.size(); index++) {
			int reservationNumber = reservationList.get(index).getContactNumber();
			if (reservationNumber == contactNumber) {
				filteredList.add(index);
			}
		}
		return filteredList;
	}

	public static String checkCurrentShift(LocalTime timeToCheck) {
		LocalTime amStart = LocalTime.parse("11:00");
		LocalTime amEnd = LocalTime.parse("15:00");
		LocalTime pmStart = LocalTime.parse("18:00");
		LocalTime pmEnd = LocalTime.parse("22:00");
		if (timeToCheck.isAfter(amStart) && timeToCheck.isBefore(amEnd))
			return "AM";
		else if (timeToCheck.isAfter(pmStart) && timeToCheck.isBefore(pmEnd))
			return "PM";
		else
			return null;
	}

	public static void setTablesToReservedDuringShift(ArrayList<Reservation> reservationList,
			ArrayList<Table> tableList) {
		LocalDate currentDate = LocalDate.now();
		String currentShift = checkCurrentShift(LocalTime.now());
		for (int index = 0; index < reservationList.size(); index++) {
			Reservation currentReservation = reservationList.get(index);
			LocalDate reservationDate = currentReservation.getLocalDate();
			if (currentDate.isEqual(reservationDate) && currentShift.equals(currentReservation.getShift())) {
				int tableId = currentReservation.getTableId();
				tableList.get(tableId).setReserved();
			}
		}
		Database.save(TABLE_DAT, tableList);
		Database.save(RESERVATION_DAT, reservationList);
	}

	public static void checkReservationExpiry(ArrayList<Reservation> reservationList, ArrayList<Table> tableList) {
		for (int index = 0; index < reservationList.size(); index++) {
			Reservation currentReservation = reservationList.get(index);
			LocalDateTime currentTime = currentReservation.getLocalDateTime();
			boolean currentIsCheckedIn = currentReservation.getIsCheckedIn();
			if (isInvalid(currentTime) && !currentIsCheckedIn) {
				int tableId = currentReservation.getTableId();
				tableList.get(tableId).setVacated();
				reservationList.remove(index);
			}
		}
		Database.save(TABLE_DAT, tableList);
		Database.save(RESERVATION_DAT, reservationList);
	}

}