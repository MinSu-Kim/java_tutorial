package java_tutorial.socket2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ServerThread extends Thread {
	private Socket clientSocket;
	private DataOutputStream bw;
	
	private Map<String, DataOutputStream> mClients;
	private List<String> nickNames;
	
	private String clientKey;
	private String nickName;
	private DataInputStream br;
	
	public ServerThread(Socket clientSocket, Map<String, DataOutputStream> mClients, String nickName) {
		this.clientSocket = clientSocket;
		this.mClients = mClients;
		this.nickName = nickName;
		this.nickNames = new ArrayList<String>();
	}
	
	@Override
	public void run() {
		try {
			br = new DataInputStream(clientSocket.getInputStream());
			clientKey = nickName + clientSocket.getInetAddress().toString();
			bw = new DataOutputStream(clientSocket.getOutputStream());
			mClients.put(clientKey, bw);
			nickNames.add(nickName);
			
			sendBroadCast("cnt:" + mClients.size());
			
			StringBuilder sb = new StringBuilder();
			for(String s : nickNames) {
				sb.append(s + ",");
			}
			sb.replace(sb.length()-1, sb.length(), "");
			sendBroadCast("list:" + sb.toString());
			
			System.out.println(clientKey + " : " + clientSocket + "[" + nickName + "] ´Ô Á¢¼Ó");
			
			String msg = "";
			while(true) {
				try {
					msg = br.readUTF();
					if (!msg.toLowerCase().equals("exit")) {
						sendBroadCast("[" + nickName + "]" + msg);
					}else {
						sendBroadCast("[" + nickName + "]´ÔÀÌ ³ª°¡¼Ì½À´Ï´Ù.");
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
