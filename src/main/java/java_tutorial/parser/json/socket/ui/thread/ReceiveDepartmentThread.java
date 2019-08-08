package java_tutorial.parser.json.socket.ui.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java_tutorial.parser.json.socket.dto.Department;
import java_tutorial.parser.json.socket.ui.DepartmentFrameUI;
import java_tutorial.parser.json.socket.ui.enum_crud.DepartmentCRUD;
import java_tutorial.parser.json.socket.ui.enum_crud.TitleCRUD;
import java_tutorial.parser.json.socket.ui.msg.MessengerDepartment;
import java_tutorial.parser.json.socket.ui.replymsg.ReplyDepartment;
import java_tutorial.parser.json.socket.ui.replymsg.ReplyTitle;

public class ReceiveDepartmentThread extends Thread {
	private DepartmentFrameUI departmentUI;
	private List<Department> itemList;
	private DataInputStream in;
	private DataOutputStream out;
	
	public void setSocket(Socket socket) {
		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sendMessage(out, null, DepartmentCRUD.DEPARTMENT_LIST);
	}



	@Override
	public void run() {
		System.out.println("receive");
		Gson gson = new Gson();
		String msg;
		ReplyDepartment rep;

		try {
			while (true) {
				msg = in.readUTF();
				if (msg != null) {
					System.out.println("receive message " + msg);
					rep = gson.fromJson(msg, ReplyDepartment.class);
					if (rep.getMsg()==DepartmentCRUD.DEPARTMENT_LIST) {
						itemList = gson.fromJson(rep.getStrToJson(), new TypeToken<List<Department>>() {}.getType());
						if (departmentUI == null) {
							departmentUI = new DepartmentFrameUI("직책관리");
							departmentUI.setOut(out);
							departmentUI.setTitleList(itemList);
							departmentUI.setVisible(true);
						} else {
							departmentUI.setTitleList(itemList);
						}
					}

					if (rep.getRes() == 1) {
						JOptionPane.showMessageDialog(null, rep.getMsg() + "성공");
						sendMessage(out, null, DepartmentCRUD.DEPARTMENT_LIST);
					}
					
					departmentUI.refreshUI();
				}
							
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("호스트와 연결이 끊김");
		}
	}

	private void sendMessage(DataOutputStream out, Department department, DepartmentCRUD msg) {
		Gson gson = new Gson();
		MessengerDepartment messenger = new MessengerDepartment( department, msg);
		String json = gson.toJson(messenger);
		System.out.println("json" + json);
		try {
			out.writeUTF(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}
	
}
