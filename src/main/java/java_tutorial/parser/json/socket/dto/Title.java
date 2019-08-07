package java_tutorial.parser.json.socket.dto;

public class Title {
	private int titleCode;
	private String titleName;

	public Title() {
		// TODO Auto-generated constructor stub
	}

	public Title(int titleNo) {
		this.titleCode = titleNo;
	}

	public Title(int titleNo, String titleName) {
		this.titleCode = titleNo;
		this.titleName = titleName;
	}

	public int getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(int titleCode) {
		this.titleCode = titleCode;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + titleCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Title other = (Title) obj;
		if (titleCode != other.titleCode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s-%s", titleCode, titleName);
	}

	public Object[] toArray() {
		return new Object[] { String.format("T%03d", titleCode), titleName };
	}
}
