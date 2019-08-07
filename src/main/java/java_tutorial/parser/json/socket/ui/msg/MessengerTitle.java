package java_tutorial.parser.json.socket.ui.msg;

import java_tutorial.parser.json.socket.dto.Title;
import java_tutorial.parser.json.socket.ui.enum_crud.TitleCRUD;

public class MessengerTitle {
	public TitleCRUD msg;
	public Title title;

	public MessengerTitle() {
		// TODO Auto-generated constructor stub
	}

	public MessengerTitle(Title title, TitleCRUD msg) {
		this.msg = msg;
		this.title = title;
	}

	public TitleCRUD getMsg() {
		return msg;
	}

	public void setMsg(TitleCRUD msg) {
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
