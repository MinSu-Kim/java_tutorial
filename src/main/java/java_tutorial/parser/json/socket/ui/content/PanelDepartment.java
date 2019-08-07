package java_tutorial.parser.json.socket.ui.content;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import java_tutorial.parser.json.socket.dto.Department;

@SuppressWarnings("serial")
public class PanelDepartment extends JPanel {
	private JTextField tfDeptNo;
	private JTextField tfDeptName;
	private JTextField tfFloor;

	public PanelDepartment(String title) {
		initComponents(title);
	}

	protected void initComponents(String title) {
		setBorder(new TitledBorder(null, title + " ? •ë³?", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new GridLayout(0, 2, 10, 10));

		JLabel lblDeptNo = new JLabel("ë¶??„œ ë²ˆí˜¸");
		lblDeptNo.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblDeptNo);

		tfDeptNo = new JTextField();
		add(tfDeptNo);
		tfDeptNo.setColumns(10);

		JLabel lblDeptName = new JLabel("ë¶??„œëª?");
		lblDeptName.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblDeptName);

		tfDeptName = new JTextField();
		tfDeptName.setColumns(10);
		add(tfDeptName);
		
		JLabel lblFloor = new JLabel("?œ„ì¹?(ì¸?)");
		lblFloor.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblFloor);
		
		tfFloor = new JTextField();
		tfFloor.setColumns(10);
		add(tfFloor);
	}

	public void setItem(Department dept) {
		tfDeptNo.setText(String.format("D%03d", dept.getDeptCode()));
		tfDeptName.setText(dept.getDeptName());
		tfFloor.setText(dept.getFloor()+"");
		tfDeptNo.setEditable(false);
	}

	public Department getItem() {
		int deptNo = Integer.parseInt(tfDeptNo.getText().trim().substring(1));
		String deptName = tfDeptName.getText().trim();
		int floor = Integer.parseInt(tfFloor.getText().trim());
		return new Department(deptNo, deptName, floor);
	}

	public void clearComponent(int nextNo) {
		tfDeptNo.setText(String.format("D%03d", nextNo));
		tfDeptName.setText("");
		tfFloor.setText("");
		tfDeptNo.setEditable(false);
	}

}
