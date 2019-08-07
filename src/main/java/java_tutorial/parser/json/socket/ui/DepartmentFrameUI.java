package java_tutorial.parser.json.socket.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java_tutorial.parser.json.socket.dao.DepartmentDao;
import java_tutorial.parser.json.socket.dao.DepartmentDaoImpl;
import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.content.AbstractPanel;
import java_tutorial.parser.json.socket.ui.content.PanelDepartment;
import java_tutorial.parser.json.socket.ui.list.AbstractList;
import java_tutorial.parser.json.socket.ui.list.DepartmentList;

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

		pContent = new PanelDepartment("부서정보");

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
			if (e.getActionCommand().equals("異뷂옙?")) {
				actionPerformedBtnAdd(e);
			}
			if (e.getActionCommand().equals("?占쏙옙?占쏙옙")) {
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

	}
	
	protected void actionPerformedBtnAdd(ActionEvent e) {

	}

	protected void actionPerformedBtnCancel(ActionEvent e) {
		clearContent();
	}

	private void actionPerformedMntmUpdate(ActionEvent e) {

	}

	private void actionPerformedMntmDelete(ActionEvent e) {

	}

}
