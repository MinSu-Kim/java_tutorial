package java_tutorial.parser.json.socket.ui;

import java.util.List;

import java_tutorial.parser.json.socket.dto.Title;

public class Reply {
	private String msg;
	private int res;
	private String strToJson;

	public Reply(String msg, String strToJson) {
		this.msg = msg;
		this.strToJson = strToJson;
	}

	public Reply(String msg, int res) {
		this.msg = msg;
		this.res = res;
	}

	public String getStrToJson() {
		return strToJson;
	}

	public void setStrToJson(String strToJson) {
		this.strToJson = strToJson;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}
	
	
}
