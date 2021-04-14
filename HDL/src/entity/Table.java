package entity;

import java.io.Serializable;
public class Table implements Serializable{
	private static final String VACATED = "vacated";
	private static final String OCCUPIED = "occupied";
	private static final String RESERVED = "reserved";
	private int id;
	private int pax;
	private String status;
	public Table(int i, int p, String s) {
		id=i;
		pax=p;
		status=s;
	}
	public int getId() { return id; }
	public int getPax() { return pax; }
	public String getStatus() { return status; }
	public void setVacated() { this.status = VACATED; }
	public void setReserved() { this.status = RESERVED; }
	public void setOccupied() { this.status = OCCUPIED;	}
	public boolean isVacated() { return this.getStatus().equals(VACATED); }
	public boolean isOccupied() { return this.getStatus().equals(OCCUPIED); }
	public boolean isReserved() { return this.getStatus().equals(RESERVED); }
	public String toString() {
		return String.format("Table - Id: %2d | Pax: %2d | Status: %s", this.getId(), this.getPax(), this.getStatus());
	}
}
