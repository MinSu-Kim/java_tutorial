package java_tutorial.socket2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Chatting extends JFrame implements ActionListener, Runnable {
	
	private JPanel contentPane;
	private JTextField tFNickName;
	private JTextField tFTalk;
	private JButton btnConnect;
	private JTextArea tAView;
	
	private Socket soc;
	private Thread currentTh;
	private DefaultListModel<String> model;
	private JLabel lblCnt;
	private JButton btnSend;
	private DataOutputStream dos;
	private DataInputStream dis;
	private JButton btnClose;
	
	public Chatting() {
		setTitle("채팅");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel pEast = new JPanel();
		contentPane.add(pEast, BorderLayout.EAST);
		pEast.setLayout(new BorderLayout(0, 0));
		
		JPanel pCnt = new JPanel();
		pEast.add(pCnt, BorderLayout.NORTH);
		
		JLabel lblCntTitle = new JLabel("인원 : ");
		pCnt.add(lblCntTitle);
		
		lblCnt = new JLabel("0");
		pCnt.add(lblCnt);
		
		JLabel lblCntUnit = new JLabel("명");
		pCnt.add(lblCntUnit);
		
		JPanel pList = new JPanel();
		pEast.add(pList, BorderLayout.CENTER);
		pList.setLayout(new BorderLayout(0, 0));
		
		model = new DefaultListModel<>();
		JList<String> list = new JList<>(model);
		list.setVisibleRowCount(10);
		pList.add(list, BorderLayout.NORTH);
		
		btnClose = new JButton("끝내기");
		btnClose.addActionListener(this);
		pEast.add(btnClose, BorderLayout.SOUTH);
		
		JPanel pMain = new JPanel();
		contentPane.add(pMain, BorderLayout.CENTER);
		pMain.setLayout(new BorderLayout(0, 0));
		
		JPanel pNorth = new JPanel();
		pMain.add(pNorth, BorderLayout.NORTH);
		pNorth.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNickName = new JLabel("대화명 : ");
		pNorth.add(lblNickName, BorderLayout.WEST);
		
		tFNickName = new JTextField();
		pNorth.add(tFNickName, BorderLayout.CENTER);
		tFNickName.setColumns(20);
		
		btnConnect = new JButton("접속");
		btnConnect.addActionListener(this);
		pNorth.add(btnConnect, BorderLayout.EAST);
		
		JPanel pView = new JPanel();
		pMain.add(pView, BorderLayout.CENTER);
		pView.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		pView.add(scrollPane);
		
		tAView = new JTextArea();
		tAView.setEditable(false);
		scrollPane.setViewportView(tAView);
		
		JPanel pSouth = new JPanel();
		pMain.add(pSouth, BorderLayout.SOUTH);
		pSouth.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTalk = new JLabel("대  화 : ");
		pSouth.add(lblTalk, BorderLayout.WEST);
		
		tFTalk = new JTextField();
		tFTalk.addActionListener(this);
		pSouth.add(tFTalk, BorderLayout.CENTER);
		tFTalk.setColumns(20);
		
		btnSend = new JButton("전송");
		btnSend.addActionListener(this);
		pSouth.add(btnSend, BorderLayout.EAST);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConnect || e.getSource() == tFNickName) {
			actionPerformedBtnConnect(e);
		}
		if (e.getSource() == btnSend || e.getSource() == tFTalk) {
			actionPerformedBtnSend(e);
		}
		if (e.getSource() == btnClose) {
			actionPerformedBtnClose(e);
		}
	}

	private void connectServer() throws UnknownHostException, IOException {
		soc = new Socket(Server.HOST, Server.PORT);
		dos = new DataOutputStream(soc.getOutputStream());
		dis = new DataInputStream(soc.getInputStream());
	}

	@Override
	public void run() {
		tFNickName.setEnabled(false);
		btnConnect.setEnabled(false);
		tFTalk.requestFocus();
		tAView.setText("*** 대화에 참여 하셨네요!! ***\n\n\n");
		while(true){
			String msg = null;
			try {
				msg = dis.readUTF();
				if (msg.startsWith("cnt")) {
					String cnt = msg.split(":")[1];
					lblCnt.setText(cnt);
					continue;
				}
				if (msg.startsWith("list")) {
					msg = msg.split(":")[1];
					for(String element : msg.split(",")) {
						model.addElement(element);
					}
					continue;
				}
				tAView.append(msg+ "\n");
			} catch (IOException e) {
				break;
			} 	
		}
	}
	
	protected void actionPerformedBtnSend(ActionEvent e) {
		try {
			String msg = tFTalk.getText().trim();
			dos.writeUTF(msg);
			tFTalk.setText("");
		} catch (IOException e1) {
			e1.printStackTrace();
		}//서버에게 닉네임을 전송
	}
	
	protected void actionPerformedBtnConnect(ActionEvent e) {
		String str = tFNickName.getText().trim();
		if (str==null||str.length()==0){
			tFNickName.setText("");
			tFNickName.requestFocus();
			tAView.setText("대화명을 적으세요");
			return;
		}
			
		try {
			connectServer();
			dos.writeUTF(str);
			currentTh = new Thread(this);
			currentTh.start();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			tAView.setText("서버와 연결이 되지 않았습니다.");
			return;
		}
	}
	

	protected void actionPerformedBtnClose(ActionEvent e) {
		try {
			dos.writeUTF("exit");
			soc.close();
			System.exit(0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chatting frame = new Chatting();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
