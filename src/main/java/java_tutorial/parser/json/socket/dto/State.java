package java_tutorial.parser.json.socket.dto;

public class State {
	private String deptName;
	private int total;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return String.format("State [deptName=%s, total=%s]", deptName, total);
	}

}
