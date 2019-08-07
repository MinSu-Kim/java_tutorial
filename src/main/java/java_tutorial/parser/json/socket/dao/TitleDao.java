package java_tutorial.parser.json.socket.dao;

import java.util.List;

import java_tutorial.parser.json.socket.dto.Title;

public interface TitleDao {
	List<Title> selectTitleByAll();
	int insertTitle(Title title);
	int deleteTitle(Title title);
	int updateTitle(Title title);
	Title selectTitleByCode(Title title);
}
