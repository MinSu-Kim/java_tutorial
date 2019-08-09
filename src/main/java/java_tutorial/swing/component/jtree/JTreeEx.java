package java_tutorial.swing.component.jtree;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

@SuppressWarnings("serial")
public class JTreeEx extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JMenuItem mntmDel;
	private JTree tree;
	private JMenuItem mntmAdd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
////					System.out.println(UIManager.getCrossPlatformLookAndFeelClassName());
//					//시스템에서 사용하는 룩앤필 적용
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//					System.out.println(UIManager.getSystemLookAndFeelClassName());
					JTreeEx frame = new JTreeEx();
//					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JTreeEx() {
		initComponents();
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 527, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPopupMenu popupMenu = new JPopupMenu();
		mntmDel = new JMenuItem("삭제");
		mntmDel.addActionListener(this);
		popupMenu.add(mntmDel);

		mntmAdd = new JMenuItem("추가");
		mntmAdd.addActionListener(this);
		popupMenu.add(mntmAdd);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		tree = new JTree();
		tree.setComponentPopupMenu(popupMenu);
		tree.setModel(getTreeModel());
		panel.add(tree);

		tree.setRootVisible(true);
		
		// icon변경
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
		Icon closedIcon = new ImageIcon("closed.png");
		Icon openIcon = new ImageIcon("open.png");
		Icon leafIcon = new ImageIcon("leaf.png");
		renderer.setClosedIcon(closedIcon);
		renderer.setOpenIcon(openIcon);
		renderer.setLeafIcon(leafIcon);

//	    tree.setCellRenderer(renderer);
	}

	private DefaultTreeModel getTreeModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

		DefaultMutableTreeNode mercury = new DefaultMutableTreeNode("Mercury");
		root.add(mercury);
		DefaultMutableTreeNode venus = new DefaultMutableTreeNode("Venus");
		mercury.add(venus);

		DefaultMutableTreeNode mars = new DefaultMutableTreeNode("Mars");
		root.insert(mars, 1);

		DefaultTreeModel dtm = new DefaultTreeModel(root);

		return dtm;
	}

	private void deleteSelectedItems() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
		model.removeNodeFromParent(node);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmDel) {
			deleteSelectedItems();
		}
		if (e.getSource() == mntmAdd) {
			addTreeNode();
		}
	}

	private void addTreeNode() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
		DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode("new");
		node.add(newTreeNode);
		
		tree.repaint();
	}
}
