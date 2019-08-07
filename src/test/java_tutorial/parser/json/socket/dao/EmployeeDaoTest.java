package java_tutorial.parser.json.socket.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java_tutorial.parser.json.socket.AbstractTest;
import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.dto.State;
import java_tutorial.parser.json.socket.dto.Title;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeDaoTest extends AbstractTest {
	private static EmployeeDao empDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		empDao = new EmployeeDaoImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		empDao = null;
	}

	@Test
	public void test01SelectEmployeeByAll() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		List<Employee> EmployeeList = empDao.selectEmployeeByAll();
		Assert.assertNotNull(EmployeeList);
		
		for(Employee t : EmployeeList) {
			log.debug(t.toString());
		}
	}
	
	@Test
	public void test02InsertEmployee() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Date joinDate = new Date();
		Employee newEmp = new Employee(5000, "케이시", 2000000, 
				                       new Department(1), true, 
				                       joinDate, new Title(3));
		int res = empDao.insertEmployee(newEmp);
		Assert.assertEquals(1, res);
	}
	@Test
	public void test03UpdateEmployee() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Calendar joinDate = Calendar.getInstance();
		joinDate.clear();
		joinDate.set(Calendar.YEAR, 2018);
		joinDate.set(Calendar.MONTH, 7);
		joinDate.set(Calendar.DAY_OF_MONTH, 01);
		
		Employee upemp = new Employee(5000, "케이시", 2000000, 
                new Department(1), true, 
                joinDate.getTime(), new Title(3));
		
		int res = empDao.updateEmployee(upemp);
		Assert.assertEquals(1, res);
	}

	@Test
	public void test04SelectEmployeeByCode() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
			
		Employee selEmp = new Employee(1003, "조민희"); 
		Employee res = empDao.selectEmployeeByCode(selEmp);
		Assert.assertNotNull(res);
	}
	
	@Test
	public void test05deleteEmployee() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Employee delEmp = new Employee(5000, "케이시");
		int res = empDao.deleteEmployee(delEmp);
		Assert.assertEquals(1, res);
	}
	
	@Test
	public void test06getSalaryByDepartment() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Map<String, Object> param = new HashMap<>();
		param.put("deptno", 1);
		log.debug(param.get("deptno") + "");
		
		Map<String, Object> result = empDao.getSalaryByDepartment(param);
		for(Entry<String, Object> e : result.entrySet()) {
			log.debug(String.format("%s->%d", e.getKey(), e.getValue()));
		}
		Assert.assertNotEquals(0, result.size());
	}

	@Test
	public void test07getStateSalaryByDepartment() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Map<String, Object> param = new HashMap<>();
		param.put("deptno", 2);
		
		State state = empDao.getStateSalaryByDepartment(param);
		log.debug(state.toString());
		
		Assert.assertNotEquals(null, state);
	}
}










