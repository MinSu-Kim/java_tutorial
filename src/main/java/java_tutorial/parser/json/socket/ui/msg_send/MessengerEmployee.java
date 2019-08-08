package java_tutorial.parser.json.socket.ui.msg_send;

import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.ui.enum_crud.EmployeeCRUD;

public class MessengerEmployee {
	public Employee employee;
	public EmployeeCRUD msg;

	public MessengerEmployee() {
	}

	public MessengerEmployee(Employee department, EmployeeCRUD msg) {
		this.employee = department;
		this.msg = msg;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setDepartment(Employee department) {
		this.employee = department;
	}

	public EmployeeCRUD getMsg() {
		return msg;
	}

	public void setMsg(EmployeeCRUD msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return String.format("MessengerEmployee [employee=%s, msg=%s]", employee, msg);
	}

}
