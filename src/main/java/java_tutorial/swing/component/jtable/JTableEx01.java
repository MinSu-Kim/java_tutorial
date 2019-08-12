package java_tutorial.swing.component.jtable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

@SuppressWarnings("serial")

public class JTableEx01 extends JFrame {


	private JPanel contentPane;

	private JTable table;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {

					JTableEx01 frame = new JTableEx01();

					frame.setVisible(true);

				} catch (Exception e) {

					e.printStackTrace();

				}

			}

		});

	}

	public JTableEx01() {

		initComponents();

		table.getSelectionModel().setSelectionInterval(1, 1);

	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 450, 300);

		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		contentPane.setLayout(new BorderLayout(0, 0));

		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();

		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();

		table.setPreferredScrollableViewportSize(new Dimension(500, 70));

		table.setFillsViewportHeight(true);

		scrollPane.setViewportView(table);

		loadDatas();

	}

	private Object[][] getDatas() {

		return new Object[][] { { "블랙리스트", true, Color.DARK_GRAY, "bb", null },

				{ "리스트", false, Color.GRAY, "bb", null }, { "블랙리스트", true, Color.GREEN, "bb", null },

				{ "리스트", false, Color.RED, "bb", null }, { "블랙리스트", false, Color.BLUE, "bb", null },
				{ "리스트", true, Color.CYAN, "bb", null }, };

	}

	private Object[] getColumnNames() {
		return new Object[] { "셀색적용", "checkbox표시", "셀배경색표시", "New column", "New column" };

	}

	public void loadDatas() {
		MyTableModel model = new MyTableModel(getDatas(), getColumnNames());
		table.setModel(model);
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		setAlignWidth(); // table.setModel 다음에 데이터가 들어오기 때문에 그 다음에 setAlignWidth() 호출해야 함.
	}

	private void setAlignWidth() {
		tableCellAlignment(JLabel.CENTER, 0, 1, 2, 3, 4);// ReturnTableCellRenderer jlabel상속
		tableSetWidth(120, 120, 80, 80, 80);

	}

	// 정렬
	protected void tableCellAlignment(int align, int... idx) {
		ReturnTableCellRenderer dtcr = new ReturnTableCellRenderer();
		dtcr.setHorizontalAlignment(align);

		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < idx.length; i++) {
			tcm.getColumn(idx[i]).setCellRenderer(dtcr);
		}

	}

	// 너비
	protected void tableSetWidth(int... width) {
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(1).setCellRenderer(new SelectTableCellRenderer());
		tcm.getColumn(2).setCellRenderer(new ColorRenderer());
		for (int i = 0; i < width.length; i++) {
			tcm.getColumn(i).setPreferredWidth(width[i]);
		}

	}

	private class ReturnTableCellRenderer extends JLabel implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText(value == null ? "" : value.toString());
			setOpaque(true);
			if (table.getValueAt(row, 0).toString().equals("블랙리스트")) {
				setBackground(new Color(255, 0, 0, 40));
			} else {
				setBackground(Color.WHITE);
			}

			if (isSelected) {
				setBackground(Color.LIGHT_GRAY);
			}
			return this;
		}

	}

	private class SelectTableCellRenderer extends JCheckBox implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			boolean boolWrapper = (Boolean) value;
			setSelected(boolWrapper);
			setHorizontalAlignment(CENTER);
			if (isSelected) {
				setBackground(Color.LIGHT_GRAY);
			} else {
				setBackground(Color.WHITE);
			}
			return this;
		}

	}

	class ColorRenderer extends JLabel implements TableCellRenderer {

		Border unselectedBorder = null;
		Border selectedBorder = null;
		boolean isBordered;
		public ColorRenderer() {
			setOpaque(true); // MUST do this for background to show up.
		}

		public ColorRenderer(boolean isBordered) {
			this.isBordered = isBordered;
			setOpaque(true); // MUST do this for background to show up.
		}

		public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Color newColor = (Color) color;
			setBackground(newColor);
//			if (isBordered) {
//				if (isSelected) {
//					if (selectedBorder == null) {
//						selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
//					}
//					setBorder(selectedBorder);
//				} else {
//					if (unselectedBorder == null) {
//						unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
//					}
//					setBorder(unselectedBorder);
//				}
//			}

			setToolTipText("RGB value: " + newColor.getRed() + ", " + newColor.getGreen() + ", " + newColor.getBlue());
			return this;

		}

	}

	// 내부클래스
	private class MyTableModel extends DefaultTableModel {
		public MyTableModel(Object[][] data, Object[] columnNames) {
			super(data, columnNames);
		}

		/**
		 *  테이블 셀내용 수정 불가능
		*/
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public Class<?> getColumnClass(int column) {
			Class<?> returnValue;
			if ((column >= 0) && (column < getColumnCount()) && (getRowCount() > 0)) {
				returnValue = getValueAt(0, column).getClass();
			} else {
				returnValue = Object.class;
			}
			return returnValue;
		}

	}

}