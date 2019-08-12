package java_tutorial.swing.component.progress;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;

@SuppressWarnings("serial")
public class ProgressBarDemo extends JPanel implements ActionListener, PropertyChangeListener {

	private JProgressBar progressBar;
	private JButton startButton;
	private JTextArea taskOutput;
	private Task task;

	class Task extends SwingWorker<Void, Void> {

		@Override
		public Void doInBackground() {
			Random random = new Random();
			int progress = 0;
			// Initialize progress property.
			setProgress(0);
			while (progress < 100) {
				try {
					Thread.sleep(random.nextInt(1000));
				} catch (InterruptedException ignore) {
				}
				progress += random.nextInt(10);
				setProgress(Math.min(progress, 100));
			}
			return null;
		}

		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			startButton.setEnabled(true);
			setCursor(null); // turn off the wait cursor
			taskOutput.append("Done!\n");
		}
		
		
	}

	public ProgressBarDemo() {
		super(new BorderLayout());

		// Create the demo's UI.
		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		taskOutput = new JTextArea(5, 20);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);

		JPanel panel = new JPanel();
		panel.add(startButton);
		panel.add(progressBar);

		add(panel, BorderLayout.PAGE_START);
		add(new JScrollPane(taskOutput), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	public void actionPerformed(ActionEvent evt) {
		startButton.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// Instances of javax.swing.SwingWorker are not reusuable, so
		// we create new instances as needed.
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
		}
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("ProgressBarDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new ProgressBarDemo();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
