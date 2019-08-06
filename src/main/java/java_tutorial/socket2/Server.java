package java_tutorial.socket2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends Thread {
	public static final int PORT = 12345;
	public static final String HOST = "localhost";
	
	private Map<String, DataOutputStream> mClients;
	private List<String> nickNames;
	private ServerSocket ss;
	
	public Server() {
		mClients = new HashMap<String, DataOutputStream>();
		nickNames = new ArrayList<String>();
		Collections.synchronizedMap(mClients);
	}
	
	@Override
	public void run() {
		try {
			ss = new ServerSocket(PORT);
		} catch (IOException e1) {
			System.err.println("해당 포트를 열수 없습니다");
			System.exit(1);
		}
		
		while(true){
			Socket soc = null;
			try {
				soc = ss.accept();
				DataInputStream br = new DataInputStream(soc.getInputStream());
				String nickName = br.readUTF();
				
				ServerThread clientThread = new ServerThread(soc, mClients, nickNames, nickName);
				clientThread.start();
			} catch (IOException e) {
				System.out.println("accept error " + e);
				return;
			} 
		} 
	}


}
