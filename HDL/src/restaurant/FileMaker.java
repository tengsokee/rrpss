package restaurant;

import java.util.ArrayList;

import entity.Menu;
import entity.Order;
import entity.Promotion;
import entity.Reservation;
import entity.Staff;
import entity.Table;

public class FileMaker {
	public static void main(String[] args) {
		ArrayList<Table> tableList = new ArrayList<Table>();
		tableList.add(new Table(20, 2, "vacated"));
		tableList.add(new Table(21, 2, "vacated"));
		tableList.add(new Table(22, 2, "vacated"));
		tableList.add(new Table(23, 2, "vacated"));
		tableList.add(new Table(24, 2, "vacated"));
		tableList.add(new Table(25, 2, "vacated"));
		tableList.add(new Table(26, 2, "vacated"));
		tableList.add(new Table(27, 2, "vacated"));
		tableList.add(new Table(28, 2, "vacated"));
		tableList.add(new Table(29, 2, "vacated"));
		tableList.add(new Table(40, 4, "vacated"));
		tableList.add(new Table(41, 4, "vacated"));
		tableList.add(new Table(42, 4, "vacated"));
		tableList.add(new Table(43, 4, "vacated"));
		tableList.add(new Table(44, 4, "vacated"));
		tableList.add(new Table(45, 4, "vacated"));
		tableList.add(new Table(46, 4, "vacated"));
		tableList.add(new Table(47, 4, "vacated"));
		tableList.add(new Table(48, 4, "vacated"));
		tableList.add(new Table(49, 4, "vacated"));
		tableList.add(new Table(80, 8, "vacated"));
		tableList.add(new Table(81, 8, "vacated"));
		tableList.add(new Table(82, 8, "vacated"));
		tableList.add(new Table(83, 8, "vacated"));
		tableList.add(new Table(84, 8, "vacated"));
		tableList.add(new Table(10, 10, "vacated"));
		tableList.add(new Table(11, 10, "vacated"));
		tableList.add(new Table(12, 10, "vacated"));
		tableList.add(new Table(13, 10, "vacated"));
		tableList.add(new Table(14, 10, "vacated"));
		Database.save("table.dat", tableList);

		ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
		Database.save("reservation.dat", reservationList);

		ArrayList<Staff> staffList = new ArrayList<Staff>();
		staffList.add(new Staff("Sok Ee", "female", "1", "employee", 91111111));
		staffList.add(new Staff("Jeslyn", "female", "2", "employee", 92222222));
		staffList.add(new Staff("Hu Soon", "male", "3", "employee", 93333333));
		staffList.add(new Staff("Vivien", "female", "4", "employee", 94444444));
		staffList.add(new Staff("Jainil", "male", "5", "employee", 95555555));
		Database.save("staff.dat", staffList);

		ArrayList<Menu> menuList = new ArrayList<Menu>();
		menuList.add(new Menu("Crab", 26, "Freshly imported from Japan", "Seafood"));
		menuList.add(new Menu("Scallops", 12, "Sweet scallops imported from Japan", "Seafood"));
		menuList.add(new Menu("Kelp", 6, "Crunchy texture", "Vegetable"));
		menuList.add(new Menu("Potato Slices", 4, "Thinly slices", "Vegetable"));
		menuList.add(new Menu("Cabbage", 6, "Crunchy sweet texture", "Vegetable"));
		menuList.add(new Menu("Spinach", 6, "Crunchy veggies", "Vegetable"));
		menuList.add(new Menu("Tofu", 6, "Freshly chilled", "Others"));
		menuList.add(new Menu("Pork Neck", 12, "200g worth", "Meat"));
		menuList.add(new Menu("Waygu Beef", 56, "200g worth", "Meat"));
		menuList.add(new Menu("US Beef Slices", 18, "300g worth", "Meat"));
		menuList.add(
				new Menu("Chicken Soup Hotpot", 22, "Creamy silky base with silken tofu, boiled for 24HR", "SoupBase"));
		menuList.add(new Menu("Mushroom Soup Hotpot", 18, "Clear base with 5 variety of mushrooms", "SoupBase"));
		menuList.add(new Menu("Sichuan spicy Soup Hotpot", 18, "Mala spicy with herbs & black peppercorn", "SoupBase"));
		menuList.add(new Menu("Tomato Soup Hotpot", 14, "Tangy tomato flavour", "SoupBase"));
		menuList.add(new Menu("Yuzu Sorbet", 10, "Yuzu-flavoured ice sorbet", "Dessert"));
		menuList.add(new Menu("Banana split", 12, "Banana split with 3 different flavours of Ice-Cream", "Dessert"));
		menuList.add(new Menu("Cola Cola", 3, "300ML", "Beverages"));
		menuList.add(new Menu("Sprite", 3, "300ML", "Beverages"));
		menuList.add(new Menu("Mineral Water", 3, "1 Bottle", "Beverages"));
		Database.save("menu.dat", menuList);

		Menu firstSetOne = new Menu("Waygu Beef", 56, "200g worth", "Meat");
		Menu firstSetTwo = new Menu("Pork Neck", 12, "200g worth", "Meat");
		Menu firstSetThree = new Menu("Chicken Soup Hotpot", 22, "Creamy silky base with silken tofu, boiled for 24HR",
				"SoupBase");
		ArrayList<Menu> firstSet = new ArrayList<Menu>();
		firstSet.add(firstSetOne);
		firstSet.add(firstSetTwo);
		firstSet.add(firstSetThree);

		Menu secondSetOne = new Menu("Crab", 26, "Freshly imported from Japan", "Seafood");
		Menu secondSetTwo = new Menu("Scallops", 12, "Sweet scallops imported from Japan", "Seafood");
		Menu secondSetThree = new Menu("Kelp", 6, "Crunchy texture", "Vegetable");
		ArrayList<Menu> secondSet = new ArrayList<Menu>();
		secondSet.add(secondSetOne);
		secondSet.add(secondSetTwo);
		secondSet.add(secondSetThree);

		ArrayList<Promotion> promoList = new ArrayList<Promotion>();
		promoList.add(new Promotion("Meat Meat", 50, firstSet));
		promoList.add(new Promotion("Sea Sea", 35, secondSet));
		Database.save("promotion.dat", promoList);

		ArrayList<Order> currentOrders = new ArrayList<Order>();
		Database.save("currentO.dat", currentOrders);

		ArrayList<Order> completedOrders = new ArrayList<Order>();
		Database.save("completedO.dat", completedOrders);

	}
}