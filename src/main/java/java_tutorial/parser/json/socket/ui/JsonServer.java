package java_tutorial.parser.json.socket.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java_tutorial.parser.json.socket.dao.TitleDao;
import java_tutorial.parser.json.socket.dao.TitleDaoImpl;
import java_tutorial.parser.json.socket.dto.Title;

public class JsonServer {
	public static final String HOST = "localhost";
	public static final int PORT = 12345;
	
	private ServerSocket serverSocket;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private TitleDao dao;

	public static void main(String[] args) throws Exception {
		new JsonServer();
	}

	public JsonServer() {
		dao = new TitleDaoImpl();
		setupConnection();
		
		Receiver receiver = new Receiver();
		Thread th = new Thread(receiver); // ���κ��� �޽��� ������ ���� ������ ����
		th.start();
//		serverSocket.close();
	}

	private void setupConnection() {
		try {
			serverSocket = new ServerSocket(PORT); // ���� ���� ����
			System.out.println("Server Ready " + serverSocket);
			socket = serverSocket.accept(); // Ŭ���̾�Ʈ�κ��� ���� ��û ���
			System.out.println("Client Request " + socket);
			in = new DataInputStream(socket.getInputStream()); // Ŭ���̾�Ʈ�κ����� �Է� ��Ʈ��
			out = new DataOutputStream(socket.getOutputStream()); // Ŭ���̾�Ʈ���� ��� ��Ʈ��
		} catch (IOException e) {
			e.printStackTrace();
		} // Ŭ���̾�Ʈ ���� ����
	}

	private class Receiver implements Runnable {
		Gson gson = new Gson();

		@Override
		public void run() {
			String msg = null;
			MessengerTitle rep = null;
			try {
				while (true) {
					msg = in.readUTF();
					if (msg != null) {
						rep = gson.fromJson(msg, MessengerTitle.class);
						Title newTitle;
						int res;
						switch (rep.getMsg()) {
						case TITLE_INSERT:
							newTitle = rep.getTitle();
							res = dao.insertTitle(newTitle);
							replyResult(res, TitleCRUD.TITLE_INSERT);
							break;
						case TITLE_DELETE:
							newTitle = rep.getTitle();
							res = dao.deleteTitle(newTitle);
							replyResult(res, TitleCRUD.TITLE_DELETE);
							break;
						case TITLE_UPDATE:
							newTitle = rep.getTitle();
							res = dao.updateTitle(newTitle);
							replyResult(res, TitleCRUD.TITLE_UPDATE);
							break;
						case TITLE_LIST:
							List<Title> list = dao.selectTitleByAll();
							replyResult(list, TitleCRUD.TITLE_LIST);
							break;
						default:
							break;
						}
					}
				}
			} catch (SocketException e) {
				System.out.println("Ŭ���̾�Ʈ�� ������ ���� �����ϴ�.");
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	private void replyResult(int res, TitleCRUD msg) {
		Gson gson = new Gson();
		ReplyTitle messenger = new ReplyTitle(msg, res);
		String repl = gson.toJson(messenger);
		System.out.println(repl);
		try {
			out.writeUTF(repl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void replyResult(List<Title> list, TitleCRUD msg) {
		Gson gson = new Gson();
		String listToJson = gson.toJson(list, new TypeToken<List<Title>>() {}.getType());

		ReplyTitle messenger = new ReplyTitle(msg, listToJson);
		String repl = gson.toJson(messenger);
		System.out.println(repl);
		try {
			out.writeUTF(repl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
