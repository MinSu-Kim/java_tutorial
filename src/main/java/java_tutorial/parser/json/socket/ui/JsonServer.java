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

import java_tutorial.parser.json.Person;
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
			System.out.println("Server Ready~~");
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
			Messenger rep = null;
			try {
				while ((msg = in.readUTF())!= null) {
					rep = gson.fromJson(msg, Messenger.class);
					Title newTitle;
					int res;
					switch (rep.getMsg()) {
					case "insert":
						newTitle = rep.getTitle();
						res = dao.insertTitle(newTitle);
						replyResult(res, "insert");
						break;
					case "delete":
						newTitle = rep.getTitle();
						res = dao.deleteTitle(newTitle);
						replyResult(res, "delete");
						break;
					case "update":
						newTitle = rep.getTitle();
						res = dao.updateTitle(newTitle);
						replyResult(res, "update");
						break;
					case "listAll":
						List<Title> list = dao.selectTitleByAll();
						replyResult(list, "listAll");
						break;
					default:
						break;
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
	
	private void replyResult(int res, String msg) {
		Gson gson = new Gson();
		Reply messenger = new Reply(msg, res);
		String repl = gson.toJson(messenger);
		System.out.println(repl);
		try {
			out.writeUTF(repl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void replyResult(List<Title> list, String msg) {
		Gson gson = new Gson();
		String listToJson = gson.toJson(list, new TypeToken<List<Title>>(){}.getType());
		
		Reply messenger = new Reply(msg, listToJson);
		String repl = gson.toJson(messenger);
		System.out.println(repl);
		try {
			out.writeUTF(repl);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}
