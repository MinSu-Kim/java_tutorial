package java_tutorial.swing.component.icon;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class IconDemoApp extends JFrame {

	private JLabel photographLabel = new JLabel();
	private JToolBar buttonBar = new JToolBar();
	private String imagedir = "images/";
	private MissingIcon placeholderIcon = new MissingIcon();
	private String[] imageCaptions = { "Original SUNW Logo", "The Clocktower", "Clocktower from the West", "The Mansion", "Sun Auditorium" };
	private String[] imageFileNames = { "sunw01.jpg", "sunw02.jpg", "sunw03.jpg", "sunw04.jpg", "sunw05.jpg" };

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				IconDemoApp app = new IconDemoApp();
				app.setVisible(true);
			}
		});
	}

	public IconDemoApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Icon Demo: Please Select an Image");

		photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
		photographLabel.setHorizontalTextPosition(JLabel.CENTER);
		photographLabel.setHorizontalAlignment(JLabel.CENTER);
		photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		buttonBar.add(Box.createGlue());
		buttonBar.add(Box.createGlue());

		add(buttonBar, BorderLayout.SOUTH);
		add(photographLabel, BorderLayout.CENTER);

		setSize(400, 300);

		setLocationRelativeTo(null);

		loadimages.execute();
	}

	private SwingWorker<Void, ThumbnailAction> loadimages = new SwingWorker<Void, ThumbnailAction>() {

		@Override
		protected Void doInBackground() throws Exception {
			for (int i = 0; i < imageCaptions.length; i++) {
				ImageIcon icon;
				icon = createImageIcon(imagedir + imageFileNames[i], imageCaptions[i]);

				ThumbnailAction thumbAction;
				if (icon != null) {
					ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 32, 32));
					thumbAction = new ThumbnailAction(icon, thumbnailIcon, imageCaptions[i]);

				} else {
					thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, imageCaptions[i]);
				}
				publish(thumbAction);
			}
			return null;
		}

		@Override
		protected void process(List<ThumbnailAction> chunks) {
			for (ThumbnailAction thumbAction : chunks) {
				JButton thumbButton = new JButton(thumbAction);
				buttonBar.add(thumbButton, buttonBar.getComponentCount() - 1);
			}
		}
	};

	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 * 
	 * @param srcImg - source image to scale
	 * @param w      - desired width
	 * @param h      - desired height
	 * @return - the new resized image
	 */
	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	/**
	 * Action class that shows the image specified in it's constructor.
	 */
	private class ThumbnailAction extends AbstractAction {

		/**
		 * The icon if the full image we want to display.
		 */
		private Icon displayPhoto;

		/**
		 * @param Icon   - The full size photo to show in the button.
		 * @param Icon   - The thumbnail to show in the button.
		 * @param String - The descriptioon of the icon.
		 */
		public ThumbnailAction(Icon photo, Icon thumb, String desc) {
			displayPhoto = photo;

			// The short description becomes the tooltip of a button.
			putValue(SHORT_DESCRIPTION, desc);

			// The LARGE_ICON_KEY is the key for setting the
			// icon when an Action is applied to a button.
			putValue(LARGE_ICON_KEY, thumb);
		}

		/**
		 * Shows the full image in the main area and sets the application title.
		 */
		public void actionPerformed(ActionEvent e) {
			photographLabel.setIcon(displayPhoto);
			setTitle("Icon Demo: " + getValue(SHORT_DESCRIPTION).toString());
		}
	}
}
