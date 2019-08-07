package java_tutorial.parser.json.socket.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;

import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.dto.State;
import java_tutorial.parser.json.socket.jdbc.MyBatisSqlSessionFactory;

public class EmployeeDaoImpl implements EmployeeDao {
	private static final String namespace = "kr.or.yi.gradle_mybatis_c3p0_teacher.dao.EmployeeDao";

	@Override
	public List<Employee> selectEmployeeByAll() {
		try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession()) {
			return sqlSession.selectList(namespace + ".selectEmployeeByAll");
		}
	}

	@Override
	public int insertEmployee(Employee employee) {
		try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession()) {
			int res = sqlSession.update(namespace + ".insertEmployee", employee);
			sqlSession.commit();
			return res;
		}
	}

	@Override
	public int deleteEmployee(Employee employee) {
		try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession()) {
			int res = sqlSession.update(namespace + ".deleteEmployee", employee);
			sqlSession.commit();
			return res;
		}
	}

	@Override
	public int updateEmployee(Employee employee) {
		try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession()) {
			int res = sqlSession.update(namespace + ".updateEmployee", employee);
			sqlSession.commit();
			return res;
		}
	}

	@Override
	public Employee selectEmployeeByCode(Employee employee) {
		try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession()) {
			return sqlSession.selectOne(namespace + ".selectEmployeeByCode", employee);
		}
	}

	@Override
	public Map<String, Object> getSalaryByDepartment(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		ResultHandler<State> resultHandler = new ResultHandler<State>() {
			@Override
			public void handleResult(ResultContext<? extends State> resultContext) {
				State state = resultContext.getResultObject();
				map.put(state.getDeptName(), state.getTotal());
			}
		};
		
		try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession()) {
			sqlSession.select(namespace + ".getSalaryByDepartment", param, resultHandler);
		}
		return map;
	}

	@Override
	public State getStateSalaryByDepartment(Map<String, Object> param) {
		try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession()) {
			return sqlSession.selectOne(namespace + ".getStateSalaryByDepartment", param);
		}
	}

}










