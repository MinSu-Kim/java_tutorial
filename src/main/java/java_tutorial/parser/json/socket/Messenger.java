package java_tutorial.parser.json.socket;

import java_tutorial.parser.json.Person;

public class Messenger {
	public String msg;
	public Person person;

	public Messenger(String msg, Person person) {
		this.msg = msg;
		this.person = person;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "Messenger [msg=" + msg + ", person=" + person + "]";
	}

}