package java_tutorial.swing.component;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
public class LayeredPaneEx extends JFrame {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LayeredPaneEx frame = new LayeredPaneEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LayeredPaneEx() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JLayeredPane pane = getLayeredPane();
		// creating buttons
		JButton top = new JButton();
		top.setBackground(Color.white);
		top.setBounds(20, 20, 50, 50);
		
		JButton middle = new JButton();
		middle.setBackground(Color.red);
		middle.setBounds(40, 40, 50, 50);
		
		JButton bottom = new JButton();
		bottom.setBackground(Color.cyan);
		bottom.setBounds(60, 60, 50, 50);
		
		// adding buttons on pane
		pane.add(bottom, new Integer(1));
		pane.add(middle, new Integer(2));
		pane.add(top, new Integer(3));
	}

}
