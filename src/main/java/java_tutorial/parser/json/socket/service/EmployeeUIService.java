package java_tutorial.parser.json.socket.service;

import java.util.List;

import java_tutorial.parser.json.socket.dao.DepartmentDao;
import java_tutorial.parser.json.socket.dao.DepartmentDaoImpl;
import java_tutorial.parser.json.socket.dao.EmployeeDao;
import java_tutorial.parser.json.socket.dao.EmployeeDaoImpl;
import java_tutorial.parser.json.socket.dao.TitleDao;
import java_tutorial.parser.json.socket.dao.TitleDaoImpl;
import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.dto.Title;

public class EmployeeUIService {
	private TitleDao titleDao;
	private DepartmentDao deptDao;
	private EmployeeDao empDao;

	public EmployeeUIService() {
		titleDao = new TitleDaoImpl();
		deptDao = new DepartmentDaoImpl();
		empDao = new EmployeeDaoImpl();
	}

	public List<Employee> selectEmpAll() {
		return empDao.selectEmployeeByAll();
	}

	public List<Department> selectDeptAll() {
		return deptDao.selectDepartmentByAll();
	}

	public List<Title> selectTitleAll() {
		return titleDao.selectTitleByAll();
	}

	public int updateEmpoyee(Employee item) {
		return empDao.updateEmployee(item);
	}

	public int deleteEmp(Employee item) {
		return empDao.deleteEmployee(item);
	}

	public int createItem(Employee item) {
		return empDao.insertEmployee(item);
	}

	
	
}
