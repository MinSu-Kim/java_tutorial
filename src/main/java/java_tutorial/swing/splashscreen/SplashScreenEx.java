package java_tutorial.swing.splashscreen;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class SplashScreenEx {
	private JFrame frame;
	private JLabel image = new JLabel(new ImageIcon("apple.jpg"));
	private JLabel text = new JLabel("TUTORIALS FIELD");
	private JProgressBar progressBar = new JProgressBar();// Creating an object of JProgressBar

	public SplashScreenEx() {
		createGUI();
		addImage();
		addText();
		addProgressBar();
		runningPBar();
	}

	private void createGUI() {
		frame = new JFrame("Splash Me2");
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.magenta);
		frame.setVisible(true);
	}

	private void addImage() {
		image.setSize(600, 200);// Setting size of the image
		frame.add(image);// Adding image to the frame
	}

	private void addText() {
		text.setFont(new Font("arial", Font.BOLD, 30));// Setting font size of text
		text.setBounds(170, 220, 600, 40);// Setting size and location
		text.setForeground(Color.BLUE);// Setting foreground color
		frame.add(text);// adding text to the frame
	}
	
	private void addProgressBar() {
		progressBar.setBounds(100, 280, 400, 30);// Setting Location and size
		progressBar.setBorderPainted(true);// Setting border painted property
		progressBar.setStringPainted(true);// Setting String painted property
		progressBar.setBackground(Color.WHITE);// setting background color
		progressBar.setForeground(Color.BLACK);// setting foreground color
		frame.add(progressBar);// adding progress bar to frame
	}
	
	private void runningPBar() {
		int i = 0;// Creating an integer variable and intializing it to 0
		do{
			try {
				Thread.sleep(50);// Pausing execution for 50 milliseconds
				i++;
				progressBar.setValue(i);// Setting value of Progress Bar
//				progressBar.setString("LOADING " + Integer.toString(i) + "%");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}while (i <= 100);
		frame.dispose();
	}
}
