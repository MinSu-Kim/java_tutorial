package java_tutorial.parser.json.socket.ui.list;

import javax.swing.SwingConstants;

import java_tutorial.parser.json.socket.dto.Title;

@SuppressWarnings("serial")
public class TitleList extends AbstractList<Title> {
	
	public TitleList() {
		super("직책");
	}
	
	@Override
	protected void tableAlignmentAndWidth() {
		// 직책번호, 직책명�? �??��?�� ?��?��
		tableCellAlignment(SwingConstants.CENTER, 0, 1);
		// 직책번호, 직책명의 ?��?�� (100, 200)?���? �??��?���? ?��?��
		tableSetWidth(150, 300);
	}

	@Override
	protected Object[] toArray(int idx) {
		Title title = itemList.get(idx);
		return title.toArray();
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] { "직책번호", "직책�?"};
	}

}
