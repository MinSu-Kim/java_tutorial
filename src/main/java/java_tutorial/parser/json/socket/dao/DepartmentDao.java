package java_tutorial.parser.json.socket.dao;

import java.util.List;

import java_tutorial.parser.json.socket.dto.Department;

public interface DepartmentDao {
	List<Department> selectDepartmentByAll();
	int insertDepartment(Department title);
	int deleteDepartment(Department title);
	int updateDepartment(Department title);
	Department selectDepartmentByCode(Department title);
}
