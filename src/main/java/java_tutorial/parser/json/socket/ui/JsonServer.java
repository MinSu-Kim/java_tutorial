package java_tutorial.parser.json.socket.ui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java_tutorial.parser.json.socket.dao.DepartmentDao;
import java_tutorial.parser.json.socket.dao.DepartmentDaoImpl;
import java_tutorial.parser.json.socket.dao.TitleDao;
import java_tutorial.parser.json.socket.dao.TitleDaoImpl;
import java_tutorial.parser.json.socket.ui.thread_server.ServerProcessDeptThread;
import java_tutorial.parser.json.socket.ui.thread_server.ServerProcessTitleThread;

public class JsonServer {
	public static final String HOST = "localhost";
	public static final int PORT_TITLE = 12345;
	public static final int PORT_DEPARTMENT = 12346;
	
	private ServerSocket serverTitleSocket;
	private ServerSocket serverDeptSocket;
	private TitleDao titleDao;
	private DepartmentDao deptDao;
	
	public static void main(String[] args) throws Exception {
		new JsonServer();
	}

	public JsonServer() {
		titleDao = new TitleDaoImpl();
		deptDao = new DepartmentDaoImpl();
		
		
		new Thread(()->setupTitleConnection()).start();
		new Thread(()->setupDeptConnection()).start();
		
//		serverSocket.close();
	}

	private void setupTitleConnection() {
		try {
			serverTitleSocket = new ServerSocket(PORT_TITLE); // 서버 소켓 생성
			System.out.println("Server Ready " + serverTitleSocket);
			while(true) {
				Socket socket = serverTitleSocket.accept(); // 클라이언트로부터 연결 요청 대기
				System.out.println("Client Request " + socket);
				new ServerProcessTitleThread(socket, titleDao).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                if( serverTitleSocket != null && !serverTitleSocket.isClosed() ) {
                    serverTitleSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	private void setupDeptConnection() {
		try {
			serverDeptSocket = new ServerSocket(PORT_DEPARTMENT); // 서버 소켓 생성
			System.out.println("Server Ready " + serverDeptSocket);
			while(true) {
				Socket socket = serverDeptSocket.accept(); // 클라이언트로부터 연결 요청 대기
				System.out.println("Client Request " + socket);
				new ServerProcessDeptThread(socket, deptDao).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                if( serverDeptSocket != null && !serverDeptSocket.isClosed() ) {
                	serverDeptSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

}