package java_tutorial.file_upload;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import java.awt.GridLayout;


/**
 * @author mskim
 * java_tutorial.file_upload.FileDnDListHandlerEx 참조하여 최적화변경
 * jfilechooser에서 는 jlist로 drog and drop 적용되지 않으므로 추가 버튼으로 추가
 * 탐색기에서는 DnD 됨
 */
@SuppressWarnings("serial")
public class FileUploadListEx extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JButton btnUpload;
	private JButton btnFileChooser;
	private JList<File> list;
	private JPanel pNorth;

	private static final File UPLOAD_DIR = new File(System.getProperty("user.dir") + "\\upload\\");
	private File[] selFiles;
	private DefaultListModel<File> model;
	
	private JMenuItem mntmDel;
	private List<String> uploadPathList;
	private JPanel pBottom;
	private JLabel lbResult;
	private JPanel pResult;

	static {
		if (!UPLOAD_DIR.exists())
			UPLOAD_DIR.mkdir();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileUploadListEx frame = new FileUploadListEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FileUploadListEx() {
		initComponents();
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 243);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel pCenter = new JPanel();
		contentPane.add(pCenter, BorderLayout.CENTER);
		pCenter.setLayout(new BorderLayout(0, 0));

		model = new DefaultListModel<File>();
		list = new JList<>(model);
		list.setDragEnabled(true);
		list.setTransferHandler(new ListTransferHandler(list));
		list.setCellRenderer(new FileListCell());
		list.setDropMode(DropMode.INSERT);

		pCenter.add(list);

		pNorth = new JPanel();
		FlowLayout fl_pNorth = (FlowLayout) pNorth.getLayout();
		fl_pNorth.setAlignment(FlowLayout.LEFT);
		pCenter.add(pNorth, BorderLayout.NORTH);

		btnFileChooser = new JButton("파일선택");
		pNorth.add(btnFileChooser);

		btnUpload = new JButton("업로드");
		pNorth.add(btnUpload);
		btnUpload.addActionListener(this);
		btnFileChooser.addActionListener(this);
		
		JPopupMenu popupMenu = new JPopupMenu();
		list.setComponentPopupMenu(popupMenu);

		mntmDel = new JMenuItem("삭제");
		mntmDel.addActionListener(this);
		popupMenu.add(mntmDel);
		
		pBottom = new JPanel();
		contentPane.add(pBottom, BorderLayout.SOUTH);
		pBottom.setLayout(new BorderLayout(0, 0));
		
		lbResult = new JLabel("결과");
		pBottom.add(lbResult, BorderLayout.NORTH);
		
		pResult = new JPanel();
		pBottom.add(pResult, BorderLayout.SOUTH);
		pResult.setLayout(new GridLayout(0, 1, 0, 0));
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnFileChooser) {
			try {
				actionPerformedBtnFileChooser(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == btnUpload) {
			actionPerformedBtnUpload(e);
		}
		if (e.getSource() == mntmDel) {
			actionPerformedMntmDel(e);
		}
	}

	protected void actionPerformedBtnUpload(ActionEvent e) {
		File[] selFiles = new File[model.getSize()];
		for (int i = 0; i < selFiles.length; i++) {
			selFiles[i] = model.get(i);
		}
		new UploadWorker(selFiles).execute();
	}

	protected void actionPerformedMntmDel(ActionEvent e) {
		int[] selIndexes = list.getSelectedIndices();
		for (int i = selIndexes.length - 1; i >= 0; i--) {
			model.removeElementAt(selIndexes[i]);
		}

	}
	
	protected void actionPerformedBtnFileChooser(ActionEvent e) throws IOException {

		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDragEnabled(true);
		fileChooser.setDialogTitle("Add a file");
		fileChooser.setApproveButtonText("추가");
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selFiles = fileChooser.getSelectedFiles();
			for (File f : selFiles) {
				model.addElement(f);
			}
		}

	}

	private class UploadWorker extends SwingWorker<Void, String> {
		File[] selectedFiles;
		private List<String> uploadPathList;
		
		public UploadWorker(File[] selectedFiles) {
			this.selectedFiles = selectedFiles;
			this.uploadPathList = new ArrayList<String>();
		}

		@Override
		protected Void doInBackground() throws Exception {
			for (File file : selectedFiles) {
				UUID uid = UUID.randomUUID();
				String savedName = uid.toString() + "_" + file.getName();
				
				Calendar cal = Calendar.getInstance();
				String yearPath = File.separator + cal.get(Calendar.YEAR);
				String monthPath = yearPath + File.separator + String.format("%02d", cal.get(Calendar.MONTH)+1);
				String datePath = monthPath + File.separator + String.format("%02d", cal.get(Calendar.DATE));
				
				makeDir(UPLOAD_DIR, yearPath, monthPath, datePath);
				
				File target = new File(UPLOAD_DIR + datePath, File.separator + savedName);
				
				makeFile(file, target);
				
				String fileType = new Tika().detect(file);
				String type = fileType.substring(fileType.lastIndexOf("/")+1);
				
				System.out.println("MediaUtils.getMediaType(type) " + MediaUtils.getMediaType(type));
				
				String uploadPath = null;
				if (MediaUtils.getMediaType(type) != null) {
					uploadPath = makeThumbnale(UPLOAD_DIR, datePath, savedName, file);
				}else {
					uploadPath = (datePath + File.separator + savedName).replace(File.separatorChar, '/');
				}
				
				publish(uploadPath);
				
			}
			return null;
		}

		private void makeFile(File srcFile, File destFile) throws IOException, FileNotFoundException {
			
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));) {
				
				byte[] readBuffer = new byte[1024];
				while (bis.read(readBuffer, 0, readBuffer.length) != -1) {
					bos.write(readBuffer);
				}
			}
		}

		
		@Override
		protected void process(List<String> chunks) {
			uploadPathList.add(chunks.get(0));
		}

		@Override
		protected void done() {
			JOptionPane.showMessageDialog(null, "Upload 완료");
			setUploadPaths(uploadPathList);
			model.clear();
		}
		
		private String makeThumbnale(File uploadPath, String path, String fileName, File file) throws IOException /* throws IOException */ {
						
			BufferedImage sourceImg = ImageIO.read(file);
			
			BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC,  Scalr.Mode.FIT_TO_HEIGHT, 100);
			
			String thumbnailName = uploadPath + path + File.separator + "s_" + fileName;
			
			System.out.println("thumbnailName " + thumbnailName);
			
			File newFile = new File(thumbnailName);
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
			
			ImageIO.write(destImg, formatName.toUpperCase(), newFile);
			return thumbnailName.substring(uploadPath.toPath().toString().length()).replace(File.separatorChar, '/');
		}
		
		private void makeDir(File uploadPath, String...paths) {
			if (new File(paths[paths.length-1]).exists())
				return;
			for(String path:paths) {
				File dirPath = new File(uploadPath, path);
				if (! dirPath.exists()){
					dirPath.mkdir();
				}
			}
		}
	}

	private class ListTransferHandler extends TransferHandler {
		private JList<File> list;

		ListTransferHandler(JList<File> list) {
			this.list = list;
		}

		@Override
		public boolean canImport(TransferHandler.TransferSupport info) {
			if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				return false;
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean importData(TransferHandler.TransferSupport info) {
			if (!info.isDrop()) {
				return false;
			}

			if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				displayDropLocation("List doesn't accept a drop of this type.");
				return false;
			}

			Transferable t = info.getTransferable();
			List<File> data;
			try {
				data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
			} catch (Exception e) {
				return false;
			}
			DefaultListModel<File> model = (DefaultListModel<File>) list.getModel();
			JList.DropLocation dropLocation = (JList.DropLocation) info.getDropLocation();
			int dropIndex = dropLocation.getIndex();
			
			for (Object file : data) {
				model.add(dropIndex++, (File) file); 
			}
			return true;
		}

		private void displayDropLocation(String string) {
			System.out.println(string);
		}
	}


	private class FileListCell extends DefaultListCellRenderer {

		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (c instanceof JLabel && value instanceof File) {
				JLabel l = (JLabel) c;
				File f = (File) value;
				l.setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
				l.setText(f.getName());
				try {
					Path source = Paths.get(f.getPath());
					l.setToolTipText(f.getAbsolutePath() + " type(" +Files.probeContentType(source)+")");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return c;
		}

	}

	public void setUploadPaths(List<String> uploadPaths) {
		this.uploadPathList	 = 	uploadPaths;
		System.out.println(uploadPathList);
		
		for(String subPath : uploadPathList) {
			File f = new File(UPLOAD_DIR, subPath);
			System.out.println(f.exists());
			String fname = f.getName();
			
			JLabel lbl = new JLabel();
			Path source = Paths.get(f.getAbsolutePath());
			if (checkImageType(fname)) {
				lbl.setIcon( new ImageIcon(source.toAbsolutePath().toString()));
			}else {
				lbl.setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
			}
			lbl.setText(fname.substring(fname.indexOf("_")+1));
			lbl.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					JFileChooser f = new JFileChooser(System.getProperty("user.dir"));
					int res = f.showSaveDialog(null);
					File file = f.getSelectedFile();
					if (res==JFileChooser.APPROVE_OPTION) {
						System.out.println(file.getPath());
					}
				}
				
			});
			pResult.add(lbl);
			repaint();
			revalidate();
		}
		
	}
	
	private boolean checkImageType(String fileName) {
		return fileName.contains("jpg") || fileName.contains("gif") || fileName.contains("png") || fileName.contains("jpeg") ;
	}
}
