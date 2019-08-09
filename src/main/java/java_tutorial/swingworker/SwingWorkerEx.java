package java_tutorial.swingworker;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class SwingWorkerEx extends JFrame implements ActionListener, PropertyChangeListener {

	private JPanel contentPane;
	private JButton btnStart;
	private JLabel lblResultLeft;
	private JPanel panel;
	private JButton btnCancel;
	private MySwingWorker workLeft;
	private JScrollPane scrollPaneLeft;
	private JTextArea taLeft;
	private JLabel lblPropertyLeft;
	private JPanel panel_1;
	private JScrollPane scrollPaneRight;
	private JTextArea taRight;
	private MySwingWorker2 workRigth;
	private JPanel panel_2;
	private JLabel lblPropertyRigth;
	private JLabel lblResultRight;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingWorkerEx frame = new SwingWorkerEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SwingWorkerEx() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		lblResultLeft = new JLabel("New label");
		panel.add(lblResultLeft);
		
		btnStart = new JButton("Start");
		panel.add(btnStart);
		
		btnCancel = new JButton("취소");
		btnCancel.addActionListener(this);
		panel.add(btnCancel);
		
		lblResultRight = new JLabel("New label");
		panel.add(lblResultRight);
		
		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(0, 2, 10, 0));
		
		scrollPaneLeft = new JScrollPane();
		panel_1.add(scrollPaneLeft);
		
		taLeft = new JTextArea();
		scrollPaneLeft.setViewportView(taLeft);
		
		scrollPaneRight = new JScrollPane();
		panel_1.add(scrollPaneRight);
		
		taRight = new JTextArea();
		scrollPaneRight.setViewportView(taRight);
		
		panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		lblPropertyLeft = new JLabel("New label");
		lblPropertyLeft.setFont(new Font("굴림", Font.BOLD, 20));
		lblPropertyLeft.setForeground(Color.RED);
		lblPropertyLeft.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblPropertyLeft);
		
		lblPropertyRigth = new JLabel("New label");
		lblPropertyRigth.setFont(new Font("굴림", Font.BOLD, 20));
		lblPropertyRigth.setForeground(Color.RED);
		lblPropertyRigth.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblPropertyRigth);
		btnStart.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) {
			actionPerformedBtnCancel(e);
		}
		if (e.getSource() == btnStart) {
			actionPerformedBtnStart(e);
		}
	}
	
	protected void actionPerformedBtnStart(ActionEvent e) {
		workLeft = new MySwingWorker();
		workLeft.addPropertyChangeListener(this);
		workLeft.execute();
		
		workRigth = new MySwingWorker2();
		workRigth.addPropertyChangeListener(this);
		workRigth.execute();
	}
	
	
	protected void actionPerformedBtnCancel(ActionEvent e) {
		workLeft.cancel(true);
		workRigth.cancel(true);
	}
	
	class MySwingWorker extends SwingWorker<String, Integer>{
		/**
		 * doInBackground()내에서 publish()호출에 의해 실행되는 함수
		 */
		@Override
		protected void process(List<Integer> chunks) {
			int val = chunks.get(chunks.size()-1);//마지막값 100->100, 100, 99->99
			lblResultLeft.setText(String.valueOf(val));
			taLeft.append(val + "\n");
		}

		/**
		 * exec()함수에 의해 호출됨
		 */
		@Override
		protected String doInBackground() throws Exception {
			int i=100;
			while (i>0) {
				Thread.sleep(100);
//				System.out.println("Value in thread : " + i);
		        publish(i);//process(호출)
		        setProgress(i); //propertyChange()호출
		        i--;
		        if (isCancelled()) {
		        	break;
		        }
		    }
			String res = "Finished Execution";
			return res;
		}
		
		/**
		 *  doInBackground()완료되면 호출됨
		 */
		protected void done() {
			try {
				String statusMsg = get();
//				System.out.println("Inside done function");
				lblResultLeft.setText(statusMsg);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (CancellationException e) {
				System.out.println("취소 되었습니다.");
			}
		};
	}

	class MySwingWorker2 extends SwingWorker<String, Integer>{
		
		@Override
		protected String doInBackground() throws Exception {
			int i=100;
			while (i>0) {
				Thread.sleep(100);
		        setProgress(i); //propertyChange()호출
		        i--;
		        if (isCancelled()) {
		        	break;
		        }
		    }
			String res = "Finished Execution";
			return res;
		}
		
		protected void done() {
			try {
				String statusMsg = get();
				lblResultRight.setText(statusMsg);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (CancellationException e) {
				System.out.println("취소 되었습니다.");
			}
		};
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt);
		if (evt.getSource() == workLeft) {
			if (evt.getPropertyName().equals("progress")) {
				int progress = (Integer) evt.getNewValue();
				lblPropertyLeft.setText(progress + "");
			}	
		}	
		if (evt.getSource() == workRigth) {
			if (evt.getPropertyName().equals("progress")) {
				int progress = (Integer) evt.getNewValue();
				lblPropertyRigth.setText(progress + "");
				taRight.append(progress + "\n");
			}
		}
	}
}
