package java_tutorial.parser.json.socket.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java_tutorial.parser.json.socket.ui.content.AbstractPanel;
import java_tutorial.parser.json.socket.ui.content.PanelDepartment;
import java_tutorial.parser.json.socket.ui.list.AbstractList;
import java_tutorial.parser.json.socket.ui.list.DepartmentList;

@SuppressWarnings("serial")
public class DepartmentFrameUI extends JFrame implements ActionListener {
	private JButton btnAdd;
	private AbstractPanel<Department> pContent;
	private List<Department> deptList;
	private AbstractList<Department> pList;
	private JButton btnCancel;

	private JPopupMenu popupMenu;
	private JMenuItem mntmUpdate;
	private JMenuItem mntmDelete;

	private DepartmentDao dao;

	public DepartmentFrameUI() {
		dao = new DepartmentDaoImpl();
		initComponents();
	}

	private void initComponents() {
		setTitle("직책�?�?");
		setBounds(200, 100, 450, 450);
		JPanel pMain = new JPanel();
		getContentPane().add(pMain, BorderLayout.CENTER);
		pMain.setLayout(new BorderLayout(0, 0));

		pContent = new PanelDepartment("�??��");

		pMain.add(pContent, BorderLayout.CENTER);

		JPanel pBtns = new JPanel();
		pMain.add(pBtns, BorderLayout.SOUTH);

		btnAdd = new JButton("추�?");
		btnAdd.addActionListener(this);
		pBtns.add(btnAdd);

		btnCancel = new JButton("취소");
		btnCancel.addActionListener(this);
		pBtns.add(btnCancel);

		pList = new DepartmentList("�??��");
		getContentPane().add(pList, BorderLayout.SOUTH);

		popupMenu = new JPopupMenu();

		mntmUpdate = new JMenuItem("?��?��");
		mntmUpdate.addActionListener(this);
		popupMenu.add(mntmUpdate);

		mntmDelete = new JMenuItem("?��?��");
		mntmDelete.addActionListener(this);
		popupMenu.add(mntmDelete);

		pList.setPopupMenu(popupMenu);

		reloadList();
		clearContent();
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
			if (e.getActionCommand().equals("추�?")) {
				actionPerformedBtnAdd(e);
			}
			if (e.getActionCommand().equals("?��?��")) {
				actionPerformedBtnUpdate(e);
			}
		}
	}

	private void clearContent() {
		pContent.clearComponent(deptList.size() == 0 ? 1 : deptList.size() + 1);
	}

	private void reloadList() {
		deptList = dao.selectDepartmentByAll();
		pList.setItemList(deptList);
		pList.reloadData();
	}

	private void refreshUI(Department item, int res) {
		String message = res == 1 ? "?���?" : "?��?��";
		JOptionPane.showMessageDialog(null, item + message);
		reloadList();
		clearContent();
	}

	private void actionPerformedBtnUpdate(ActionEvent e) {
		Department updateDept = pContent.getItem();
		int res = dao.updateDepartment(updateDept);
		refreshUI(updateDept, res);
		btnAdd.setText("추�?");
	}
	
	protected void actionPerformedBtnAdd(ActionEvent e) {
		Department insertDepartment = pContent.getItem();
		int res = dao.insertDepartment(insertDepartment);
		refreshUI(insertDepartment, res);
	}

	protected void actionPerformedBtnCancel(ActionEvent e) {
		clearContent();
	}

	private void actionPerformedMntmUpdate(ActionEvent e) {
		Department updateDept = pList.getSelectedItem();
		pContent.setItem(updateDept);
		btnAdd.setText("?��?��");
	}

	private void actionPerformedMntmDelete(ActionEvent e) {
		Department delDept = pList.getSelectedItem();
		int res = dao.deleteDepartment(delDept);
		refreshUI(delDept, res);
	}

}
