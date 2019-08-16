package java_tutorial.swing.drag_and_drop;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class FileDropHandlerEx extends JFrame {

	private JPanel contentPane;
	private JTextArea list;

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
	public FileDropHandlerEx() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 682, 493);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		list = new JTextArea();
		list.setDragEnabled(true);
		
		list.setDropMode(DropMode.INSERT);
		
		list.setTransferHandler(new FileDropHandler());
		
		
		contentPane.add(list, BorderLayout.CENTER);
	}

	public class FileDropHandler extends TransferHandler {
		@Override
		public boolean canImport(TransferHandler.TransferSupport support) {
			for (DataFlavor flavor : support.getDataFlavors()) {
				if (flavor.isFlavorJavaFileListType()) {
					return true;
				}
			}
			return false;
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean importData(TransferHandler.TransferSupport support) {
			if (!this.canImport(support))
				return false;

			List<File> files;
			try {
				files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			} catch (UnsupportedFlavorException | IOException ex) {
				return false;
			}
			
			for (File file : files) {
				System.out.println(file.getAbsolutePath());
				list.append(file.getAbsoluteFile().getAbsolutePath()+"\n");
			}
			return true;
		}
		
	}
}
