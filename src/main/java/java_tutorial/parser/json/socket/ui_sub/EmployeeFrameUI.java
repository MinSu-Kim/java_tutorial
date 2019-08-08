package java_tutorial.parser.json.socket.ui_sub;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.google.gson.Gson;

import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.content.PanelEmployee;
import java_tutorial.parser.json.socket.ui.enum_crud.EmployeeCRUD;
import java_tutorial.parser.json.socket.ui.list.EmployeeList;
import java_tutorial.parser.json.socket.ui.msg_send.MessengerEmployee;

@SuppressWarnings("serial")
public class EmployeeFrameUI extends JFrame implements ActionListener {
	private JButton btnAdd;
	protected PanelEmployee pContent;
	protected EmployeeList pList;
	private JButton btnCancel;

	private JPopupMenu popupMenu;
	private JMenuItem mntmUpdate;
	private JMenuItem mntmDelete;

	private DataOutputStream out;
	private List<Employee> itemList;

	public void setItemList(List<Employee> itemList) {
		this.itemList = itemList;
	}

	public void setDeptList(List<Department> deptList) {
		pContent.setDeptList(deptList);
	}

	public void setTitleList(List<Title> titleList) {
		pContent.setTitleList(titleList);
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public EmployeeFrameUI(String title) {
		initComponents(title);
	}

	private void initComponents(String title) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(title);
		setBounds(200, 100, 550, 700);
		JPanel pMain = new JPanel();
		getContentPane().add(pMain, BorderLayout.CENTER);
		pMain.setLayout(new BorderLayout(0, 0));

		pContent = new PanelEmployee("사원 정보");

		pMain.add(pContent, BorderLayout.CENTER);

		JPanel pBtns = new JPanel();
		pMain.add(pBtns, BorderLayout.SOUTH);

		btnAdd = new JButton("추가");
		btnAdd.addActionListener(this);
		pBtns.add(btnAdd);

		btnCancel = new JButton("취소");
		btnCancel.addActionListener(this);
		pBtns.add(btnCancel);

		pList = new EmployeeList("부서 목록");
		getContentPane().add(pList, BorderLayout.SOUTH);

		popupMenu = new JPopupMenu();

		mntmUpdate = new JMenuItem("수정");
		mntmUpdate.addActionListener(this);
		popupMenu.add(mntmUpdate);

		mntmDelete = new JMenuItem("삭제");
		mntmDelete.addActionListener(this);
		popupMenu.add(mntmDelete);

		pList.setPopupMenu(popupMenu);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmDelete) {
			actionPerformedMntmDelete(e);
		}
		if (e.getSource() == mntmUpdate) {
			actionPerformedMntmUpdate(e);
		}
		if (e.getSource() == btnCancel) {
			actionPerformedBtnCancel(e);
		}
		if (e.getSource() == btnAdd) {
			if (e.getActionCommand().equals("추가")) {
				actionPerformedBtnAdd(e);
			}
			if (e.getActionCommand().equals("수정")) {
				actionPerformedBtnUpdate(e);
			}
		}
	}
	
	protected void clearContent() {
		pContent.clearComponent(itemList.size() == 0 ? 1 : itemList.size() + 1);
	}
	
	public void refreshUI() {
		pList.setItemList(itemList);
		pList.reloadData();
		clearContent();
	}
	
	private void actionPerformedBtnUpdate(ActionEvent e) {
		Employee emp = pContent.getItem();
		sendMessage(emp, EmployeeCRUD.EMPLOYEE_UPDATE);
		btnAdd.setText("추가");
	}
	
	protected void actionPerformedBtnAdd(ActionEvent e) {
		Employee emp = pContent.getItem();
		sendMessage(emp, EmployeeCRUD.EMPLOYEE_INSERT);
	}
	
	protected void actionPerformedBtnCancel(ActionEvent e) {
		clearContent();
	}
	
	private void actionPerformedMntmUpdate(ActionEvent e) {
		Employee updateEmp = pList.getSelectedItem();
		pContent.setItem(updateEmp);
		btnAdd.setText("수정");
	}

	private void actionPerformedMntmDelete(ActionEvent e) {
		Employee delEmp = pList.getSelectedItem();
		sendMessage(delEmp, EmployeeCRUD.EMPLOYEE_DELETE);
	}

	private void sendMessage(Employee employee, EmployeeCRUD msg) {
		Gson gson = new Gson();
		MessengerEmployee messenger = new MessengerEmployee(employee, msg);
		String json = gson.toJson(messenger);
		System.out.println(json);
		try {
			out.writeUTF(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
