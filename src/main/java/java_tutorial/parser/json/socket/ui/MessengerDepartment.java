package java_tutorial.parser.json.socket.ui;

import java_tutorial.parser.json.socket.dto.Department;

public class MessengerDepartment {
	public Department department;
	public DepartmentCRUD msg;

	public MessengerDepartment() {
		// TODO Auto-generated constructor stub
	}

	public MessengerDepartment(Department department, DepartmentCRUD msg) {
		this.department = department;
		this.msg = msg;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public DepartmentCRUD getMsg() {
		return msg;
	}

	public void setMsg(DepartmentCRUD msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "MessengerDepartment [department=" + department + ", msg=" + msg + "]";
	}

}
