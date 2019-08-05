package java_tutorial.swing.component;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class JDesktopPaneEx extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JDesktopPaneEx frame = new JDesktopPaneEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JDesktopPaneEx() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		CustomDesktopPane desktopPane = new CustomDesktopPane(); 
		desktopPane.display(desktopPane);  
		contentPane.add(desktopPane, BorderLayout.CENTER);
		
	}

	private class CustomDesktopPane extends JDesktopPane {
		int numFrames = 3, x = 30, y = 30;

		public void display(CustomDesktopPane dp) {
			for (int i = 0; i < numFrames; ++i) {
				JInternalFrame jframe = new JInternalFrame("Internal Frame " + i, true, true, true, true);

				jframe.setBounds(x, y, 250, 85);
				Container c1 = jframe.getContentPane();
				c1.add(new JLabel("I love my country"));
				dp.add(jframe);
				jframe.setVisible(true);
				y += 85;
			}
		}
	}
}
