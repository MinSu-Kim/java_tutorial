package java_tutorial.socket2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;

public class ServerThread extends Thread {
	private Socket clientSocket;
	private DataOutputStream bw;
	
	private Map<String, DataOutputStream> mClients;
	private String clientKey;
	private String nickName;
	private DataInputStream br;
	
	public ServerThread(Socket clientSocket, Map<String, DataOutputStream> mClients, String nickName) {
		this.clientSocket = clientSocket;
		this.mClients = mClients;
		this.nickName = nickName;
	}
	
	@Override
	public void run() {
		try {
			br = new DataInputStream(clientSocket.getInputStream());
			clientKey = nickName + clientSocket.getInetAddress().toString();
			bw = new DataOutputStream(clientSocket.getOutputStream());
			mClients.put(clientKey, bw);
			
			System.out.println(clientSocket + "[" + nickName + "] �� ����");
			
			String msg = "";
			while(true) {
				try {
					msg = br.readUTF();
					if (!msg.toLowerCase().equals("exit")) {
						sendBroadCast("[" + nickName + "]" + msg);
					}else {
						sendBroadCast("[" + nickName + "]���� �����̽��ϴ�.");
						break;
					}
				}catch(EOFException e) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				mClients.remove(clientKey);
				bw.close();
				br.close();
				clientSocket.close();
			}catch (IOException e) {
			}
		}
	}

	private void sendBroadCast(String msg) {
		for(Entry<String, DataOutputStream> e : mClients.entrySet()) {
			try {
				e.getValue().writeUTF(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
				mClients.remove(e.getKey());
			}
		}
		
	}
}
