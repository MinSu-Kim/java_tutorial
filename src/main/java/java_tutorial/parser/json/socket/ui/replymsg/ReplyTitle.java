package java_tutorial.parser.json.socket.ui.replymsg;

import java_tutorial.parser.json.socket.ui.enum_crud.TitleCRUD;

public class ReplyTitle {
	private TitleCRUD msg;
	private int res;
	private String strToJson;

	public ReplyTitle(TitleCRUD msg, String strToJson) {
		this.msg = msg;
		this.strToJson = strToJson;
	}

	public ReplyTitle(TitleCRUD msg, int res) {
		this.msg = msg;
		this.res = res;
	}

	public String getStrToJson() {
		return strToJson;
	}

	public void setStrToJson(String strToJson) {
		this.strToJson = strToJson;
	}

	public TitleCRUD getMsg() {
		return msg;
	}

	public void setMsg(TitleCRUD msg) {
		this.msg = msg;
	}

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

}
