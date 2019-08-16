package java_tutorial.swing.drag_and_drop.file;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Example {
    public static void main(String[] args) {
        JFrame frame = new JFrame("FileDrop");
        final JTextArea text = new JTextArea();
        frame.getContentPane().add(new JScrollPane(text), BorderLayout.CENTER);
 
        new FileDrop(System.out, text, new FileDrop.Listener() {
            public void filesDropped(File[] files) {
                for (int i = 0; i < files.length; i++) {
                    try {
                        text.append(files[i].getCanonicalPath() + "\n");
                    } // end try
                    catch (java.io.IOException e) {
                    }
                } // end for: through each dropped file
            } // end filesDropped
        }); // end FileDrop.Listener
 
        frame.setBounds(100, 100, 300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } // end main
 
}
