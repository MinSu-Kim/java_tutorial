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

import java_tutorial.parser.json.socket.dto.Title;

@SuppressWarnings("serial")
public class TitleList extends JPanel {
	private JTable table;
	protected List<Title> itemList;
	private JScrollPane scrollPane;
	
	public TitleList(String title) {
		initComponents(title);
	}

	private void initComponents(String title) {
		setBorder(new TitledBorder(null, title , TitledBorder.LEADING, TitledBorder.TOP, null, null));
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

	public void setItemList(List<Title> itemList) {
		this.itemList = itemList;
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

	public Title getSelectedItem() {
		int i = table.getSelectedRow();
		Title item = null;
		if (table.getModel().getRowCount() == 0) {
			return item;
		}
		if (i < 0 || i > table.getModel().getRowCount() - 1) {
			JOptionPane.showMessageDialog(null, "직책을 선택하세요.");
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
	
	protected void tableAlignmentAndWidth() {
		tableCellAlignment(SwingConstants.CENTER, 0, 1);
		tableSetWidth(150, 300);
	}

	protected Object[] toArray(int idx) {
		Title title = itemList.get(idx);
		return title.toArray();
	}

	protected String[] getColumnNames() {
		return new String[] { "직책번호", "직책명"};
	}

}
