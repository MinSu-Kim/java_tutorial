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

import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.service.EmployeeUIService;
import java_tutorial.parser.json.socket.ui.enum_crud.EmployeeCRUD;
import java_tutorial.parser.json.socket.ui.msg_reply.ReplyEmployee;
import java_tutorial.parser.json.socket.ui.msg_send.MessengerEmployee;

public class ServerProcessEmployeeThread extends Thread {
		private Socket socket;
		private EmployeeUIService empDao;
		
		public ServerProcessEmployeeThread(Socket socket, EmployeeUIService empDao) {
			this.socket = socket;
			this.empDao = empDao;
		}
		
		@Override
		public void run() {
				ServerEmployeeReceiver empTh = new ServerEmployeeReceiver(); // 상대로부터 메시지 수신을 위한 스레드 생성
				empTh.setDao(empDao);
				empTh.setSocket(socket);
				empTh.start();
		}
		
		private class ServerEmployeeReceiver extends Thread {

			private DataInputStream in;
			private DataOutputStream out;
			private EmployeeUIService dao;

			private Gson gson;

			public ServerEmployeeReceiver() {
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

			public void setDao(EmployeeUIService dao) {
				this.dao = dao;
			}

			@Override
			public void run() {
				String msg = null;
				MessengerEmployee rep = null;
				try {
					while (true) {
						msg = in.readUTF();
						if (msg != null) {
							rep = gson.fromJson(msg, MessengerEmployee.class);
							Employee newTitle;
							int res;
							switch (rep.getMsg()) {
							case EMPLOYEE_INSERT:
								newTitle = rep.getEmployee();
								res = dao.createItem(newTitle);
								replyResult(res, EmployeeCRUD.EMPLOYEE_INSERT);
								break;
							case EMPLOYEE_DELETE:
								newTitle = rep.getEmployee();
								res = dao.deleteEmp(newTitle);
								replyResult(res, EmployeeCRUD.EMPLOYEE_DELETE);
								break;
							case EMPLOYEE_UPDATE:
								newTitle = rep.getEmployee();
								res = dao.updateEmpoyee(newTitle);
								replyResult(res, EmployeeCRUD.EMPLOYEE_UPDATE);
								break;
							case EMPLOYEE_LIST:
								List<Employee> list = dao.selectEmpAll();
								replyResult(list, EmployeeCRUD.EMPLOYEE_LIST);
								break;
							case EMPLOYEE_LIST_DEPT:
								List<Department> deptList = dao.selectDeptAll();
								replyResult(deptList, EmployeeCRUD.EMPLOYEE_LIST_DEPT);
								break;
							case EMPLOYEE_LIST_TITLE:
								List<Title> titleList = dao.selectTitleAll();
								replyResult(titleList, EmployeeCRUD.EMPLOYEE_LIST_TITLE);
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

			private void replyResult(int res, EmployeeCRUD msg) {
				Gson gson = new Gson();
				ReplyEmployee messenger = new ReplyEmployee(msg, res);
				String repl = gson.toJson(messenger);
				System.out.println(repl);
				try {
					out.writeUTF(repl);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void replyResult(List<?> list, EmployeeCRUD msg) {
				
				Gson gson = new Gson();
				String listToJson = gson.toJson(list, new TypeToken<List<?>>() {}.getType());

				ReplyEmployee messenger = new ReplyEmployee(msg, listToJson);
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