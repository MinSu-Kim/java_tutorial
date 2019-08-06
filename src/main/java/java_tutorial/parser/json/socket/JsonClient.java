package java_tutorial.parser.json.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.Gson;

import java_tutorial.parser.json.Person;

public class JsonClient {
	public static void main(String[] args) {
		Socket soc;
		try {
			soc = new Socket(JsonServer.HOST, JsonServer.PORT);
			BufferedWriter bos = new BufferedWriter( new OutputStreamWriter(soc.getOutputStream()));
			String json = getObjectToJson();
			bos.write(json);
			bos.flush();
			soc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String getObjectToJson() {
		Messenger messenger = new Messenger("insert", new Person("kim", 20, "M"));
		
		Gson gson = new Gson();
		String json = gson.toJson(messenger);
		return json;
	}
	
}