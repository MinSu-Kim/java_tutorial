package java_tutorial.swing.component.layout;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class GroupLayoutEx extends JFrame {

	private JPanel contentPane;
	private JTextField tfName;
	private JTextField tfAddr;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GroupLayoutEx frame = new GroupLayoutEx();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GroupLayoutEx() {
		initComponents();
	}

	private void initComponents() {
		setTitle("GroupLayout 예제");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 333, 176);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel pCenter = new JPanel();
		contentPane.add(pCenter, BorderLayout.CENTER);
		pCenter.setBorder(new TitledBorder(null, "윈도우 빌더를 이용", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblName = new JLabel("이름");

		tfName = new JTextField();
		tfName.setColumns(10);

		JLabel lblAddr = new JLabel("주소");

		tfAddr = new JTextField();
		tfAddr.setText("h-resizable");
		tfAddr.setColumns(10);

		JButton btnOk = new JButton("Ok");

		JButton btnNewButton = new JButton("h-resizable");

		GroupLayout gl_pCenter = new GroupLayout(pCenter);
		gl_pCenter.setHorizontalGroup(gl_pCenter.createParallelGroup(Alignment.LEADING).addGroup(gl_pCenter
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_pCenter.createParallelGroup(Alignment.LEADING).addComponent(lblName).addComponent(lblAddr))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_pCenter.createParallelGroup(Alignment.LEADING)
						.addComponent(tfName, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfAddr, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
						.addGroup(gl_pCenter.createSequentialGroup().addComponent(btnOk)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNewButton,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				.addGap(78)));
		gl_pCenter.setVerticalGroup(gl_pCenter.createParallelGroup(Alignment.LEADING).addGroup(gl_pCenter
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_pCenter.createParallelGroup(Alignment.BASELINE, false).addComponent(lblName).addComponent(
						tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_pCenter.createParallelGroup(Alignment.BASELINE).addComponent(lblAddr).addComponent(tfAddr,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18).addGroup(gl_pCenter.createParallelGroup(Alignment.BASELINE).addComponent(btnOk)
						.addComponent(btnNewButton))
				.addGap(20)));
		pCenter.setLayout(gl_pCenter);
	}
}
