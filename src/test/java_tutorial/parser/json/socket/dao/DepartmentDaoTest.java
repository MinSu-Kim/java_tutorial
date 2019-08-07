package java_tutorial.parser.json.socket.dao;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java_tutorial.parser.json.socket.AbstractTest;
import java_tutorial.parser.json.socket.dto.Department;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DepartmentDaoTest extends AbstractTest {
	private static DepartmentDao deptDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		deptDao = new DepartmentDaoImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		deptDao = null;
	}

	@Test
	public void test01SelectDepartmentByAll() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		List<Department> deptList = deptDao.selectDepartmentByAll();
		Assert.assertNotNull(deptList);
		
		for(Department dept : deptList) {
			log.debug(String.format("%d %s %s", dept.getDeptCode(), dept.getDeptName(), dept.getFloor()));
		}
	}
	
	@Test
	public void test02Insertdepartment() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Department insertDepartment = new Department(6, "마케팅", 20);
		int res = deptDao.insertDepartment(insertDepartment);
		Assert.assertEquals(1, res);
	}
	
	@Test
	public void test03Updatedepartment() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Department updatedepartment = new Department(6, "마케팅2", 40);
		int res = deptDao.updateDepartment(updatedepartment);
		Assert.assertEquals(1, res);
	}
	
	
	@Test
	public void test04Deletedepartment() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Department deletedepartment = new Department(6);
		int res = deptDao.deleteDepartment(deletedepartment);
		Assert.assertEquals(1, res);
	}

	@Test
	public void test05SelectdepartmentByCode() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Department seletedDepartment = new Department(1);
		Department searchDepartment = deptDao.selectDepartmentByCode(seletedDepartment);
		log.debug("search department" + searchDepartment);
		Assert.assertNotNull(searchDepartment);
	}

}
