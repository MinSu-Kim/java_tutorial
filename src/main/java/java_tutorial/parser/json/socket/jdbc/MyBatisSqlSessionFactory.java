package java_tutorial.parser.json.socket.jdbc;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisSqlSessionFactory {
	private static SqlSessionFactory sqlSessionFactory;
	
	private static SqlSessionFactory getSqlSessionFactory() {
		if (sqlSessionFactory == null) {
			try(InputStream is = Resources.getResourceAsStream("mybatis-config.xml")){
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getCause());
			}
		}
		return sqlSessionFactory;
	}
	
	public static  SqlSession openSession() {
		return getSqlSessionFactory().openSession();
	}
}
