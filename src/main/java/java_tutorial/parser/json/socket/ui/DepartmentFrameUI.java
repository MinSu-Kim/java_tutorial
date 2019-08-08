package java_tutorial.parser.json.socket.ui;

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
import java_tutorial.parser.json.socket.ui.content.PanelDepartment;
import java_tutorial.parser.json.socket.ui.enum_crud.DepartmentCRUD;
import java_tutorial.parser.json.socket.ui.list.DepartmentList;
import java_tutorial.parser.json.socket.ui.msg_send.MessengerDepartment;

@SuppressWarnings("serial")
public class DepartmentFrameUI extends JFrame implements ActionListener {
	private JButton btnAdd;
	private PanelDepartment pContent;
	private List<Department> itemList;
	private DepartmentList pList;
	private JButton btnCancel;

	private JPopupMenu popupMenu;
	private JMenuItem mntmUpdate;
	private JMenuItem mntmDelete;
	
	private DataOutputStream out;

	public void setTitleList(List<Department> itemList) {
		this.itemList = itemList;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}
	
	public DepartmentFrameUI(String title) {
		initComponents(title);
	}

	private void initComponents(String title) {
		setTitle(title);
		setBounds(200, 100, 450, 450);
		JPanel pMain = new JPanel();
		getContentPane().add(pMain, BorderLayout.CENTER);
		pMain.setLayout(new BorderLayout(0, 0));

		pContent = new PanelDepartment("부서 정보");

		pMain.add(pContent, BorderLayout.CENTER);

		JPanel pBtns = new JPanel();
		pMain.add(pBtns, BorderLayout.SOUTH);

		btnAdd = new JButton("추가");
		btnAdd.addActionListener(this);
		pBtns.add(btnAdd);

		btnCancel = new JButton("취소");
		btnCancel.addActionListener(this);
		pBtns.add(btnCancel);

		pList = new DepartmentList("부서 목록");
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

	private void clearContent() {
		pContent.clearComponent(itemList.size() == 0 ? 1 : itemList.size() + 1);
	}

	public void refreshUI() {
		pList.setItemList(itemList);
		pList.reloadData();
		clearContent();
	}

	private void actionPerformedBtnUpdate(ActionEvent e) {
		Department title = pContent.getItem();
		sendMessage(title, DepartmentCRUD.DEPARTMENT_UPDATE);
		btnAdd.setText("추가");
	}
	
	protected void actionPerformedBtnAdd(ActionEvent e) {
		Department dept = pContent.getItem();
		sendMessage(dept, DepartmentCRUD.DEPARTMENT_INSERT);
	}

	protected void actionPerformedBtnCancel(ActionEvent e) {
		clearContent();
	}

	private void actionPerformedMntmUpdate(ActionEvent e) {
		Department updateDept = pList.getSelectedItem();
		pContent.setItem(updateDept);
		btnAdd.setText("수정");
	}

	private void actionPerformedMntmDelete(ActionEvent e) {
		Department delDept = pList.getSelectedItem();
		sendMessage(delDept, DepartmentCRUD.DEPARTMENT_DELETE);
	}

	private void sendMessage(Department dept, DepartmentCRUD msg) {
		Gson gson = new Gson();
		MessengerDepartment messenger = new MessengerDepartment(dept, msg);
		String json = gson.toJson(messenger);
		System.out.println(json);
		try {
			out.writeUTF(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
