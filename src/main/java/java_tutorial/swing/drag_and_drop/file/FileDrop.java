package java_tutorial.swing.drag_and_drop.file;

import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

public class FileDrop {
	private transient Border normalBorder;
	private transient DropTargetListener dropListener;

	private static Boolean supportsDnD;

	private static java.awt.Color defaultBorderColor = new java.awt.Color(0f, 0f, 1f, 0.25f);

	public FileDrop(Component c, final Listener listener) {
		this(null, // Logging stream
				c, // Drop target
				BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), // Drag border
				true, // Recursive
				listener);
	} // end constructor

	public FileDrop(final Component c, final boolean recursive, final Listener listener) {
		this(null, // Logging stream
				c, // Drop target
				BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), // Drag border
				recursive, // Recursive
				listener);
	} // end constructor

	public FileDrop(final PrintStream out, final Component c, final Listener listener) {
		this(out, c, BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), false, listener);
	} // end constructor

	public FileDrop(final PrintStream out, final Component c, final boolean recursive, final Listener listener) {
		this(out, // Logging stream
				c, // Drop target
				BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), // Drag border
				recursive, // Recursive
				listener);
	} // end constructor

	public FileDrop(final Component c, final Border dragBorder, final Listener listener) {
		this(null, // Logging stream
				c, // Drop target
				dragBorder, // Drag border
				false, // Recursive
				listener);
	} // end constructor

	public FileDrop(final Component c, final Border dragBorder, final boolean recursive, final Listener listener) {
		this(null, c, dragBorder, recursive, listener);
	} // end constructor

	public FileDrop(final PrintStream out, final Component c, final Border dragBorder, final Listener listener) {
		this(out, // Logging stream
				c, // Drop target
				dragBorder, // Drag border
				false, // Recursive
				listener);
	} // end constructor

	public FileDrop(final PrintStream out, final Component c, final Border dragBorder, final boolean recursive,
			final Listener listener) {

		if (supportsDnD()) { // Make a drop listener
			dropListener = new DropTargetListener() {
				public void dragEnter(DropTargetDragEvent evt) {
					log(out, "FileDrop: dragEnter event.");

					// Is this an acceptable drag event?
					if (isDragOk(out, evt)) {
						// If it's a Swing component, set its border
						if (c instanceof JComponent) {
							JComponent jc = (JComponent) c;
							normalBorder = jc.getBorder();
							log(out, "FileDrop: normal border saved.");
							jc.setBorder(dragBorder);
							log(out, "FileDrop: drag border set.");
						} // end if: JComponent

						evt.acceptDrag(DnDConstants.ACTION_COPY);
						log(out, "FileDrop: event accepted.");
					} // end if: drag ok
					else { // Reject the drag event
						evt.rejectDrag();
						log(out, "FileDrop: event rejected.");
					} // end else: drag not ok
				} // end dragEnter

				public void dragOver(DropTargetDragEvent evt) { // This is called continually as long as
																// the mouse is
																// over the drag target.
				} // end dragOver

				public void drop(DropTargetDropEvent evt) {
					log(out, "FileDrop: drop event.");
					try { // Get whatever was dropped
						Transferable tr = evt.getTransferable();

						if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
							evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
							log(out, "FileDrop: file list accepted.");

							List fileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
							Iterator iterator = fileList.iterator();

							// Convert list to array
							File[] filesTemp = new File[fileList.size()];
							fileList.toArray(filesTemp);
							final File[] files = filesTemp;

							// Alert listener to drop.
							if (listener != null)
								listener.filesDropped(files);

							// Mark that drop is completed.
							evt.getDropTargetContext().dropComplete(true);
							log(out, "FileDrop: drop complete.");
						} // end if: file list
						else // this section will check for a reader flavor.
						{
							DataFlavor[] flavors = tr.getTransferDataFlavors();
							boolean handled = false;
							for (int zz = 0; zz < flavors.length; zz++) {
								if (flavors[zz].isRepresentationClassReader()) {
									// Say we'll take it.
									// evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
									evt.acceptDrop(DnDConstants.ACTION_COPY);
									log(out, "FileDrop: reader accepted.");

									Reader reader = flavors[zz].getReaderForText(tr);

									BufferedReader br = new BufferedReader(reader);

									if (listener != null)
										listener.filesDropped(createFileArray(br, out));

									// Mark that drop is completed.
									evt.getDropTargetContext().dropComplete(true);
									log(out, "FileDrop: drop complete.");
									handled = true;
									break;
								}
							}
							if (!handled) {
								log(out, "FileDrop: not a file list or reader - abort.");
								evt.rejectDrop();
							}
						} // end else: not a file list
					} // end try
					catch (IOException io) {
						log(out, "FileDrop: IOException - abort:");
						io.printStackTrace(out);
						evt.rejectDrop();
					} // end catch IOException
					catch (UnsupportedFlavorException ufe) {
						log(out, "FileDrop: UnsupportedFlavorException - abort:");
						ufe.printStackTrace(out);
						evt.rejectDrop();
					} // end catch: UnsupportedFlavorException
					finally {
						// If it's a Swing component, reset its border
						if (c instanceof JComponent) {
							JComponent jc = (JComponent) c;
							jc.setBorder(normalBorder);
							log(out, "FileDrop: normal border restored.");
						} // end if: JComponent
					} // end finally
				} // end drop

				public void dragExit(DropTargetEvent evt) {
					log(out, "FileDrop: dragExit event.");
					// If it's a Swing component, reset its border
					if (c instanceof JComponent) {
						JComponent jc = (JComponent) c;
						jc.setBorder(normalBorder);
						log(out, "FileDrop: normal border restored.");
					} // end if: JComponent
				} // end dragExit

				public void dropActionChanged(DropTargetDragEvent evt) {
					log(out, "FileDrop: dropActionChanged event.");
					if (isDragOk(out, evt)) { // evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
						evt.acceptDrag(DnDConstants.ACTION_COPY);
						log(out, "FileDrop: event accepted.");
					} // end if: drag ok
					else {
						evt.rejectDrag();
						log(out, "FileDrop: event rejected.");
					} // end else: drag not ok
				} // end dropActionChanged

			}; // end DropTargetListener

			makeDropTarget(out, c, recursive);
		} // end if: supports dnd
		else {
			log(out, "FileDrop: Drag and drop is not supported with this JVM");
		} // end else: does not support DnD
	} // end constructor

	private static boolean supportsDnD() { // Static Boolean
		if (supportsDnD == null) {
			boolean support = false;
			try {
				Class<?> arbitraryDndClass = Class.forName("java.awt.dnd.DnDConstants");
				support = true;
			} // end try
			catch (Exception e) {
				support = false;
			} // end catch
			supportsDnD = new Boolean(support);
		} // end if: first time through
		return supportsDnD.booleanValue();
	} // end supportsDnD

	private static String ZERO_CHAR_STRING = "" + (char) 0;

	private static File[] createFileArray(BufferedReader bReader, PrintStream out) {
		try {
			List list = new ArrayList();
			String line = null;
			while ((line = bReader.readLine()) != null) {
				try {
					if (ZERO_CHAR_STRING.equals(line))
						continue;

					File file = new File(new URI(line));
					list.add(file);
				} catch (Exception ex) {
					log(out, "Error with " + line + ": " + ex.getMessage());
				}
			}

			return (java.io.File[]) list.toArray(new File[list.size()]);
		} catch (IOException ex) {
			log(out, "FileDrop: IOException");
		}
		return new File[0];
	}

	private void makeDropTarget(final PrintStream out, final Component c, boolean recursive) {
		final DropTarget dt = new DropTarget();
		try {
			dt.addDropTargetListener(dropListener);
		} // end try
		catch (TooManyListenersException e) {
			e.printStackTrace();
			log(out, "FileDrop: Drop will not work due to previous error. Do you have another listener attached?");
		} // end catch

		c.addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent evt) {
				log(out, "FileDrop: Hierarchy changed.");
				Component parent = c.getParent();
				if (parent == null) {
					c.setDropTarget(null);
					log(out, "FileDrop: Drop target cleared from component.");
				} // end if: null parent
				else {
					new DropTarget(c, dropListener);
					log(out, "FileDrop: Drop target added to component.");
				} // end else: parent not null
			} // end hierarchyChanged
		}); // end hierarchy listener
		if (c.getParent() != null)
			new DropTarget(c, dropListener);

		if (recursive && (c instanceof Container)) {
			Container cont = (Container) c;
			Component[] comps = cont.getComponents();
			for (int i = 0; i < comps.length; i++)
				makeDropTarget(out, comps[i], recursive);
		} // end if: recursively set components as listener
	} // end dropListener

	private boolean isDragOk(final PrintStream out, final DropTargetDragEvent evt) {
		boolean ok = false;

		DataFlavor[] flavors = evt.getCurrentDataFlavors();

		int i = 0;
		while (!ok && i < flavors.length) {
			final DataFlavor curFlavor = flavors[i];
			if (curFlavor.equals(DataFlavor.javaFileListFlavor) || curFlavor.isRepresentationClassReader()) {
				ok = true;
			}
			i++;
		} // end while: through flavors

		// If logging is enabled, show data flavors
		if (out != null) {
			if (flavors.length == 0)
				log(out, "FileDrop: no data flavors.");
			for (i = 0; i < flavors.length; i++)
				log(out, flavors[i].toString());
		} // end if: logging enabled

		return ok;
	} // end isDragOk

	private static void log(PrintStream out, String message) { // Log message if requested
		if (out != null)
			out.println(message);
	} // end log

	public static boolean remove(Component c) {
		return remove(null, c, true);
	} // end remove

	public static boolean remove(PrintStream out, Component c, boolean recursive) { // Make sure we
																					// support dnd.
		if (supportsDnD()) {
			log(out, "FileDrop: Removing drag-and-drop hooks.");
			c.setDropTarget(null);
			if (recursive && (c instanceof Container)) {
				Component[] comps = ((Container) c).getComponents();
				for (int i = 0; i < comps.length; i++)
					remove(out, comps[i], recursive);
				return true;
			} // end if: recursive
			else
				return false;
		} // end if: supports DnD
		else
			return false;
	} // end remove

	public static interface Listener {

		public abstract void filesDropped(java.io.File[] files);

	} // end inner-interface Listener

	@SuppressWarnings("serial")
	public static class Event extends EventObject {

		private java.io.File[] files;

		public Event(File[] files, Object source) {
			super(source);
			this.files = files;
		} // end constructor

		public File[] getFiles() {
			return files;
		} // end getFiles

	} // end inner class Event

	public static class TransferableObject implements Transferable {

		public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";

		public final static DataFlavor DATA_FLAVOR = new DataFlavor(FileDrop.TransferableObject.class, MIME_TYPE);

		private Fetcher fetcher;
		private Object data;

		private DataFlavor customFlavor;

		public TransferableObject(Object data) {
			this.data = data;
			this.customFlavor = new DataFlavor(data.getClass(), MIME_TYPE);
		} // end constructor

		public TransferableObject(Fetcher fetcher) {
			this.fetcher = fetcher;
		} // end constructor

		public TransferableObject(Class dataClass, Fetcher fetcher) {
			this.fetcher = fetcher;
			this.customFlavor = new DataFlavor(dataClass, MIME_TYPE);
		} // end constructor

		public DataFlavor getCustomDataFlavor() {
			return customFlavor;
		} // end getCustomDataFlavor

		public DataFlavor[] getTransferDataFlavors() {
			if (customFlavor != null)
				return new DataFlavor[] { customFlavor, DATA_FLAVOR, DataFlavor.stringFlavor }; // end flavors array
			else
				return new DataFlavor[] { DATA_FLAVOR, DataFlavor.stringFlavor }; // end flavors array
		} // end getTransferDataFlavors

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			// Native object
			if (flavor.equals(DATA_FLAVOR))
				return fetcher == null ? data : fetcher.getObject();

			// String
			if (flavor.equals(DataFlavor.stringFlavor))
				return fetcher == null ? data.toString() : fetcher.getObject().toString();

			// We can't do anything else
			throw new UnsupportedFlavorException(flavor);
		} // end getTransferData

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			// Native object
			if (flavor.equals(DATA_FLAVOR))
				return true;

			// String
			if (flavor.equals(DataFlavor.stringFlavor))
				return true;

			// We can't do anything else
			return false;
		} // end isDataFlavorSupported

		public static interface Fetcher {
			public abstract Object getObject();
		} // end inner interface Fetcher

	} // end class TransferableObject

} // end class FileDrop