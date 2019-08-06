package java_tutorial.socket;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ChatServer extends JFrame implements ActionListener {
	private JPanel contentPane;

	private BufferedReader in = null;
	private BufferedWriter out = null;
	private ServerSocket listener = null;
	private Socket socket = null;
	private Receiver receiver; // JTextArea�� ��ӹް� Runnable �������̽��� ������ Ŭ�����μ� ���� ������ ��� ��ü
	private JTextField sender; // JTextField ��ü�μ� ������ ������ ��� ��ü

	public ChatServer() {
		initComponents();
		
		try {
			setupConnection();
		} catch (IOException e) {
			handleError(e.getMessage());
		}
		
		Thread th = new Thread(receiver); // ���κ��� �޽��� ������ ���� ������ ����
		th.start();
	}
	
	private void initComponents() {
		setTitle("���� ä�� â"); // ������ Ÿ��Ʋ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //������ ���� ��ư(X)�� Ŭ���ϸ� ���α׷� ����
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		receiver = new Receiver(); // Ŭ���̾�Ʈ���� ���� �޽����� ����� ���۳�Ʈ
		receiver.setEditable(false); // ���� �Ұ�

		sender = new JTextField();
		sender.addActionListener(this);

		contentPane.add(new JScrollPane(receiver),BorderLayout.CENTER); // ��ũ�ѹٸ� ����  ScrollPane �̿�
		contentPane.add(sender,BorderLayout.SOUTH);
		
		setSize(400, 200); // �� 400 �ȼ�, ���� 200 �ȼ��� ũ��� ������ ũ�� ����
		setVisible(true); // �������� ȭ�鿡 ��Ÿ������ ����
	}
	
	
	private void setupConnection() throws IOException {
		listener = new ServerSocket(9999); // ���� ���� ����
		socket = listener.accept(); // Ŭ���̾�Ʈ�κ��� ���� ��û ���
		receiver.append("Ŭ���̾�Ʈ�κ��� ���� �Ϸ�");
		int pos = receiver.getText().length();
		receiver.setCaretPosition(pos); // caret �������� ���� ���������� �̵�
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Ŭ���̾�Ʈ�κ����� �Է� ��Ʈ��
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Ŭ���̾�Ʈ���� ��� ��Ʈ��
	}

	private static void handleError(String string) {
		System.out.println(string);
		System.exit(1);
	}
	
	private class Receiver extends JTextArea implements Runnable {
		@Override
		public void run() {
			String msg = null;
			while (true) {
				try {
					msg = in.readLine(); // ���κ��� �� ���� ���ڿ� �ޱ�
				} catch (IOException e) {
					handleError(e.getMessage());
				} 
				this.append("\n  Ŭ���̾�Ʈ : " + msg); // ���� ���ڿ��� JTextArea�� ���
				int pos = this.getText().length();
				this.setCaretPosition(pos); // caret �������� ���� ���������� �̵�
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) { // JTextField�� <Enter> Ű ó��
		if (e.getSource() == sender) {
			String msg = sender.getText(); // �ؽ�Ʈ �ʵ忡�� ���ڿ� ����
			try {
				out.write(msg+"\n"); // ���ڿ� ����
				out.flush();
				
				receiver.append("\n���� : " + msg);// JTextArea�� ���
				int pos = receiver.getText().length();
				receiver.setCaretPosition(pos); // caret �������� ���� ���������� �̵�
				sender.setText(null); // �Է�â�� ���ڿ� ����
			} catch (IOException e1) {
				handleError(e1.getMessage());
			} 
		}
	}
	
	public static void main(String[] args) {
		new ChatServer();
	}

}
