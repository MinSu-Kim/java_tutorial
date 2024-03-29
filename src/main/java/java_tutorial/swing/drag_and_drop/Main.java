package java_tutorial.swing.drag_and_drop;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

@SuppressWarnings("serial")
public class Main extends JFrame {
	private JTextField newTextField = new JTextField(10);
	private JList<String> sourceList = new JList<>(new DefaultListModel<>());
	private JList<String> destList = new JList<>(new DefaultListModel<>());

	public Main() {

		for (int i = 0; i < 5; i++) {
			((DefaultListModel<String>) sourceList.getModel()).add(i, "A " + i);
			((DefaultListModel<String>) destList.getModel()).add(i, "B " + i);
		}
		Box nameBox = Box.createHorizontalBox();
		nameBox.add(new JLabel("New:"));
		nameBox.add(newTextField);

		Box sourceBox = Box.createVerticalBox();
		sourceBox.add(new JLabel("Source"));
		sourceBox.add(new JScrollPane(sourceList));

		Box destBox = Box.createVerticalBox();
		destBox.add(new JLabel("Destination"));
		destBox.add(new JScrollPane(destList));

		Box listBox = Box.createHorizontalBox();
		listBox.add(sourceBox);
		listBox.add(destBox);

		Box allBox = Box.createVerticalBox();
		allBox.add(nameBox);
		allBox.add(listBox);

		this.getContentPane().add(allBox, BorderLayout.CENTER);

		sourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		destList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		newTextField.setDragEnabled(true);
		sourceList.setDragEnabled(true);
		destList.setDragEnabled(true);

		sourceList.setDropMode(DropMode.INSERT);
		destList.setDropMode(DropMode.INSERT);

		sourceList.setTransferHandler(new ListTransferHandler());
		destList.setTransferHandler(new ListTransferHandler());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Main frame = new Main();
			frame.pack();
			frame.setVisible(true);
		});
	}
}

@SuppressWarnings("serial")
class ListTransferHandler extends TransferHandler {
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Transferable createTransferable(JComponent source) {
		JList<String> sourceList = (JList<String>) source;
		String data = sourceList.getSelectedValue();
		Transferable t = new StringSelection(data);
		return t;
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		@SuppressWarnings("unchecked")
		JList<String> sourceList = (JList<String>) source;
		String movedItem = sourceList.getSelectedValue();
		if (action == TransferHandler.MOVE) {
			DefaultListModel<String> listModel = (DefaultListModel<String>) sourceList.getModel();
			listModel.removeElement(movedItem);
		}
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport support) {
		if (!support.isDrop()) {
			return false;
		}
		return support.isDataFlavorSupported(DataFlavor.stringFlavor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		if (!this.canImport(support)) {
			return false;
		}
		Transferable t = support.getTransferable();
		String data = null;
		try {
			data = (String) t.getTransferData(DataFlavor.stringFlavor);
			if (data == null) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JList.DropLocation dropLocation = (JList.DropLocation) support.getDropLocation();
		int dropIndex = dropLocation.getIndex();
		JList<String> targetList = (JList<String>) support.getComponent();
		DefaultListModel<String> listModel = (DefaultListModel<String>) targetList.getModel();
		if (dropLocation.isInsert()) {
			listModel.add(dropIndex, data);
		} else {
			listModel.set(dropIndex, data);
		}
		return true;
	}
}