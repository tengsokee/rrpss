package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import entity.Menu;
import entity.Promotion;

public class MenuManager extends RRPSS {

	private static String mfilename = "menu.dat";
	private static String pfilename = "promotion.dat";
	private static ArrayList<Menu> almenu = (ArrayList) Database.read(mfilename);
	private static ArrayList<Promotion> alpromo = (ArrayList) Database.read(pfilename);

	public static void userInterface() {
		Scanner sc = new Scanner(System.in);
		int choice;
		do {
			System.out.println("+---------------+");
			System.out.println("| Menu Manager |");
			System.out.println("+---------------+");
			System.out.println("1. Create Menu Item");
			System.out.println("2. Update Menu Item");
			System.out.println("3. Remove Menu Item");
			System.out.println("4. Create Promotion Item");
			System.out.println("5. Update Promotion Item");
			System.out.println("6. Remove Promotion Item");
			System.out.println("7. Print Ala-Carte Menu");
			System.out.println("8. Print Promotion Menu");
			System.out.println("Your choice: ");
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				CreateItem();
				break;
			case 2:
				UpdateItem();
				break;
			case 3:
				RemoveItem();
				break;
			case 4:
				CreatePromo();
				break;
			case 5:
				UpdatePromo();
				break;
			case 6:
				RemovePromo();
				break;
			case 7:
				printMenu(true);
				break;
			case 8:
				printMenu(false);
				break;
			default:
				break;
			}
		} while (choice >= 1 && choice <= 8);
	}

	// MENU
	// sorting by category
	public static Comparator<Menu> categoryComparator = new Comparator<Menu>() {
		@Override
		public int compare(Menu item1, Menu item2) {
			return (item1.getCategory().compareTo(item2.getCategory()));
		}
	};

	public static ArrayList<Menu> getSortedMenuByCategory(ArrayList al) {
		Collections.sort(al, MenuManager.categoryComparator);
		return al;
	}

	public static void printMenu(boolean option) {
		if (option == true) {
			ArrayList al = getSortedMenuByCategory(almenu);
			for (int i = 0; i < al.size(); i++) {
				Menu m = (Menu) al.get(i);
				System.out.println("(" + (i + 1) + ")" + "Name: " + m.getName());
				System.out.println("Price: " + m.getPrice());
				System.out.println("Description: " + m.getDescription());
				System.out.println("Category: " + m.getCategory() + "\n");
			}
		} else {
			for (int i = 0; i < alpromo.size(); i++) {
				Promotion p = alpromo.get(i);
				System.out.println("(" + (i + 1) + ") " + p.getName().toUpperCase());
				printPromotionItem(p);
				// System.out.println("("+ (j+1) + ")" +
				// m.getName()+"--------"+m.getDescription());
			}
		}
	}

	public static void printPromotionItem(Promotion p) {
		for (int j = 0; j < p.getmenuObjs().size(); j++) {
			Menu m = new Menu();
			m = (Menu) p.getmenuObjs().get(j);
			System.out.printf("%d. %-20s %20s %n", j + 1, m.getName(), m.getDescription());
		}
		System.out.println("Price: $" + p.getPrice() + "\n");
	}

	public static void displayMenuNames() {
		for (int i = 0; i < almenu.size(); i++) {
			Menu m = almenu.get(i);
			System.out.println("(" + (i + 1) + ")" + "Name: " + m.getName());
		}

	}

	public static String setCategory() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter choice of Category of item: ");
		System.out.println("1. Soup Base\r\n" + "2. Meat\r\n" + "3. Vegetable\r\n" + "4. Seafood\r\n"
				+ "5. Beverages\r\n" + "6. Dessert\r\n");
		int choice = sc.nextInt();

		switch (choice) {
		case 1:
			return "SoupBase";
		case 2:
			return "Meat";
		case 3:
			return "Vegetable";
		case 4:
			return "Seafood";
		case 5:
			return "Beverages";
		case 6:
			return "Dessert";
		case 7:
			return "Others";
		default:
			return null;
		}
	}

	public static void CreateItem() {
		Scanner sc = new Scanner(System.in);
		System.out.println("-------------Creating New Item-------------");
		System.out.println("Enter Name of item: ");
		String name = sc.nextLine();
		System.out.println("Enter Price of item: ");
		double price = sc.nextDouble();
		sc.nextLine();
		System.out.println("Enter Description of item: ");
		String description = sc.nextLine();
		String category = setCategory();
		Menu p1 = new Menu(name, price, description, category);
		// al is an array list containing Menu obj
		almenu.add(p1);
		// write Menu record/s to file.
		Database.save(mfilename, almenu);
		System.out.println("Item " + name + " is successfully added!\n");
	}

	public static void UpdateItem() {
		Scanner sc = new Scanner(System.in);
		displayMenuNames();
		System.out.println("-------------Updating Item-------------");
		System.out.println("Enter item number to update: ");
		int n = sc.nextInt();
		sc.nextLine();

		Menu m = almenu.get(n - 1);
		System.out.println(m.getName());
		System.out.println("Enter updated Name of item (" + m.getName() + "): ");
		String name = sc.nextLine();
		System.out.println("Enter updated Price of item: ");
		double price = sc.nextDouble();
		sc.nextLine();
		System.out.println("Enter updated Description of item: ");
		String description = sc.nextLine();
		almenu.get(n - 1).setItem(name, price, description, setCategory());
		System.out.println(almenu.get(n - 1).getName() + " is updated!\n");
		Database.save(mfilename, almenu);
	}

	public static void RemoveItem() {
		Scanner sc = new Scanner(System.in);
		displayMenuNames();
		System.out.println("-------------Removing Item-------------");
		System.out.println("Enter item number to remove: ");
		int n = sc.nextInt();
		sc.nextLine();
		System.out.println(almenu.get(n - 1).getName() + " is removed!\n");
		almenu.remove(n - 1);
		Database.save(mfilename, almenu);
	}

	// PROMOTION
	public static void CreatePromo() {
		Scanner sc = new Scanner(System.in);
		ArrayList<Menu> tempList = new ArrayList<Menu>();
		int choice = 0;
		System.out.println("-------------Creating New Promotion Set-------------");
		System.out.println("Enter Name of Promotion Set: ");
		String Pname = sc.nextLine();
		do {
			displayMenuNames();
			System.out.println("Choose Menu Item to include in the set: ");
			System.out.println("----------------------------------------");
			int menuChoice = sc.nextInt();
			Menu menuItem = (almenu.get(menuChoice - 1));
			tempList.add(menuItem);

			System.out.println("(1) Add more items in this set \n" + "(2) Set Price for Promotion set");
			choice = sc.nextInt();

		} while (choice == 1);

		System.out.println("Enter price of " + Pname + ":");
		double price = sc.nextDouble();
		// ADD SET TO PROMO DAT
		Promotion promo = new Promotion(Pname, price, tempList);
		alpromo.add(promo);
		Database.save(pfilename, alpromo);
		System.out.println(Pname + " promotion successfully added!\n");
	}

	public static void UpdatePromo() {
		Scanner sc = new Scanner(System.in);
		printMenu(false);
		System.out.println("-------------Updating Promotion Set-------------");
		System.out.println("Enter promotion number to update: ");
		int n = sc.nextInt();
		sc.nextLine();
		Promotion p = alpromo.get(n - 1);
		System.out.println("Enter updated Name of Promotion " + p.getName() + ": ");
		String name = sc.nextLine();
		System.out.println("Enter updated Price of Promotion Set: ");
		double price = sc.nextDouble();
		p.setName(name);
		p.setPrice(price);
		Database.save(pfilename, alpromo);
		System.out.println(p.getName() + " is updated!\n");

	}

	public static void RemovePromo() {
		Scanner sc = new Scanner(System.in);
		printMenu(false);
		System.out.println("-------------Removing Promotion Set-------------");
		System.out.println("Enter promotion number to remove: ");
		int n = sc.nextInt();
		sc.nextLine();
		System.out.println(alpromo.get(n - 1).getName() + " is removed!\n");
		alpromo.remove(n - 1);
		Database.save(pfilename, alpromo);
	}

	// FOR ORDER CLASS
	public static Menu selectedItem() {
		Scanner sc = new Scanner(System.in);
		List l = returnCombineMenu();
		for (int i = 0; i < l.size(); i++) {
			Menu m = (Menu) l.get(i);
			System.out.println("************************");
			System.out.println((i + 1) + ") " + m.getName());
		}
		System.out.print("Enter item choice (Press -1 to exit): ");
		int selection = sc.nextInt();
		if (selection == -1) {
			return null;
		}
		return (Menu) l.get(selection - 1);
	}

	public static List returnCombineMenu() {
		List combine = new ArrayList();
		combine.addAll(almenu);
		combine.addAll(alpromo);
		return combine;
	}
}