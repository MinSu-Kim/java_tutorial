package java_tutorial.swing.component.icon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

public class FileSystemIconDisplayEx {
	public static void main(String[] args) throws Exception {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				JPanel gui = new JPanel(new BorderLayout(2, 2));

				File userHome = new File(System.getProperty("user.dir"));
				File[] files = userHome.listFiles();
				JList list = new JList(files);
				list.setCellRenderer(new FileListCellRenderer());
				gui.add(new JScrollPane(list));

				JOptionPane.showMessageDialog(null, gui);
			}
		};
		SwingUtilities.invokeLater(r);
	}
}

/** A FileListCellRenderer for a File. */
class FileListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -7799441088157759804L;
	private FileSystemView fileSystemView;
	private JLabel label;
	private Color textSelectionColor = Color.BLACK;
	private Color backgroundSelectionColor = Color.CYAN;
	private Color textNonSelectionColor = Color.BLACK;
	private Color backgroundNonSelectionColor = Color.WHITE;

	FileListCellRenderer() {
		label = new JLabel();
		label.setOpaque(true);
		fileSystemView = FileSystemView.getFileSystemView();
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected,
			boolean expanded) {

		File file = (File) value;
		label.setIcon(fileSystemView.getSystemIcon(file));
		label.setText(fileSystemView.getSystemDisplayName(file));
		label.setToolTipText(file.getPath());

		if (selected) {
			label.setBackground(backgroundSelectionColor);
			label.setForeground(textSelectionColor);
		} else {
			label.setBackground(backgroundNonSelectionColor);
			label.setForeground(textNonSelectionColor);
		}

		return label;
	}
}
