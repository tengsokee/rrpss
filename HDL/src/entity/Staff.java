package entity;


import java.io.Serializable;

public class Staff implements Serializable {
	private String name;
	private String gender;
	private String employee_id;
	private String job_title;
	private int contact_number;

	public Staff(String n, String g, String i, String j, int cn) {
		name = n;
		gender = g;
		employee_id = i;
		job_title = j;
		contact_number = cn;
	}

	public String getName() {
		return name;
	}

	public String getGender() {
		return gender;
	}

	public String getEmployeeId() {
		return employee_id;
	}

	public String getJobTitle() {
		return job_title;
	}

	public int getContactNumber() {
		return contact_number;
	}

	public void setName(String set_name) {
		name = set_name;
	}

	public void setGender(String set_gender) {
		gender = set_gender;
	}

	public void setEmployeeId(String set_employee_id) {
		employee_id = set_employee_id;
	}

	public void setJobTitle(String set_job_title) {
		job_title = set_job_title;
	}

	public void setContactNumber(int set_contact_number) {
		contact_number = set_contact_number;
	}
}