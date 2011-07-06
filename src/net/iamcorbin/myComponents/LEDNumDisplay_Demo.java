/**
 * LEDNumDisplay_Demo
 * @date 7-6-2011
 * @author Corbin Tarrant
 * @site iamcorbin.net
 * @email Corbin@IAmCorbin.net
 *
 * This is a simple component that will simulate an analog 8-segment LED number display
 * 
 * Currently it displays a single number and will loop through 0-9 on mouse clicks
 */

package net.iamcorbin.myComponents;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

public class LEDNumDisplay_Demo {
    
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
        JFrame f = new JFrame("testLEDNumDisplay");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(new LEDNumDisplay());
        f.setSize(250,250);
        f.setVisible(true);
    } 

}

class LEDNumDisplay extends JPanel {
	private static final long serialVersionUID = 1L;
	//debug, counting number of times paintComponent is called
	private int count = 0;
	private String msg;
    LEDNum led_num = new LEDNum(20,60,8);

    public LEDNumDisplay() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        final LEDNumDisplay self = this;

	    addMouseListener(new MouseAdapter(){
	        public void mousePressed(MouseEvent e){
	        	led_num.setNum(led_num.getNum()+1);
	            int[] loc = led_num.getLoc();
	            self.repaint(loc[0],loc[1],loc[2],loc[3]);
	        }
	    });
    }

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        // Draw Text
        msg = "LEDNumDisplay - Custom Panel";
        g.drawString(msg,10,20);
        // Draw paintComponent count
        msg = "paintComponent() called "+(++count)+" times";
        g.drawString(msg,10,40);

        led_num.paintNum(g);
    }  
}

class LEDNum{
	//upper left coordinate of number display
    private int xPos;
    //upper right coordinate of number display
    private int yPos;
    //width of bars
    private int width;
    //height of bars
    private int height;
    //the number currently displayed
	private int num = 0;
	//padding around the numbers
	private int padding;
	//off color for a segment
	private Color c_off = new Color(0.2f,0.0f,0.0f);
	//on color for a segment
	private Color c_on = new Color(1.0f,0.0f,0.0f);
	//number border color
	private Color c_border = new Color(0.1f, 0.1f, 0.1f);

    public LEDNum(int x, int y, int size) {
    	width = size;
    	height = size*6;
    	padding = width;
    	xPos = x+padding;
    	yPos = y+padding;
    }
    
    public void setX(int xPos){ 
        this.xPos = xPos;
    }

    public int getX(){
        return xPos;
    }

    public void setY(int yPos){
        this.yPos = yPos;
    }

    public int getY(){
        return yPos;
    }

    public int getWidth(){
        return width;
    } 

    public int getHeight(){
        return height;
    }

    public void setNum(int n) {
    	if(n < 10 && n > -1)
    		num = n;
    	else {
    		System.out.println("LEDNum.setNum - Attempted to set a number above 9, defaulting to 0");
    		num = 0;
    	}
    }
    
    public int getNum() {
    	return num;
    }
    
    public int[] getLoc() {
    	int[] a = {xPos-padding, yPos-padding, (2*padding)+width*7, (2*padding)+width*15};
    	return a;
    }
    
    public void paintNum(Graphics g){
        //background
    	g.setColor(Color.BLACK);
    	int[] loc = getLoc();
    	g.fillRect(loc[0],loc[1],loc[2],loc[3]);
    	//left-top
    	if(num == 0||num == 4||num == 5||num == 6||num == 8||num == 9)
    		g.setColor(c_on);
    	else
    		g.setColor(c_off);
        g.fill3DRect(xPos,yPos+(1*width),width,height, true);
        //left-top-border
        g.setColor(c_border);
        g.draw3DRect(xPos,yPos+(1*width),width,height,true);
        //top
        if(num == 0||num == 2||num == 3||num == 5||num == 6||num == 7||num == 8||num == 9)
    		g.setColor(c_on);
    	else
    		g.setColor(c_off);
        g.fill3DRect(xPos+(1*width),yPos,width*5,width, true);
        //top-border
        g.setColor(c_border);
        g.draw3DRect(xPos+(1*width),yPos,width*5,width,true);
        //right-top
        if(num == 0||num == 1||num == 2||num == 3||num == 4||num == 7||num == 8||num == 9)
    		g.setColor(c_on);
    	else
    		g.setColor(c_off);
        g.fill3DRect(xPos+(6*width),yPos+(1*width),width,height, true);
        //right-top-border
        g.setColor(c_border);
        g.draw3DRect(xPos+(6*width),yPos+(1*width),width,height,true);
        //middle
        if(num == 2||num == 3||num == 4||num == 5||num == 6||num == 8||num == 9)
    		g.setColor(c_on);
    	else
    		g.setColor(c_off);
        g.fill3DRect(xPos+(1*width),yPos+(7*width),width*5,width, true);
        //middle-border
        g.setColor(c_border);
        g.draw3DRect(xPos+(1*width),yPos+(7*width),width*5,width,true);
        //left-bottom
        if(num == 0||num == 2||num == 6||num == 8)
    		g.setColor(c_on);
    	else
    		g.setColor(c_off);
        g.fill3DRect(xPos,yPos+(8*width),width,height, true);
        //left-bottom-border
        g.setColor(c_border);
        g.draw3DRect(xPos,yPos+(8*width),width,height,true);
        //bottom
        if(num == 0||num == 2||num == 3||num == 5||num == 6||num == 8||num == 9)
    		g.setColor(c_on);
    	else
    		g.setColor(c_off);
        g.fill3DRect(xPos+(1*width),yPos+(14*width),width*5,width,true);
        //bottom-border
        g.setColor(c_border);
        g.draw3DRect(xPos+(1*width),yPos+(14*width),width*5,width,true);    
        //right-bottom
        if(num == 0||num == 1||num == 3||num == 4||num == 5||num == 6||num == 7||num == 8||num == 9)
    		g.setColor(c_on);
    	else
    		g.setColor(c_off);
        g.fill3DRect(xPos+(6*width),yPos+(8*width),width,height, true);
        //right-bottom-border
        g.setColor(c_border);
        g.draw3DRect(xPos+(6*width),yPos+(8*width),width,height,true);
    }
}