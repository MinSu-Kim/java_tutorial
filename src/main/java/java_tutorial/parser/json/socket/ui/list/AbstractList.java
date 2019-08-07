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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public abstract class AbstractList<T> extends JPanel {
	private JTable table;
	protected List<T> itemList;
	private JScrollPane scrollPane;

	public AbstractList(String title) {
		initComponents(title);
	}

	private void initComponents(String title) {
		setBorder(new TitledBorder(null, title + "Î™©Î°ù", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400, 200));
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);
	}

	public void setPopupMenu(JPopupMenu popupMenu) {
		table.setComponentPopupMenu(popupMenu);
		scrollPane.setComponentPopupMenu(popupMenu);
	}

	public void setItemList(List<T> itemList) {
		this.itemList = itemList;
	}

	public void reloadData() {
		table.setModel(new DefaultTableModel(getRows(), getColumnNames()));
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

	protected abstract void tableAlignmentAndWidth();

	protected abstract Object[] toArray(int idx);

	protected abstract String[] getColumnNames();
	
	// ?Öå?ù¥Î∏? ?? ?Ç¥?ö©?ùò ?†ï?†¨
	protected void tableCellAlignment(int align, int... idx) {
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(align);

		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < idx.length; i++) {
			model.getColumn(idx[i]).setCellRenderer(dtcr);
		}
	}

	// ?Öå?ù¥Î∏? ???ùò ?è≠ ?Ñ§?†ï
	protected void tableSetWidth(int... width) {
		TableColumnModel cModel = table.getColumnModel();

		for (int i = 0; i < width.length; i++) {
			cModel.getColumn(i).setPreferredWidth(width[i]);
		}
	}

	public T getSelectedItem() {
		int i = table.getSelectedRow();
		T item = null;
		if (table.getModel().getRowCount() == 0) {
			return item;
		}
		if (i < 0 || i > table.getModel().getRowCount() - 1) {
			JOptionPane.showMessageDialog(null, "?Ñ†?Éù?êú Î∂??ÑúÍ∞? ?óÜ?äµ?ãà?ã§.");
			return item;
		}

		item = itemList.get(i);
		return item;
	}
}
