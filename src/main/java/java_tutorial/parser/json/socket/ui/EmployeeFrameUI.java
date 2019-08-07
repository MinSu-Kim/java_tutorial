package java_tutorial.parser.json.socket.ui;

import java.util.List;

import javax.swing.JFrame;

import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.service.EmployeeUIService;
import java_tutorial.parser.json.socket.ui.content.AbstractPanel;
import java_tutorial.parser.json.socket.ui.content.PanelEmployee;
import java_tutorial.parser.json.socket.ui.list.AbstractList;
import java_tutorial.parser.json.socket.ui.list.EmployeeList;

@SuppressWarnings("serial")
public class EmployeeFrameUI extends AbstractFrameUI<Employee> {
	private EmployeeUIService service;
	
	public EmployeeFrameUI(String title) {
		super(title);
		setBounds(100, 100, 550, 700);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	protected void initDao() {
		service = new EmployeeUIService();		
	}

	@Override
	protected AbstractList<Employee> createListPanel() {
		return new EmployeeList();
	}

	@Override
	protected AbstractPanel<Employee> createContentPanel() {
		PanelEmployee empPanel = new PanelEmployee();
		empPanel.setDeptList(service.selectDeptAll());
		empPanel.setTitleList(service.selectTitleAll());
		return empPanel;
	}

	@Override
	protected int updateItem(Employee item) {
		return service.updateEmpoyee(item);
	}

	@Override
	protected List<Employee> getListAll() {
		return service.selectEmpAll();
	}

	@Override
	protected void clearContent() {
		pContent.clearComponent(itemList.size() == 0 ? 1 : itemList.size() + 1);
		
	}

	@Override
	protected int deleteItem(Employee item) {
		return service.deleteEmp(item);
	}

	@Override
	protected int insertItem(Employee item) {
		return service.createItem(item);
	}

}
