package java_tutorial.socket2;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ServerManagement extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JButton btnStop;
	private JButton btnStart;
	private Server server;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerManagement frame = new ServerManagement();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerManagement() {
		setTitle("\uCC44\uD305 \uC11C\uBC84 \uAD00\uB9AC");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 115);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		contentPane.add(btnStart);

		btnStop = new JButton("Stop");
		btnStop.addActionListener(this);
		contentPane.add(btnStop);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnStart) {
			actionPerformedBtnStart(e);
		}
		if (e.getSource() == btnStop) {
			actionPerformedBtnStop(e);
		}
	}

	protected void actionPerformedBtnStop(ActionEvent e) {
		server.interrupt();
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
	}

	protected void actionPerformedBtnStart(ActionEvent e) {
		try {
			
			if (server == null) {
				server = new Server();
				btnStart.setEnabled(false);
				server.start();
			}
		} catch (IllegalThreadStateException e1) {

		}
	}
}
