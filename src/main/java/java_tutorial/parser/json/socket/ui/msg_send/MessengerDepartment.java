package java_tutorial.parser.json.socket.ui.msg_send;

import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.ui.enum_crud.DepartmentCRUD;

public class MessengerDepartment {
	public Department department;
	public DepartmentCRUD msg;

	public MessengerDepartment() {
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
