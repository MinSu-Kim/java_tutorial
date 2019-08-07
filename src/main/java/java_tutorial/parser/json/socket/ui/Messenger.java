package java_tutorial.parser.json.socket.ui;

import java_tutorial.parser.json.socket.dto.Title;

public class Messenger {
	public String msg;
	public Title title;
	
	public Messenger() {
		// TODO Auto-generated constructor stub
	}
	public Messenger(String msg, Title title) {
		this.msg = msg;
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Title getTitle() {
		return title;
	}
	public void setTitle(Title title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return String.format("Messenger [msg=%s, title=%s]", msg, title);
	}
	
}
