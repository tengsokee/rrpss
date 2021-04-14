package restaurant;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import entity.Staff;

public class StaffManager {
	private static ArrayList<Staff> staffList = (ArrayList<Staff>) Database.read("staff.dat");

	public static void main(String[] args) {
		userInterface();
	}

	public static void userInterface() {
		Scanner scan = new Scanner(System.in);
		int choice;
		do {
			System.out.println("+---------------------+");
			System.out.println("| Staff Manager |");
			System.out.println("+---------------------+");
			System.out.println("1. Create Staff");
			System.out.println("2. List all Staff");
			System.out.println("3. Remove Staff");
			System.out.println("4. Back");
			System.out.println("Your choice: ");
			choice = scan.nextInt();
			switch (choice) {
			case 1:
				CreateStaff();
				break;
			case 2:
				ListStaff();
				break;
			case 3:
				ListStaff();
				RemoveStaff();
				break;
			default:
				break;
			}
		} while (choice >= 1 && choice <= 3);
	}

	// Class to create staff
	private static void CreateStaff() {
		Scanner scan = new Scanner(System.in);

		// variables
		boolean flagname = true;
		boolean flaggender = true;
		boolean flagjobtitle = true;
		boolean flagcontactnumber = true;
		String staff_name = null;
		String gender = null;
		String job_title = null;
		int contact_number = 0;

		// while loops to take user inputs
		while (flagname) {
			try {
				System.out.println("Please enter staff name: ");
				staff_name = scan.nextLine();
				// Check if name is already in database
				for (int i = 0; i < staffList.size(); i++) {
					Staff s = staffList.get(i);
					String namestaff = s.getName();
					// If name is found, prompt user to re-enter a new name
					if (staff_name.equals(namestaff)) {
						System.out.println("Name is found in the database.");
						System.out.println("Please enter staff name: ");
						staff_name = scan.nextLine();
					}
				}
				flagname = false;
			} catch (InputMismatchException e) {
				System.out.println("You have entered invalid name.");
				System.out.println("Please enter a valid name.");
			}
		}

		while (flaggender) {
			try {
				System.out.println("Please enter your gender: ");
				gender = scan.nextLine();
				flaggender = false;

			} catch (InputMismatchException e) {
				System.out.println("You have entered invalid data");
				System.out.println("Please enter a valid gender.");
			}
		}
		while (flagjobtitle) {
			try {
				System.out.println("Please enter your job title: ");
				job_title = scan.nextLine();
				flagjobtitle = false;

			} catch (InputMismatchException e) {
				System.out.println("You have entered invalid data");
				System.out.println("Please enter a valid job title.");
			}
		}
		do {
			try {
				System.out.println("Please enter your contact number: ");
				contact_number = scan.nextInt();
				// Check if users phone number is valid or not
				if (contact_number >= 80000000 && contact_number <= 99999999)
					flagcontactnumber = false;

			} catch (InputMismatchException e) {
				System.out.println("You have entered invalid data");
				System.out.println("Please enter a phone number.");
			}
		} while (flagcontactnumber);

		// Add new staff to the database

		Staff create = staffList.get(staffList.size() - 1);
		int idstaff = Integer.parseInt(create.getEmployeeId()) + 1;
		staffList.add(new Staff(staff_name, gender, Integer.toString(idstaff), job_title, contact_number));
		Database.save("staff.dat", staffList);
	}

	private static void ListStaff() {
		System.out.println("List of staff");
		int i = 0;
		do {
			Staff s = staffList.get(i);
			System.out.println("_____________________");
			System.out.println("Staff name: " + s.getName());
			System.out.println("Gender: " + s.getGender());
			System.out.println("Staff Id: " + s.getEmployeeId());
			System.out.println("Job Title: " + s.getJobTitle());
			System.out.println("Contact Number: " + s.getContactNumber());
			System.out.println("_____________________");
			i++;
		} while (i < staffList.size());
	}

	// Class to remove existing staff
	private static void RemoveStaff() {
		Scanner scan = new Scanner(System.in);
		int idchoice = 0;
		try {
			System.out.println("Please enter the employee's employee id you would like to remove: ");
			idchoice = scan.nextInt();

		} catch (InputMismatchException e) {
			System.out.println("input is not an int value");
			System.out.println("Please enter a valid employee id.");
			// Here catch NumberFormatException
			// So input is not a int.
		}

		// Remove staff from the database
		for (int i = 0; i < staffList.size(); i++) {
			Staff s = staffList.get(i);
			int idstaff = Integer.parseInt(s.getEmployeeId());
			if (idstaff == idchoice) {
				staffList.remove(i);
				Database.save("staff.dat", staffList);
			}
		}
	}

	public static int inputStaffId() {
		Scanner sc = new Scanner(System.in);
		int id = 0;
		Staff lastid = staffList.get(staffList.size() - 1);
		int maxstaffid = Integer.parseInt(lastid.getEmployeeId()) + 1;
		do {
			try {
				System.out.println("Please enter your staff id: ");
				id = sc.nextInt();
				// You can use this method to convert String to int, But if input
				// is not an int value then this will throws NumberFormatException.
			} catch (InputMismatchException e) {
				System.out.println("input is not an int value");
				System.out.println("Please enter a valid choice.");
				// Here catch NumberFormatException
				// So input is not a int.
			}
		} while (id > maxstaffid);
		return id;
	}
}
