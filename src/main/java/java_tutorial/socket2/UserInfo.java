package java_tutorial.socket2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class UserInfo {
	public Socket socket;
	public BufferedWriter out;
	public BufferedReader in;
	public String nickName;

	public UserInfo(Socket socket) {
		this.socket = socket;
		try {
			out = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			nickName = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getNickName() {
		return nickName;
	}

	@Override
	public String toString() {
		return String.format("UserInfo [socket=%s, out=%s, in=%s, nickName=%s]", socket, out, in, nickName);
	}
	
	
}