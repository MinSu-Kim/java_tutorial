package java_tutorial.parser.json.socket.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.content.PanelTitle;
import java_tutorial.parser.json.socket.ui.list.TitleList;

@SuppressWarnings("serial")
public class TitleFrameUI extends JFrame implements ActionListener {
	private JButton btnAdd;
	protected PanelTitle pContent;
	protected List<Title> itemList;
	protected TitleList pList;
	private JButton btnCancel;
	private int nextNo;
	
	private JPopupMenu popupMenu;
	private JMenuItem mntmUpdate;
	private JMenuItem mntmDelete;

	private DataInputStream in;
	private DataOutputStream out;
	private Socket socket;
	
	public TitleFrameUI(String title) {
		setupConnection();
	
//		itemList = new ArrayList<Title>();
		initComponents(title);
		
		Receiver receiver = new Receiver();
		Thread th = new Thread(receiver); // 상대로부터 메시지 수신을 위한 스레드 생성
		th.start();
		
		new Thread(()->{
			sendMessage(null, "listAll");
		}).start();
	}
	
	private void setupConnection() {
		try {
			socket = new Socket("localhost", 12345);
			in = new DataInputStream(socket.getInputStream()); // 클라이언트로부터의 입력 스트림
			out = new DataOutputStream(socket.getOutputStream()); // 클라이언트로의 출력 스트림
		} catch (IOException e) {
			System.err.println("Server가 동작하지 않습니다. Server를 확인하세요.");
//			e.printStackTrace();
			System.exit(-1);
		} // 클라이언트 소켓 생성
	
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

	private void refreshUI() {
		pList.setItemList(itemList);
		pList.reloadData();
		clearContent();
	}

	private void actionPerformedBtnUpdate(ActionEvent e) {
		Title title = pContent.getItem();
		sendMessage(title, "update");
		btnAdd.setText("추가");
	}

	private void actionPerformedMntmUpdate(ActionEvent e) {
		Title updateDept = pList.getSelectedItem();
		pContent.setItem(updateDept);
		btnAdd.setText("수정");
	}

	private void actionPerformedMntmDelete(ActionEvent e) {
		Title delDept = pList.getSelectedItem();
		sendMessage(delDept, "delete");
	}

	private void sendMessage(Title title, String msg) {
		Gson gson = new Gson();
		Messenger messenger = new Messenger(msg, title);
		String json = gson.toJson(messenger);
		System.out.println(json);
		try {
			out.writeUTF(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	protected void actionPerformedBtnAdd(ActionEvent e) {
		Title title = pContent.getItem();
		sendMessage(title, "insert");
	}
	
	private class Receiver implements Runnable {
		Gson gson = new Gson();
		String msg;
		Reply rep;
		
		@Override
		public void run() {
			try {
				while ((msg = in.readUTF())!= null) {
					rep = gson.fromJson(msg, Reply.class);
					if (rep.getMsg().equals("listAll")){
						JOptionPane.showMessageDialog(null, "listAll - 성공");
						itemList = gson.fromJson(rep.getStrToJson(), new TypeToken<List<Title>>(){}.getType());
						pList.setItemList(itemList);
						pList.reloadData();
					}
					if (rep.getRes() == 1) {
						JOptionPane.showMessageDialog(null, rep.getMsg() + "성공");
						refreshUI();
					}
					
				}
			} catch (EOFException e) {
				// TODO: handle exception
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				System.exit(1);
			} 
		}
	}
	
	protected void actionPerformedBtnCancel(ActionEvent e) {
		clearContent();
	}

	protected void clearContent() {
		pContent.clearComponent(itemList.size());
	}

	public void getListAll() {
		sendMessage(null, "listAll");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TitleFrameUI frame = new TitleFrameUI("직책관리");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
