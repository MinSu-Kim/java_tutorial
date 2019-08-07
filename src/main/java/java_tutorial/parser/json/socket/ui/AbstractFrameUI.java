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

import java_tutorial.parser.json.socket.ui.content.AbstractPanel;
import java_tutorial.parser.json.socket.ui.list.AbstractList;

@SuppressWarnings("serial")
public abstract class AbstractFrameUI<T> extends JFrame implements ActionListener {
	private JButton btnAdd;
	protected AbstractPanel<T> pContent;
	protected List<T> itemList;
	protected AbstractList<T> pList;
	private JButton btnCancel;

	private JPopupMenu popupMenu;
	private JMenuItem mntmUpdate;
	private JMenuItem mntmDelete;

	public AbstractFrameUI(String title) {
		initDao();
		initComponents(title);
	}

	protected abstract void initDao();

	private void initComponents(String title) {
		setTitle(title);
		setBounds(200, 100, 450, 450);
		JPanel pMain = new JPanel();
		getContentPane().add(pMain, BorderLayout.CENTER);
		pMain.setLayout(new BorderLayout(0, 0));

		pContent = createContentPanel();

		pMain.add(pContent, BorderLayout.CENTER);

		JPanel pBtns = new JPanel();
		pMain.add(pBtns, BorderLayout.SOUTH);

		btnAdd = new JButton("ì¶”ê?");
		btnAdd.addActionListener(this);
		pBtns.add(btnAdd);

		btnCancel = new JButton("ì·¨ì†Œ");
		btnCancel.addActionListener(this);
		pBtns.add(btnCancel);

		pList = createListPanel();
		getContentPane().add(pList, BorderLayout.SOUTH);

		popupMenu = new JPopupMenu();

		mntmUpdate = new JMenuItem("?ˆ˜? •");
		mntmUpdate.addActionListener(this);
		popupMenu.add(mntmUpdate);

		mntmDelete = new JMenuItem("?‚­? œ");
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
			if (e.getActionCommand().equals("ì¶”ê?")) {
				actionPerformedBtnAdd(e);
			}
			if (e.getActionCommand().equals("?ˆ˜? •")) {
				actionPerformedBtnUpdate(e);
			}
		}
	}

	protected void reloadList() {
		itemList = getListAll();
		pList.setItemList(itemList);
		pList.reloadData();
	}

	private void refreshUI(T item, int res) {
		String message = res == 1 ? "?„±ê³?" : "?‹¤?Œ¨";
		JOptionPane.showMessageDialog(null, item + message);
		reloadList();
		clearContent();
	}

	private void actionPerformedBtnUpdate(ActionEvent e) {
		T updateDept = pContent.getItem();
		int res =updateItem(updateDept);
		refreshUI(updateDept, res);
		btnAdd.setText("ì¶”ê?");
	}
	
	private void actionPerformedMntmUpdate(ActionEvent e) {
		T updateDept = pList.getSelectedItem();
		pContent.setItem(updateDept);
		btnAdd.setText("?ˆ˜? •");
	}

	private void actionPerformedMntmDelete(ActionEvent e) {
		T delDept = pList.getSelectedItem();
		int res = deleteItem(delDept);
		refreshUI(delDept, res);
	}
	
	protected void actionPerformedBtnAdd(ActionEvent e) {
		T insertDepartment = pContent.getItem();
		int res = insertItem(insertDepartment);
		refreshUI(insertDepartment, res);
	}
	
	protected void actionPerformedBtnCancel(ActionEvent e) {
		clearContent();
	}
	
	protected abstract AbstractList<T> createListPanel();
	protected abstract AbstractPanel<T> createContentPanel();
	
	protected abstract int updateItem(T item);
	protected abstract List<T> getListAll();
	protected abstract void clearContent();
	protected abstract int deleteItem(T item);
	protected abstract int insertItem(T item);

}
