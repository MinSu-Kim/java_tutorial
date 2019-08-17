package java_tutorial.file_upload;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class FileUploadBasicEx extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JButton btnUpload;
	private JButton btnFileChooser;
	private JTextArea taFileName;
	private JPanel pNorth;
	
//	private static final String UPLOAD_DIR_NAME =  ;
	private static final File UPLOAD_DIR =  new File(System.getProperty("user.dir") + "\\upload\\");
	private File[] selFiles;
	
	static {
		if (!UPLOAD_DIR.exists()) UPLOAD_DIR.mkdir();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileUploadBasicEx frame = new FileUploadBasicEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FileUploadBasicEx() {
		initComponents();
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 149);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel pCenter = new JPanel();
		contentPane.add(pCenter, BorderLayout.CENTER);
		pCenter.setLayout(new BorderLayout(0, 0));

		taFileName = new JTextArea();
		pCenter.add(taFileName);

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
	}

	protected void actionPerformedBtnUpload(ActionEvent e) {
		new UploadWorker(selFiles).execute();
	}

	protected void actionPerformedBtnFileChooser(ActionEvent e) throws IOException {
		
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int returnValue = fileChooser.showOpenDialog(null);
			
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selFiles = fileChooser.getSelectedFiles();
			
			for(File selectedFile : selFiles){
				taFileName.append("URI : " + selectedFile.toURI() + "\n");
				String mimeType = Files.probeContentType(selectedFile.toPath());
				String text = String.format("%s - size(%d) %s\n", selectedFile.getName(), selectedFile.getUsableSpace(),
						mimeType);
				taFileName.append(text);
			}

		}

		fileChooser.setDialogTitle("Open a  picture file");
		fileChooser.setApproveButtonText("Open File");
	}
	
	private class UploadWorker extends SwingWorker<Void, String>{
		File[] selectedFiles;
		
		public UploadWorker(File[] selectedFiles) {
			this.selectedFiles = selectedFiles;
		}

		@Override
		protected Void doInBackground() throws Exception {
			
			for(File file : selectedFiles) {
				UUID uid = UUID.randomUUID();
				String savedName = uid.toString() + "_" + file.getName();
				File target = new File(UPLOAD_DIR, savedName);
				
				try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));){
					byte[] readBuffer = new byte[1024];
					while (bis.read(readBuffer, 0, readBuffer.length) != -1) {
						bos.write(readBuffer);
					}
				}
				publish(savedName);
			}
			return null;
		}

		@Override
		protected void done() {
			JOptionPane.showMessageDialog(null, "Upload 완료");
		}
		
		
	}
}
