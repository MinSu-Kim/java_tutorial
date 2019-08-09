package java_tutorial.swing.component.jtable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public final class MainPanel extends JPanel {
	private final Object[] columnNames = { Status.INDETERMINATE, "Integer", "String" };
	private final Object[][] data = { { true, 1, "BBB" }, { false, 12, "AAA" }, { true, 2, "DDD" }, { false, 5, "CCC" },
			{ true, 3, "EEE" }, { false, 6, "GGG" }, { true, 4, "FFF" }, { false, 7, "HHH" } };

	private final TableModel model = new DefaultTableModel(data, columnNames) {
		@Override
		public Class<?> getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}
	};

	private final JTable table = new JTable(model) {
		protected static final int CHECKBOX_COLUMN = 0;
		protected transient HeaderCheckBoxHandler handler;

		@Override
		public void updateUI() {
			setSelectionForeground(new ColorUIResource(Color.RED));
			setSelectionBackground(new ColorUIResource(Color.RED));
			getTableHeader().removeMouseListener(handler);
			TableModel m = getModel();
			if (Objects.nonNull(m)) {
				m.removeTableModelListener(handler);
			}
			super.updateUI();

			m = getModel();
			for (int i = 0; i < m.getColumnCount(); i++) {
				TableCellRenderer r = getDefaultRenderer(m.getColumnClass(i));
				if (r instanceof Component) {
					SwingUtilities.updateComponentTreeUI((Component) r);
				}
			}
			TableColumn column = getColumnModel().getColumn(CHECKBOX_COLUMN);
			column.setHeaderRenderer(new HeaderRenderer());
			column.setHeaderValue(Status.INDETERMINATE);

			handler = new HeaderCheckBoxHandler(this, CHECKBOX_COLUMN);
			m.addTableModelListener(handler);
			getTableHeader().addMouseListener(handler);
		}

		@Override
		public Component prepareEditor(TableCellEditor editor, int row, int column) {
			Component c = super.prepareEditor(editor, row, column);
			if (c instanceof JCheckBox) {
				JCheckBox b = (JCheckBox) c;
				b.setBackground(getSelectionBackground());
				b.setBorderPainted(true);
			}
			return c;
		}
	};

	public MainPanel() {
		super(new BorderLayout());
		table.setFillsViewportHeight(true);
		add(new JScrollPane(table));
		setPreferredSize(new Dimension(320, 240));
	}

	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGui();
			}
		});
	}

	public static void createAndShowGui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}
		JFrame frame = new JFrame("TableHeaderCheckBox");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MainPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}

class HeaderRenderer implements TableCellRenderer {
	private final JCheckBox check = new JCheckBox("");
	private final JLabel label = new JLabel("Check All");

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value instanceof Status) {
			switch ((Status) value) {
			case SELECTED:
				check.setSelected(true);
				check.setEnabled(true);
				break;
			case DESELECTED:
				check.setSelected(false);
				check.setEnabled(true);
				break;
			case INDETERMINATE:
				check.setSelected(true);
				check.setEnabled(false);
				break;
			default:
				throw new AssertionError("Unknown Status");
			}
		} else {
			check.setSelected(true);
			check.setEnabled(false);
		}
		check.setOpaque(false);
		check.setFont(table.getFont());
		TableCellRenderer r = table.getTableHeader().getDefaultRenderer();
		JLabel l = (JLabel) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setIcon(new ComponentIcon(check));
		l.setIcon(new ComponentIcon(label));
		l.setText(null); // XXX: Nimbus???
		return l;
	}
}

class HeaderCheckBoxHandler extends MouseAdapter implements TableModelListener {
	private final JTable table;
	private final int targetColumnIndex;

	protected HeaderCheckBoxHandler(JTable table, int index) {
		super();
		this.table = table;
		this.targetColumnIndex = index;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == targetColumnIndex) {
			int vci = table.convertColumnIndexToView(targetColumnIndex);
			TableColumn column = table.getColumnModel().getColumn(vci);
			Object status = column.getHeaderValue();
			TableModel m = table.getModel();
			if (m instanceof DefaultTableModel && fireUpdateEvent((DefaultTableModel) m, column, status)) {
				JTableHeader h = table.getTableHeader();
				h.repaint(h.getHeaderRect(vci));
			}
		}
	}

	private boolean fireUpdateEvent(DefaultTableModel m, TableColumn column, Object status) {
		if (Status.INDETERMINATE.equals(status)) {
			List<Boolean> l = ((Vector<?>) m.getDataVector()).stream()
					.map(v -> (Boolean) ((Vector<?>) v).get(targetColumnIndex)).distinct().collect(Collectors.toList());
			boolean isOnlyOneSelected = l.size() == 1;
			if (isOnlyOneSelected) {
				column.setHeaderValue(l.get(0) ? Status.SELECTED : Status.DESELECTED);
				return true;
			} else {
				return false;
			}
		} else {
			column.setHeaderValue(Status.INDETERMINATE);
			return true;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JTableHeader header = (JTableHeader) e.getComponent();
		JTable tbl = header.getTable();
		TableColumnModel columnModel = tbl.getColumnModel();
		TableModel m = tbl.getModel();
		int vci = columnModel.getColumnIndexAtX(e.getX());
		int mci = tbl.convertColumnIndexToModel(vci);
		if (mci == targetColumnIndex && m.getRowCount() > 0) {
			TableColumn column = columnModel.getColumn(vci);
			Object v = column.getHeaderValue();
			boolean b = Status.DESELECTED.equals(v);
			for (int i = 0; i < m.getRowCount(); i++) {
				m.setValueAt(b, i, mci);
			}
			column.setHeaderValue(b ? Status.SELECTED : Status.DESELECTED);
			// header.repaint();
		}
	}
}

class ComponentIcon implements Icon {
	private final Component cmp;

	protected ComponentIcon(Component cmp) {
		this.cmp = cmp;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		SwingUtilities.paintComponent(g, cmp, c.getParent(), x, y, getIconWidth(), getIconHeight());
	}

	@Override
	public int getIconWidth() {
		return cmp.getPreferredSize().width;
	}

	@Override
	public int getIconHeight() {
		return cmp.getPreferredSize().height;
	}
}

enum Status {
	SELECTED, DESELECTED, INDETERMINATE
}
