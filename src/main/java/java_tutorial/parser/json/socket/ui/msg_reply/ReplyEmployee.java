package java_tutorial.parser.json.socket.ui.msg_reply;

import java_tutorial.parser.json.socket.ui.enum_crud.EmployeeCRUD;

public class ReplyEmployee {
	private EmployeeCRUD msg;
	private int res;
	private String strToJson;

	public ReplyEmployee(EmployeeCRUD msg, String strToJson) {
		this.msg = msg;
		this.strToJson = strToJson;
	}

	public ReplyEmployee(EmployeeCRUD msg, int res) {
		this.msg = msg;
		this.res = res;
	}

	public String getStrToJson() {
		return strToJson;
	}

	public void setStrToJson(String strToJson) {
		this.strToJson = strToJson;
	}

	public EmployeeCRUD getMsg() {
		return msg;
	}

	public void setMsg(EmployeeCRUD msg) {
		this.msg = msg;
	}

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

}
