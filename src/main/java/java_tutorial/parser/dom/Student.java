package java_tutorial.parser.dom;

public class Student {
	private int rollno;
	private String firstName;
	private String lastName;
	private String nickName;
	private int grade;

	public Student() {
		// TODO Auto-generated constructor stub
	}

	public Student(int rollno, String firstName, String lastName, String nickName, int grade) {
		this.rollno = rollno;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.grade = grade;
	}

	public int getRollno() {
		return rollno;
	}

	public void setRollno(int rollno) {
		this.rollno = rollno;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return String.format("Student [rollno=%s, firstName=%s, lastName=%s, nickName=%s, grade=%s]", rollno, firstName,
				lastName, nickName, grade);
	}

}
