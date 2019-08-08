package java_tutorial.parser.json.socket.ui.thread_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java_tutorial.parser.json.socket.dao.DepartmentDao;
import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.ui.enum_crud.DepartmentCRUD;
import java_tutorial.parser.json.socket.ui.msg_reply.ReplyDepartment;
import java_tutorial.parser.json.socket.ui.msg_send.MessengerDepartment;

public class ServerProcessDeptThread extends Thread{
	private Socket socket;
	private DepartmentDao deptDao;
	
	public ServerProcessDeptThread(Socket socket, DepartmentDao deptDao) {
		this.socket = socket;
		this.deptDao = deptDao;
	}
	
	@Override
	public void run() {
			ServerDepartmentReceiver detpTh = new ServerDepartmentReceiver(); // 상대로부터 메시지 수신을 위한 스레드 생성
			detpTh.setDao(deptDao);
			detpTh.setSocket(socket);
			detpTh.start();
	}
	
	private class ServerDepartmentReceiver extends Thread {

		private DataInputStream in;
		private DataOutputStream out;
		private DepartmentDao dao;

		private Gson gson;

		public ServerDepartmentReceiver() {
			gson = new Gson();
		}

		public void setSocket(Socket socket) {
			try {
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void setDao(DepartmentDao dao) {
			this.dao = dao;
		}

		@Override
		public void run() {
			String msg = null;
			MessengerDepartment rep = null;
			try {
				while (true) {
					msg = in.readUTF();
					if (msg != null) {
						rep = gson.fromJson(msg, MessengerDepartment.class);
						Department newDept;
						int res;
						switch (rep.getMsg()) {
						case DEPARTMENT_INSERT:
							newDept = rep.getDepartment();
							res = dao.insertDepartment(newDept);
							replyResult(res, DepartmentCRUD.DEPARTMENT_INSERT);
							break;
						case DEPARTMENT_DELETE:
							newDept = rep.getDepartment();
							res = dao.deleteDepartment(newDept);
							replyResult(res, DepartmentCRUD.DEPARTMENT_DELETE);
							break;
						case DEPARTMENT_UPDATE:
							newDept = rep.getDepartment();
							res = dao.updateDepartment(newDept);
							replyResult(res, DepartmentCRUD.DEPARTMENT_UPDATE);
							break;
						case DEPARTMENT_LIST:
							List<Department> list = dao.selectDepartmentByAll();
							replyResult(list, DepartmentCRUD.DEPARTMENT_LIST);
							break;
						default:
							break;
						}
					}
				}
			} catch (SocketException e) {
				System.out.println("클라이언트와 연결이 끊어 졌습니다.");
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		private void replyResult(int res, DepartmentCRUD msg) {
			Gson gson = new Gson();
			ReplyDepartment messenger = new ReplyDepartment(msg, res);
			String repl = gson.toJson(messenger);
			System.out.println(repl);
			try {
				out.writeUTF(repl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void replyResult(List<Department> list, DepartmentCRUD msg) {
			Gson gson = new Gson();
			String listToJson = gson.toJson(list, new TypeToken<List<Department>>() {}.getType());

			ReplyDepartment messenger = new ReplyDepartment(msg, listToJson);
			String repl = gson.toJson(messenger);
			System.out.println(repl);
			try {
				out.writeUTF(repl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}