package java_tutorial.parser.json.socket.ui.thread_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.dto.Employee;
import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.enum_crud.EmployeeCRUD;
import java_tutorial.parser.json.socket.ui.msg_reply.ReplyEmployee;
import java_tutorial.parser.json.socket.ui.msg_send.MessengerEmployee;
import java_tutorial.parser.json.socket.ui_sub.EmployeeFrameUI;

public class ReceiveEmployeeThread extends Thread {
	private EmployeeFrameUI employeeFrame;
	private List<Employee> itemList;
	private List<Department> deptList;
	private List<Title> titleList;
	private DataInputStream in;
	private DataOutputStream out;
	
	public void setInOutStream(DataInputStream in, DataOutputStream out) {
		this.in = in;
		this.out = out;
		
		System.out.println("sendMessage employee - List");
		sendMessage(out, null, EmployeeCRUD.EMPLOYEE_LIST);
		
		System.out.println("sendMessage Department - List");
		sendMessage(out, null, EmployeeCRUD.EMPLOYEE_LIST_DEPT);
		
		System.out.println("sendMessage title - List");
		sendMessage(out, null, EmployeeCRUD.EMPLOYEE_LIST_TITLE);
	}

	@Override
	public void run() {
		System.out.println("receive");
		Gson gson = new Gson();
		String msg;
		ReplyEmployee rep;
		
		try {
			while (true) {
				msg = in.readUTF();
				if (msg != null) {
					System.out.println("receive message " + msg);
					rep = gson.fromJson(msg, ReplyEmployee.class);
					
					if (employeeFrame == null) {
						employeeFrame = new EmployeeFrameUI("사원 관리");
						employeeFrame.setOut(out);
					}
					
					if (rep.getMsg()==EmployeeCRUD.EMPLOYEE_LIST) {
						itemList = gson.fromJson(rep.getStrToJson(), new TypeToken<List<Employee>>() {}.getType());
						employeeFrame.setItemList(itemList);
					}
					
					if (rep.getMsg() == EmployeeCRUD.EMPLOYEE_LIST_DEPT) {
						deptList = gson.fromJson(rep.getStrToJson(), new TypeToken<List<Department>>() {}.getType());
						employeeFrame.setDeptList(deptList);
					}
					
					if (rep.getMsg() == EmployeeCRUD.EMPLOYEE_LIST_TITLE) {
						titleList = gson.fromJson(rep.getStrToJson(), new TypeToken<List<Title>>() {}.getType());
						employeeFrame.setTitleList(titleList);
					}
					
					if (rep.getRes() == 1) {
						JOptionPane.showMessageDialog(null, rep.getMsg() + "성공");
						sendMessage(out, null, EmployeeCRUD.EMPLOYEE_LIST);
					}
				
					if (itemList!=null && titleList !=null && deptList != null) {
						employeeFrame.setVisible(true);
					}
					
					employeeFrame.refreshUI();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("호스트와 연결이 끊김");
		}
	}

	private void sendMessage(DataOutputStream out, Employee emp, EmployeeCRUD msg) {
		Gson gson = new Gson();
		MessengerEmployee messenger = new MessengerEmployee( emp, msg);
		String json = gson.toJson(messenger);
		System.out.println("json" + json);
		try {
			out.writeUTF(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
