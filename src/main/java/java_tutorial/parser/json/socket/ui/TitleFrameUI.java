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

import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.content.PanelTitle;
import java_tutorial.parser.json.socket.ui.enum_crud.TitleCRUD;
import java_tutorial.parser.json.socket.ui.list.TitleList;
import java_tutorial.parser.json.socket.ui.msg_send.MessengerTitle;

@SuppressWarnings("serial")
public class TitleFrameUI extends JFrame implements ActionListener {
	private JButton btnAdd;
	protected PanelTitle pContent;
	protected TitleList pList;
	private JButton btnCancel;

	private JPopupMenu popupMenu;
	private JMenuItem mntmUpdate;
	private JMenuItem mntmDelete;

	private DataOutputStream out;
	private List<Title> itemList;

	public void setTitleList(List<Title> itemList) {
		this.itemList = itemList;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public TitleFrameUI(String title) {
		initComponents(title);
	}

	private void initComponents(String title) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
		setBounds(200, 100, 450, 450);
		JPanel pMain = new JPanel();
		getContentPane().add(pMain, BorderLayout.CENTER);
		pMain.setLayout(new BorderLayout(0, 0));

		pContent = new PanelTitle("직책");

		pMain.add(pContent, BorderLayout.CENTER);

		JPanel pBtns = new JPanel();
		pMain.add(pBtns, BorderLayout.SOUTH);

		btnAdd = new JButton("추가");
		btnAdd.addActionListener(this);
		pBtns.add(btnAdd);

		btnCancel = new JButton("취소");
		btnCancel.addActionListener(this);
		pBtns.add(btnCancel);

		pList = new TitleList("직책 목록");
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
		Title title = pContent.getItem();
		sendMessage(title, TitleCRUD.TITLE_UPDATE);
		btnAdd.setText("추가");
	}
	
	protected void actionPerformedBtnAdd(ActionEvent e) {
		Title title = pContent.getItem();
		sendMessage(title, TitleCRUD.TITLE_INSERT);
	}
	
	protected void actionPerformedBtnCancel(ActionEvent e) {
		clearContent();
	}
	
	private void actionPerformedMntmUpdate(ActionEvent e) {
		Title updateTitle = pList.getSelectedItem();
		pContent.setItem(updateTitle);
		btnAdd.setText("수정");
	}

	private void actionPerformedMntmDelete(ActionEvent e) {
		Title delDept = pList.getSelectedItem();
		sendMessage(delDept, TitleCRUD.TITLE_DELETE);
	}

	private void sendMessage(Title title, TitleCRUD msg) {
		Gson gson = new Gson();
		MessengerTitle messenger = new MessengerTitle(title, msg);
		String json = gson.toJson(messenger);
		System.out.println(json);
		try {
			out.writeUTF(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
