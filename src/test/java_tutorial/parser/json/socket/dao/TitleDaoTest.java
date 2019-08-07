package java_tutorial.parser.json.socket.dao;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java_tutorial.parser.json.socket.AbstractTest;
import java_tutorial.parser.json.socket.dto.Title;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TitleDaoTest extends AbstractTest {
	private static TitleDao titleDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		titleDao = new TitleDaoImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		titleDao = null;
	}

	@Test
	public void test02SelectTitleByAll() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		List<Title> titleList = titleDao.selectTitleByAll();
		Assert.assertNotNull(titleList);
		
		for(Title t : titleList) {
			log.debug(String.format("%d -> %s", t.getTitleCode(), t.getTitleName()));
		}
	}
	
	@Test
	public void test01InsertTitle() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Title insertTitle = new Title(6, "인턴");
		int res = titleDao.insertTitle(insertTitle);
		Assert.assertEquals(1, res);
	}
	
	@Test
	public void test03UpdateTitle() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Title updateTitle = new Title(6, "계약직");
		int res = titleDao.updateTitle(updateTitle);
		Assert.assertEquals(1, res);
	}
	
	
	@Test
	public void test04DeleteTitle() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Title deleteTitle = new Title(6);
		int res = titleDao.deleteTitle(deleteTitle);
		Assert.assertEquals(1, res);
	}

	@Test
	public void test05SelectTitleByCode() {
		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		Title seletedTitle = new Title(1);
		Title searchTitle = titleDao.selectTitleByCode(seletedTitle);
		log.debug("search Title" + searchTitle);
		Assert.assertNotNull(searchTitle);
	}
}













