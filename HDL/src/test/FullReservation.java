package test;

import java.util.ArrayList;

import entity.Table;
import restaurant.Database;
import restaurant.RRPSS;

public class FullReservation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Table> tableList = new ArrayList<Table>();
		tableList.add(new Table(20, 2, "reserved"));
		tableList.add(new Table(21, 2, "reserved"));
		tableList.add(new Table(22, 2, "reserved"));
		tableList.add(new Table(23, 2, "reserved"));
		tableList.add(new Table(24, 2, "reserved"));
		tableList.add(new Table(25, 2, "reserved"));
		tableList.add(new Table(26, 2, "reserved"));
		tableList.add(new Table(27, 2, "reserved"));
		tableList.add(new Table(28, 2, "reserved"));
		tableList.add(new Table(29, 2, "reserved"));
		tableList.add(new Table(40, 4, "reserved"));
		tableList.add(new Table(41, 4, "reserved"));
		tableList.add(new Table(42, 4, "reserved"));
		tableList.add(new Table(43, 4, "reserved"));
		tableList.add(new Table(44, 4, "reserved"));
		tableList.add(new Table(45, 4, "reserved"));
		tableList.add(new Table(46, 4, "reserved"));
		tableList.add(new Table(47, 4, "reserved"));
		tableList.add(new Table(48, 4, "reserved"));
		tableList.add(new Table(49, 4, "reserved"));
		tableList.add(new Table(80, 8, "reserved"));
		tableList.add(new Table(81, 8, "reserved"));
		tableList.add(new Table(82, 8, "reserved"));
		tableList.add(new Table(83, 8, "reserved"));
		tableList.add(new Table(84, 8, "reserved"));
		tableList.add(new Table(10, 10, "reserved"));
		tableList.add(new Table(11, 10, "reserved"));
		tableList.add(new Table(12, 10, "reserved"));
		tableList.add(new Table(13, 10, "reserved"));
		tableList.add(new Table(14, 10, "reserved"));
		Database.save("table.dat", tableList);
		RRPSS.main(null);
	}

}
