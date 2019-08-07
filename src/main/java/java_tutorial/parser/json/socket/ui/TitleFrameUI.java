package java_tutorial.parser.json.socket.ui;

import java.util.List;

import java_tutorial.parser.json.socket.dao.TitleDao;
import java_tutorial.parser.json.socket.dao.TitleDaoImpl;
import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.content.AbstractPanel;
import java_tutorial.parser.json.socket.ui.content.PanelTitle;
import java_tutorial.parser.json.socket.ui.list.AbstractList;
import java_tutorial.parser.json.socket.ui.list.TitleList;

@SuppressWarnings("serial")
public class TitleFrameUI extends AbstractFrameUI<Title> {
	private TitleDao dao;
	
	public TitleFrameUI(String title) {
		super(title);
	}
	
	@Override
	protected  void initDao() {
		dao = new TitleDaoImpl();
	}
	
	@Override
	protected AbstractList<Title> createListPanel(){
		return new TitleList();
	}
	
	@Override
	protected AbstractPanel<Title> createContentPanel(){
		return new PanelTitle("직책 ?���?");
	}

	@Override
	protected void clearContent() {
		pContent.clearComponent(itemList.size() == 0 ? 1 : itemList.size() + 1);
	}
	
	@Override
	public List<Title> getListAll(){
		return dao.selectTitleByAll();
	}
	
	@Override
	protected int updateItem(Title item) {
		return dao.updateTitle(item);
	}

	@Override
	protected int insertItem(Title item) {
		return dao.insertTitle(item);
	}
	
	@Override
	protected int deleteItem(Title item) {
		return dao.deleteTitle(item);
	}
}
