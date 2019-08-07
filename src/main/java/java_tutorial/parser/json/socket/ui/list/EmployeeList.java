package java_tutorial.parser.json.socket.ui.list;

import javax.swing.SwingConstants;

import java_tutorial.parser.json.socket.dto.Employee;

@SuppressWarnings("serial")
public class EmployeeList extends AbstractList<Employee> {

	public EmployeeList() {
		super("?��?��");
	}

	@Override
	protected void tableAlignmentAndWidth() {
		// ?��?��번호(0), ?��?���?(1), 급여(2), �??��(3), ?���?(4), ?��?��?��(5), 직책(6)
		tableCellAlignment(SwingConstants.CENTER, 0, 1, 3, 4, 5, 6);
		tableCellAlignment(SwingConstants.RIGHT, 2);
		
		// 직책번호, 직책명의 ?��?�� (100, 200)?���? �??��?���? ?��?��
		tableSetWidth(100, 150, 150, 100, 50, 100, 100);
	}

	@Override
	protected Object[] toArray(int idx) {
		Employee emp = itemList.get(idx);
		return emp.toArray();
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {"?��?��번호", "?��?���?", "급여", "�??��", "?���?", "?��?��?��", "직책"};
	}

}
