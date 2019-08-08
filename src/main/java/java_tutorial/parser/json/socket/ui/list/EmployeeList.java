package java_tutorial.parser.json.socket.ui.list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java_tutorial.parser.json.socket.dto.Employee;

@SuppressWarnings("serial")
public class EmployeeList extends JPanel {
	private JTable table;
	protected List<Employee> itemList;
	private JScrollPane scrollPane;
	
	public EmployeeList(String title) {
		initComponents(title);
	}

	private void initComponents(String title) {
		setBorder(new TitledBorder(null, title + "목록", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(500, 200));
		scrollPane.setViewportView(table);
	}
	public void setPopupMenu(JPopupMenu popupMenu) {
		table.setComponentPopupMenu(popupMenu);
		scrollPane.setComponentPopupMenu(popupMenu);
	}

	public void setItemList(List<Employee> itemList) {
		this.itemList = itemList;
	}
	
	protected void tableAlignmentAndWidth() {
		tableCellAlignment(SwingConstants.CENTER, 0, 1, 3, 4, 5, 6);
		tableCellAlignment(SwingConstants.RIGHT, 2);
		
		tableSetWidth(100, 150, 150, 100, 50, 100, 100);
	}

	protected Object[] toArray(int idx) {
		Employee emp = itemList.get(idx);
		return emp.toArray();
	}

	protected String[] getColumnNames() {
		return new String[] {"사원번호", "사원명", "급여", "부서", "성별", "입사일", "직책"};
	}

	public void reloadData() {
		table.setModel(new MyTableModel(getRows(), getColumnNames()));
		tableAlignmentAndWidth();
	}

	private Object[][] getRows() {
		if (itemList==null) {
			itemList = new ArrayList<>();
		}
		Object[][] rows = new Object[itemList.size()][];
		for (int i = 0; i < itemList.size(); i++) {
			rows[i] = toArray(i);
		}
		return rows;
	}

	protected void tableCellAlignment(int align, int... idx) {
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(align);

		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < idx.length; i++) {
			model.getColumn(idx[i]).setCellRenderer(dtcr);
		}
	}

	protected void tableSetWidth(int... width) {
		TableColumnModel cModel = table.getColumnModel();

		for (int i = 0; i < width.length; i++) {
			cModel.getColumn(i).setPreferredWidth(width[i]);
		}
	}

	public Employee getSelectedItem() {
		int i = table.getSelectedRow();
		Employee item = null;
		if (table.getModel().getRowCount() == 0) {
			return item;
		}
		if (i < 0 || i > table.getModel().getRowCount() - 1) {
			JOptionPane.showMessageDialog(null, "해당 직책을 선택하세요");
			return item;
		}

		item = itemList.get(i);
		return item;
	}
	
	private class MyTableModel extends DefaultTableModel {

		public MyTableModel(Object[][] data, Object[] columnNames) {
			super(data, columnNames);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
}
