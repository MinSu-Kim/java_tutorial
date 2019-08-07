package java_tutorial.parser.json.socket.ui.list;

import javax.swing.SwingConstants;

import java_tutorial.parser.json.socket.dto.Department;

@SuppressWarnings("serial")
public class DepartmentList extends AbstractList<Department> {

	public DepartmentList(String title) {
		super(title);
	}

	@Override
	protected void tableAlignmentAndWidth() {
		// 직책번호, 직책명�? �??��?�� ?��?��
		tableCellAlignment(SwingConstants.CENTER, 0, 1, 2);
		// 직책번호, 직책명의 ?��?�� (100, 200)?���? �??��?���? ?��?��
		tableSetWidth(100, 250, 100);
	}

	@Override
	protected Object[] toArray(int idx) {
		Department department = itemList.get(idx);
		return department.toArray();
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] { "�??��번호", "�??���?", "?���?" };
	}

}
