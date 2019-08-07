package java_tutorial.parser.json.socket.dao;

import java.util.List;
import java.util.Map;

import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.dto.State;

public interface EmployeeDao {
	List<Employee> selectEmployeeByAll();
	int insertEmployee(Employee employee);
	int deleteEmployee(Employee employee);
	int updateEmployee(Employee employee);
	Employee selectEmployeeByCode(Employee employee);
	
	Map<String, Object> getSalaryByDepartment(Map<String, Object> param);
	State getStateSalaryByDepartment(Map<String, Object> param);
}
