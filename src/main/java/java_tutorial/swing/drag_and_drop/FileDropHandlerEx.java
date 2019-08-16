package java_tutorial.swing.drag_and_drop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FileDropHandlerEx extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JList list;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileDropHandlerEx frame = new FileDropHandlerEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public FileDropHandlerEx() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 291);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		File userHome = new File(System.getProperty("user.dir"));
		File[] files = userHome.listFiles();

//		list = new JList<File>(files);
		list = new JList<File>();
		list.setDragEnabled(true);
		list.setCellRenderer(new FileListCell());
		list.setDropMode(DropMode.INSERT);

		list.setTransferHandler(new FileDropHandler());
		contentPane.add(list, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(this);
		panel.add(btnNewButton);
	}

	class FileDropHandler extends TransferHandler {
		@Override
		public boolean canImport(TransferHandler.TransferSupport support) {
			for (DataFlavor flavor : support.getDataFlavors()) {
				if (flavor.isFlavorJavaFileListType()) {
					return true;
				}
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean importData(TransferHandler.TransferSupport support) {
			if (!this.canImport(support))
				return false;

			try {
				List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
				DefaultListModel<File> model = new DefaultListModel<>();
				
				for (File file : files) {
					System.out.println(file.getAbsolutePath());
					model.addElement(file);
				}
				list.setModel(model);
				
			} catch (UnsupportedFlavorException | IOException ex) {
				return false;
			}
			return true;
		}
		
	}

	class FileListCell extends JLabel implements ListCellRenderer<File>{
		private FileSystemView fileSystemView;
		private Color backgroundSelectionColor = Color.CYAN;
		private Color backgroundNonSelectionColor = Color.WHITE;
		
		public FileListCell() {
			fileSystemView = FileSystemView.getFileSystemView();
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends File> list, File value, int index, boolean isSelected, boolean cellHasFocus) {
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
		if (e.getSource() == btnNewButton) {
			actionPerformedBtnNewButton(e);
		}
	}
	
	protected void actionPerformedBtnNewButton(ActionEvent e) {
		for(int i=0; i< list.getModel().getSize() ; i++) {
			File f = (File) list.getModel().getElementAt(i);
			System.out.println(f);
		}
	}
}
