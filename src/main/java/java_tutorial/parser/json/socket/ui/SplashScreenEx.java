package java_tutorial.parser.json.socket.ui;

import java.awt.Color;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import java_tutorial.parser.json.socket.ui.thread_client.ReceiveDepartmentThread;
import java_tutorial.parser.json.socket.ui.thread_client.ReceiveEmployeeThread;
import java_tutorial.parser.json.socket.ui.thread_client.ReceiveTitleThread;

public class SplashScreenEx {
	private JFrame frame;
	private JLabel text = new JLabel("Loading....");
	private JProgressBar progressBar = new JProgressBar();// Creating an object of JProgressBar
	
	public SplashScreenEx() {
		
//		new Thread(()->runningPBar()).start();
		connectServer();
		createGUI();
		addText();
		addProgressBar();
		runningPBar();

	}

	private void connectServer() {
		try {
			connectTitle();
			connectDept();
			connectEmp();
		} catch (ConnectException e) {
			System.out.println("Server Check~~!");
			System.exit(1);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private void connectEmp() throws UnknownHostException, IOException {
		Socket socketEmp = new Socket(JsonServer.HOST, JsonServer.PORT_EMPLOYEE);
		System.out.println(socketEmp);
		
		DataOutputStream outE = new DataOutputStream(socketEmp.getOutputStream());
		DataInputStream inE = new DataInputStream(socketEmp.getInputStream());
		
		ReceiveEmployeeThread receiveEmp = new ReceiveEmployeeThread();
		receiveEmp.setInOutStream(inE, outE);
		receiveEmp.start();
		
	}

	private void connectDept() throws UnknownHostException, IOException {
		Socket socketDept = new Socket(JsonServer.HOST, JsonServer.PORT_DEPARTMENT);
		System.out.println(socketDept);
		
		DataOutputStream outD = new DataOutputStream(socketDept.getOutputStream());
		DataInputStream inD = new DataInputStream(socketDept.getInputStream());
		
		ReceiveDepartmentThread receiveDept = new ReceiveDepartmentThread();
		receiveDept.setInOutStream(inD, outD);
		receiveDept.start();
		
	}

	private void connectTitle() throws UnknownHostException, IOException {
		Socket socketTitle = new Socket(JsonServer.HOST, JsonServer.PORT_TITLE);
		System.out.println(socketTitle);
		
		DataOutputStream out = new DataOutputStream(socketTitle.getOutputStream());
		DataInputStream in = new DataInputStream(socketTitle.getInputStream());
		
		ReceiveTitleThread receiveTitle = new ReceiveTitleThread();
		receiveTitle.setInOutStream(in, out);
		receiveTitle.start();
		
	}

	private void createGUI() {
		frame = new JFrame("Splash Me2");
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		frame.setSize(600, 200);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.magenta);
		frame.setVisible(true);
	}

	private void addText() {
		text.setFont(new Font("arial", Font.BOLD, 30));// Setting font size of text
		text.setBounds(100, 70, 600, 40);// Setting size and location
		text.setForeground(Color.BLUE);// Setting foreground color
		frame.add(text);// adding text to the frame
	}

	private void addProgressBar() {
		progressBar.setBounds(100, 120, 400, 30);// Setting Location and size
		progressBar.setBorderPainted(true);// Setting border painted property
		progressBar.setStringPainted(true);// Setting String painted property
		progressBar.setBackground(Color.WHITE);// setting background color
		progressBar.setForeground(Color.BLACK);// setting foreground color
		frame.add(progressBar);// adding progress bar to frame
	}

	private void runningPBar() {
		int i = 0;
		do {
			try {
				Thread.sleep(50);// Pausing execution for 50 milliseconds
				i++;
				progressBar.setValue(i);// Setting value of Progress Bar
//				progressBar.setString("LOADING " + "cnt : " + cnt + " p : " + p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (i<100);
		frame.dispose();
	}

}
