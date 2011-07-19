package net.iamcorbin.myComponents;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LEDNumPanel_Demo {
	//the scale size for the panel
	static final int SIZE = 8;
	static final int DIGITS = 5;
	
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }

    private static void createAndShowGUI() {
    	System.out.println("Created GUI on EDT? "+
        SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("LEDNumPanel_Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.getContentPane().setLayout(new BoxLayout(f.getContentPane(),BoxLayout.Y_AXIS));
        f.add(new LEDNumPanel(5,0,0,8,true));
        f.setSize(400,300);
        f.setVisible(true);
    } 

}
