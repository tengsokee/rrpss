package test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import entity.Reservation;
import restaurant.Database;
import restaurant.RRPSS;
import restaurant.ReservationManager;

public class RemoveReservationUponExpiry extends RRPSS {
	public static void main(String[] args) {
		String name = "Mr Late";
		int pax = -1;
		int contactNumber = -1;
		int tableId = 10;
		LocalDateTime lateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(12, 30));
		System.out.println(
				"MESSAGE: Creating late reservation, bypassing edge case filtering thus invalid data gets initialised");
		Reservation reservation = new Reservation(lateTime, pax, name, contactNumber, tableId);
		System.out.println(reservation);
		reservationList.add(reservation);
		Database.save("reservation.dat", reservationList);
		System.out.println("MESSAGE: Detecting late reservations");
		ReservationManager.checkReservationExpiry(reservationList, tableList);
	}
}
