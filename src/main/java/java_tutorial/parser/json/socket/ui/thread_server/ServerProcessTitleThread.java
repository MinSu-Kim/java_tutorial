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

import java_tutorial.parser.json.socket.dao.TitleDao;
import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.enum_crud.TitleCRUD;
import java_tutorial.parser.json.socket.ui.msg_reply.ReplyTitle;
import java_tutorial.parser.json.socket.ui.msg_send.MessengerTitle;

public class ServerProcessTitleThread extends Thread {
		private Socket socket;
		private TitleDao titleDao;
		
		public ServerProcessTitleThread(Socket socket, TitleDao titleDao) {
			this.socket = socket;
			this.titleDao = titleDao;
		}
		
		@Override
		public void run() {
				ServerTitleReceiver titleTh = new ServerTitleReceiver(); // 상대로부터 메시지 수신을 위한 스레드 생성
				titleTh.setDao(titleDao);
				titleTh.setSocket(socket);
				titleTh.start();
		}
		
		private class ServerTitleReceiver extends Thread {

			private DataInputStream in;
			private DataOutputStream out;
			private TitleDao dao;

			private Gson gson;

			public ServerTitleReceiver() {
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

			public void setDao(TitleDao dao) {
				this.dao = dao;
			}

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
					System.out.println("클라이언트와 연결이 끊어 졌습니다.");
				} catch (EOFException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
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
				String listToJson = gson.toJson(list, new TypeToken<List<Title>>() {
				}.getType());

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

	}