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

import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.TitleFrameUI;
import java_tutorial.parser.json.socket.ui.enum_crud.TitleCRUD;
import java_tutorial.parser.json.socket.ui.msg.MessengerTitle;
import java_tutorial.parser.json.socket.ui.replymsg.ReplyTitle;

public class ReceiveTitleThread extends Thread {
	private TitleFrameUI titleFrame;
	private List<Title> itemList;
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
		
		sendMessage(out, null, TitleCRUD.TITLE_LIST);
	}



	@Override
	public void run() {
		System.out.println("receive");
		Gson gson = new Gson();
		String msg;
		ReplyTitle rep;

		try {
			while (true) {
				msg = in.readUTF();
				if (msg != null) {
					System.out.println("receive message " + msg);
					rep = gson.fromJson(msg, ReplyTitle.class);
					if (rep.getMsg()==TitleCRUD.TITLE_LIST) {
						itemList = gson.fromJson(rep.getStrToJson(), new TypeToken<List<Title>>() {}.getType());
						if (titleFrame == null) {
							titleFrame = new TitleFrameUI("직책관리");
							titleFrame.setOut(out);
							titleFrame.setTitleList(itemList);
							titleFrame.setVisible(true);
						} else {
							titleFrame.setTitleList(itemList);
						}
					}

					if (rep.getRes() == 1) {
						JOptionPane.showMessageDialog(null, rep.getMsg() + "성공");
						sendMessage(out, null, TitleCRUD.TITLE_LIST);
					}
					
					titleFrame.refreshUI();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("호스트와 연결이 끊김");
		}
	}

	private void sendMessage(DataOutputStream out, Title title, TitleCRUD msg) {
		Gson gson = new Gson();
		MessengerTitle messenger = new MessengerTitle( title, msg);
		String json = gson.toJson(messenger);
		System.out.println("json" + json);
		try {
			out.writeUTF(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
