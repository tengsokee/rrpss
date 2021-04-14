package entity;


import java.io.Serializable;
import java.util.ArrayList;

public class Promotion extends Menu implements Serializable {
	private ArrayList<?> menuObj;

	public ArrayList<?> getmenuObjs() {
		return menuObj;
	}

	public Promotion(String pname, double pprice, ArrayList<?> menuobjs) {
		super(pname, pprice, "", "");
		this.menuObj = menuobjs;
	}

	public void setPromo(String pname, double pprice, ArrayList<?> tempList) {
		this.setName(pname);
		this.setPrice(pprice);
		this.menuObj = tempList;
	}
}
