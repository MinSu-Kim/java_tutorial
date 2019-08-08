package java_tutorial.parser.json.socket.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java_tutorial.parser.json.socket.dao.DepartmentDao;
import java_tutorial.parser.json.socket.dao.DepartmentDaoImpl;
import java_tutorial.parser.json.socket.dao.TitleDao;
import java_tutorial.parser.json.socket.dao.TitleDaoImpl;
import java_tutorial.parser.json.socket.ui.thread.ServerDepartmentReceiver;
import java_tutorial.parser.json.socket.ui.thread.ServerTitleReceiver;

public class JsonServer {
	public static final String HOST = "localhost";
	public static final int PORT = 12345;
	
	private ServerSocket serverSocket;
	private Socket socket;
	private TitleDao titleDao;
	private DepartmentDao deptDao;
	
	public static void main(String[] args) throws Exception {
		new JsonServer();
	}

	public JsonServer() {
		titleDao = new TitleDaoImpl();
		deptDao = new DepartmentDaoImpl();
		
		setupConnection();
		
		ServerTitleReceiver th = new ServerTitleReceiver(); // 상대로부터 메시지 수신을 위한 스레드 생성
		th.setDao(titleDao);
		th.setSocket(socket);
		th.start();
		
		ServerDepartmentReceiver deptTh = new ServerDepartmentReceiver();
		deptTh.setDao(deptDao);
		deptTh.setSocket(socket);
		deptTh.start();
//		serverSocket.close();
	}

	private void setupConnection() {
		try {
			serverSocket = new ServerSocket(PORT); // 서버 소켓 생성
			System.out.println("Server Ready " + serverSocket);
			socket = serverSocket.accept(); // 클라이언트로부터 연결 요청 대기
			System.out.println("Client Request " + socket);
		} catch (IOException e) {
			e.printStackTrace();
		} // 클라이언트 소켓 생성
	}

}
