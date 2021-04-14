package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import restaurant.ReservationManager;

public class Reservation implements Serializable {
	// add static numofreservations
	private LocalDateTime dateTime;
	private LocalDate date;
	private LocalTime time;
	private int pax;
	private String name;
	private int contactNumber;
	private int tableId;
	private String shift;
	private boolean isCheckedIn;

	public Reservation(LocalDateTime dateTime, int pax, String name, int contactNumber, int tableId) {
		this.setLocalDateTime(dateTime);
		this.setPax(pax);
		this.setName(name);
		this.setContactNumber(contactNumber);
		this.setTableId(tableId);
		this.setLocalDate(dateTime.toLocalDate());
		this.setLocalTime(dateTime.toLocalTime());
		this.setShift(ReservationManager.checkCurrentShift(this.getLocalTime()));
		this.setIsCheckedIn(false);
	}

	public LocalDateTime getLocalDateTime() {
		return this.dateTime;
	}

	public LocalDate getLocalDate() {
		return this.date;
	}

	public LocalTime getLocalTime() {
		return this.time;
	}

	public int getPax() {
		return this.pax;
	}

	public String getName() {
		return this.name;
	}

	public int getContactNumber() {
		return this.contactNumber;
	}

	public int getTableId() {
		return this.tableId;
	}

	public String getShift() {
		return this.shift;
	}

	public boolean getIsCheckedIn() {
		return this.isCheckedIn;
	}

	public void setLocalDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public void setLocalDate(LocalDate date) {
		this.date = date;
	}

	public void setLocalTime(LocalTime time) {
		this.time = time;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContactNumber(int contactNumber) {
		this.contactNumber = contactNumber;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public void setIsCheckedIn(boolean isCheckedIn) {
		this.isCheckedIn = isCheckedIn;
	}

	@Override
	public String toString() {
		return String.format(
				"Reservation - Day: %s | Time: %s | Pax: %2d | Name: %10s | Contact Number: %d | Table Id: %2d | Shift: %s",
				this.getLocalDate(), this.getLocalTime(), this.getPax(), this.getName(), this.getContactNumber(),
				this.getTableId(), this.getShift());
	}
}