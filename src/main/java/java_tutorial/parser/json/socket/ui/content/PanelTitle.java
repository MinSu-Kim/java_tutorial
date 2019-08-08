package java_tutorial.parser.json.socket.ui.content;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import java_tutorial.parser.json.socket.dto.Title;

@SuppressWarnings("serial")
public class PanelTitle extends JPanel {
	private JTextField tfTitleNo;
	private JTextField tfTitleName;

	public PanelTitle(String title) {
		initComponents(title);
	}

	protected void initComponents(String title) {
		setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new GridLayout(0, 2, 10, 10));

		JLabel lblTitleNo = new JLabel("직책번호");
		lblTitleNo.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblTitleNo);

		tfTitleNo = new JTextField();
		add(tfTitleNo);
		tfTitleNo.setColumns(10);

		JLabel lblTitleName = new JLabel("직책명");
		lblTitleName.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblTitleName);

		tfTitleName = new JTextField();
		tfTitleName.setColumns(10);
		add(tfTitleName);
	}

	public void setItem(Title Title) {
		tfTitleNo.setText(String.format("T%03d", Title.getTitleCode()));
		tfTitleName.setText(Title.getTitleName());
		tfTitleNo.setEditable(false);
	}

	public Title getItem() {
		int titleNo = Integer.parseInt(tfTitleNo.getText().trim().substring(1));
		String titleName = tfTitleName.getText().trim();
		return new Title(titleNo, titleName);
	}

	public void clearComponent(int nextNo) {
		tfTitleNo.setText(String.format("T%03d", nextNo));
		tfTitleName.setText("");
		tfTitleNo.setEditable(false);
	}

}
