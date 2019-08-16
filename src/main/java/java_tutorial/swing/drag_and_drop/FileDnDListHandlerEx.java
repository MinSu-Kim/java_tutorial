package java_tutorial.swing.drag_and_drop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import java_tutorial.swing.drag_and_drop.file.FileDrop.TransferableObject;

@SuppressWarnings("serial")
public class FileDnDListHandlerEx extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JList<File> list;
	private JButton btnNewButton;
	private JPopupMenu popupMenu;
	private JMenuItem mntmDel;
	private DefaultListModel<File> model;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileDnDListHandlerEx frame = new FileDnDListHandlerEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FileDnDListHandlerEx() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 291);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		model = new DefaultListModel<File>();
		list = new JList<>(model);
		list.setDragEnabled(true);
		list.setTransferHandler(new FileDropHandler());
		list.setCellRenderer(new FileListCell());
		list.setDropMode(DropMode.INSERT);
		contentPane.add(list, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(this);
		panel.add(btnNewButton);

		popupMenu = new JPopupMenu();
		list.setComponentPopupMenu(popupMenu);

		mntmDel = new JMenuItem("삭제");
		mntmDel.addActionListener(this);
		popupMenu.add(mntmDel);

		addCurDirToJList();
	}

	private void addCurDirToJList() {
		File userHome = new File(System.getProperty("user.dir"));
		File[] files = userHome.listFiles();

		for (File f : files) {
			model.addElement(f);
		}
	}

	class FileDropHandler extends TransferHandler {

		@Override
		public int getSourceActions(JComponent c) {
			return TransferHandler.COPY_OR_MOVE;
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
//			JList<File> sourceList = (JList<File>) c;
//			File data = sourceList.getSelectedValue();
//			TransferableObject t = new TransferableObject(data);
//			return t;
			return new TransferableObject(list.getSelectedValue());
		}

		@Override
		public boolean canImport(TransferHandler.TransferSupport support) {
			if (!support.isDrop()) {
				return false;
			}
			return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean importData(TransferHandler.TransferSupport support) {
			
			if (!this.canImport(support))
				return false;

			try {
				List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
				JList.DropLocation dropLocation = (JList.DropLocation) support.getDropLocation();
				int dropIndex = dropLocation.getIndex();
				for (File file : files) { 
					model.add(dropIndex++, file); 
				}
			} catch (UnsupportedFlavorException | IOException ex) {
				return false;
			}
			return true;
		}

		@Override
		protected void exportDone(JComponent source, Transferable data, int action) {
			File movedItem = list.getSelectedValue();
			if (action == TransferHandler.MOVE) {
				model.removeElement(movedItem);
			}
		}

	}

	class FileListCell extends JLabel implements ListCellRenderer<File> {
		private FileSystemView fileSystemView;
		private Color backgroundSelectionColor = Color.CYAN;
		private Color backgroundNonSelectionColor = Color.WHITE;

		public FileListCell() {
			fileSystemView = FileSystemView.getFileSystemView();
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends File> list, File value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setIcon(fileSystemView.getSystemIcon(value));
			setText(fileSystemView.getSystemDisplayName(value));
			setToolTipText(value.getPath());

			if (isSelected) {
				setBackground(backgroundSelectionColor);
			} else {
				setBackground(backgroundNonSelectionColor);
			}
			return this;
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmDel) {
			actionPerformedMntmDel(e);
		}
		if (e.getSource() == btnNewButton) {
			actionPerformedBtnNewButton(e);
		}
	}

	protected void actionPerformedBtnNewButton(ActionEvent e) {
		for (int i = 0; i < list.getModel().getSize(); i++) {
			File f = (File) list.getModel().getElementAt(i);
			System.out.println(f);
		}
	}

	protected void actionPerformedMntmDel(ActionEvent e) {
		int[] selIndexes = list.getSelectedIndices();
		for (int i = selIndexes.length - 1; i >= 0; i--) {
			model.removeElementAt(selIndexes[i]);
		}

	}

}
