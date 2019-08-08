package java_tutorial.parser.json.socket.ui.msg_reply;

import java_tutorial.parser.json.socket.ui.enum_crud.DepartmentCRUD;

public class ReplyDepartment {
	private DepartmentCRUD msg;
	private int res;
	private String strToJson;

	public ReplyDepartment(DepartmentCRUD msg, String strToJson) {
		this.msg = msg;
		this.strToJson = strToJson;
	}

	public ReplyDepartment(DepartmentCRUD msg, int res) {
		this.msg = msg;
		this.res = res;
	}

	public String getStrToJson() {
		return strToJson;
	}

	public void setStrToJson(String strToJson) {
		this.strToJson = strToJson;
	}

	public DepartmentCRUD getMsg() {
		return msg;
	}

	public void setMsg(DepartmentCRUD msg) {
		this.msg = msg;
	}

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

}
