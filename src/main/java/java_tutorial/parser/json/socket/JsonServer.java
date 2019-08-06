package java_tutorial.parser.json.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import java_tutorial.parser.json.Person;

public class JsonServer {
	public static final String HOST = "localhost";
	public static final int PORT = 12345;

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server started  at:  " + serverSocket);
		
		Socket activeSocket = serverSocket.accept();
		
		try (BufferedReader socketReader = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));){
			String inMsg = socketReader.readLine();
			parseJson(inMsg);
		}
		
		serverSocket.close();
	}

	private static void parseJson(String msg) {
		Gson gson = new Gson();
		Messenger ms = gson.fromJson(msg, Messenger.class);		
		Person person = ms.getPerson();
		
		System.out.println(ms.getMsg());
		System.out.println(person);
	}

	public static void handleClientRequest(Socket socket) {
		try (BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
//			BufferedWriter socketWriter = null;
			
//			socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String inMsg = null;
			while ((inMsg = socketReader.readLine()) != null) {
				System.out.println("Received from  client: " + inMsg);

//				String outMsg = inMsg;
//				socketWriter.write(outMsg);
//				socketWriter.write("\n");
//				socketWriter.flush();
			}
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
