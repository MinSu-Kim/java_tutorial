package java_tutorial.swing.component.layout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class CardLayoutEx extends JFrame implements ActionListener, ItemListener {

	private JPanel contentPane;
	private JPanel pContent;
	private JButton btn1;
	private JButton btn2;
	private JButton btn3;
	private JPanel pBtns;
	private CardLayout cardLayout;
	private JButton btnNext;
	private JButton btnPrev;
	private JButton btnFirst;
	private JButton btnLast;
	private JComboBox<String> cmbName;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CardLayoutEx frame = new CardLayoutEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CardLayoutEx() {
		initComponents();
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 557, 300);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		pContent = new JPanel();
		contentPane.add(pContent, BorderLayout.CENTER);
		cardLayout = new CardLayout(0, 0);
		pContent.setLayout(cardLayout);

		btn1 = new JButton("First");
		btn1.addActionListener(this);
		pContent.add(btn1, "name_first");

		btn2 = new JButton("Mid");
		btn2.addActionListener(this);
		pContent.add(btn2, "name_mid");

		btn3 = new JButton("Last");
		btn3.addActionListener(this);
		pContent.add(btn3, "name_last");

		String[] name = { "name_first", "name_mid", "name_last" };
		ComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(name);

		pBtns = new JPanel();
		contentPane.add(pBtns, BorderLayout.NORTH);

		btnNext = new JButton("Next");
		btnNext.addActionListener(this);
		pBtns.add(btnNext);

		btnPrev = new JButton("Prev");
		btnPrev.addActionListener(this);
		pBtns.add(btnPrev);

		btnFirst = new JButton("First");
		btnFirst.addActionListener(this);
		pBtns.add(btnFirst);

		btnLast = new JButton("Last");
		btnLast.addActionListener(this);
		pBtns.add(btnLast);

		cmbName = new JComboBox<>(cbm);
		cmbName.addItemListener(this);

		pBtns.add(cmbName);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnFirst) {
			actionPerformedBtnFirst(e);
		}
		if (e.getSource() == btnPrev) {
			actionPerformedBtnPrev(e);
		}
		if (e.getSource() == btnLast) {
			actionPerformedBtnLast(e);
		}
		if (e.getSource() == btnNext) {
			actionPerformedBtnNext(e);
		}
		if (e.getSource() == btn1 || e.getSource() == btn2 || e.getSource() == btn3) {
			cardLayout.next(pContent);
		}
	}

	protected void actionPerformedBtnNext(ActionEvent e) {
		cardLayout.next(pContent);
	}

	protected void actionPerformedBtnLast(ActionEvent e) {
		cardLayout.last(pContent);
	}

	protected void actionPerformedBtnPrev(ActionEvent e) {
		cardLayout.previous(pContent);
	}

	protected void actionPerformedBtnFirst(ActionEvent e) {
		cardLayout.first(pContent);
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cmbName) {
			itemStateChangedCmbName(e);
		}
	}

	protected void itemStateChangedCmbName(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			cardLayout.show(pContent, e.getItem().toString());
		}
	}
}
