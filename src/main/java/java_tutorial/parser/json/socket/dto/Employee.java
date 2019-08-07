package java_tutorial.parser.json.socket.dto;

import java.util.Date;

public class Employee {
	private int eNo;
	private String eName;
	private int salary;
	private Department dno;
	private boolean gender;
	private Date joinDate;
	private Title title;

	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public Employee(int eNo, String eName) {
		this.eNo = eNo;
		this.eName = eName;
	}

	public Employee(int eNo, String eName, int salary, Department dno, boolean gender, Date joinDate, Title title) {
		this.eNo = eNo;
		this.eName = eName;
		this.salary = salary;
		this.dno = dno;
		this.gender = gender;
		this.joinDate = joinDate;
		this.title = title;
	}

	public int geteNo() {
		return eNo;
	}

	public void seteNo(int eNo) {
		this.eNo = eNo;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public Department getDno() {
		return dno;
	}

	public void setDno(Department dno) {
		this.dno = dno;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	
	@Override
	public String toString() {
		return String.format("[%s, %s, %s, %s, %s, %s, %s]", 
				eNo, 
				eName,
				String.format("%,d", salary),
				String.format("%s(%sÏ∏?)", dno.getDeptName(), dno.getFloor()), 
				gender ? "?Ç®?ûê" : "?ó¨?ûê",
				String.format("%tF", joinDate),
				String.format("%s(%d)", title.getTitleName(),title.getTitleCode()));
	}

	public Object[] toArray() {
		return new Object[] { 
				String.format("E%06d", eNo), 
				eName, 
				String.format("%,d", salary),
				String.format("%s(%sÏ∏?)", dno.getDeptName(), dno.getFloor()), 
				gender ? "?Ç®?ûê" : "?ó¨?ûê",
				String.format("%tF", joinDate),
				String.format("%s(%d)", title.getTitleName(),title.getTitleCode()),
		};
	}
}
