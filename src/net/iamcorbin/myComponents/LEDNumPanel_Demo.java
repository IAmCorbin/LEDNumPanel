/**
 * LEDNumPanel_Demo
 * @date 7-6-2011
 * @author Corbin Tarrant
 * @site iamcorbin.net
 * @email Corbin@IAmCorbin.net
 *
 * This is a simple component that will simulate an analog 8-segment LED number panel
 * 
 */

package net.iamcorbin.myComponents;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LEDNumPanel_Demo {
    
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
        JFrame f = new JFrame("testLEDNumPanel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(new LEDNumPanel(10, 20, 120, 8));
        f.setSize(800,300);
        f.setVisible(true);
    } 

}

class LEDNumPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	//debug, counting number of times paintComponent is called
	private int count = 10000;
	private String msg;
	//the number of digits in the panel
	private final int digits;
	//size reference
	private final int size;
	//x reference
	private final int xPos;
	//y reference
	private final int yPos;
	//The LED Digits
    LEDNum[] led_nums;
    /**
     * Constructor
     * @param d set number of digits in the panel
     * @param x set upper left x-coordinate of panel 
     * @param y set upper left y-coordinate of panel
     * @param s size of panel
     */
    public LEDNumPanel(int d, int x, int y, int s) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.digits = d;
        this.xPos = x;
        this.yPos = y;
        this.size = s;

        //create digits
        led_nums = new LEDNum[this.digits];
        LEDNum temp = null;
        for(int n=this.digits-1; n>=0; n--) {
            //create digit
        	led_nums[n] = new LEDNum(x+((9*s)*(this.digits-1-n)),y,s, temp);
        	//save reference to store this position in the next digit
        	temp = led_nums[n];
        }
        //setup initial panel number
        //addToPanel(count);
        this.led_nums[0].add(count);
        
        addMouseListener(new MouseAdapter(){
	        public void mousePressed(MouseEvent e){
	           //addToPanel(3);
	           led_nums[0].add(-3);
		       //repaint the digits
		       repaint(xPos,yPos,(size*9)*digits,size*16);
	        }
	    });
    }

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        // Draw Text
        msg = "LEDNumPanel";
        g.drawString(msg,10,20);

        for(int n=this.digits-1; n>=0; n--) {
        	//paint digit
        	led_nums[n].paintNum(g);
        }
    }  
}

class LEDNum{
	//upper left coordinate of number panel
    private int xPos;
    //upper right coordinate of number panel
    private int yPos;
    //width of bars
    private int width;
    //height of bars
    private int height;
    //the number currently displayed
	private int num = 0;
	//padding around the numbers
	private int padding;
	//reference to the next highest digit
	private LEDNum next = null;
	//off color for a segment
	private Color c_off = new Color(0.2f,0.0f,0.0f);
	//on color for a segment
	private Color c_on = new Color(1.0f,0.0f,0.0f);
	//number border color
	private Color c_border = new Color(0.1f, 0.1f, 0.1f);
	
    public LEDNum(int x, int y, int size, LEDNum n) {
    	width = size;
    	height = size*6;
    	padding = width;
    	xPos = x+padding;
    	yPos = y+padding;
    	next = n;
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
    	num = n;
    	if(n > 10 || n > -1)
    		System.out.println("WARNING - Number of a single digit was set above 9 or below 0");
    }
    /**
     * Add to a digit
     * @param n the number to add
     */
    public void add(int n) {
    	num += n;
    	if(num>9) {
    		//need to carry
	    	while(num>9) {
	    		if(next != null) {
	    			next.add(1);
	    			num -= 10;
	    		} else {
	    			System.out.println("MAXIMUM NUMBER DISPLAY CAN SHOW HAS BEEN REACHED");
	    			return;
	    		}
	    	}
    	} else if(num<0) {
    		//need to borrow
    		while(num<0) {
    			borrow();
    		}
    	}
    }
    
    /**recursive borrow function for subtraction
     * 
     */
    private void borrow() {
    	if(next != null) {
    		if(next.getNum()>0) {
    			next.add(-1);
    			this.add(10);
    		} else {
    			next.borrow();
    			next.add(-1);
    			this.add(10);
    		}
    	} else {
    		System.out.println("ERROR, NOTHING TO BORROW, NEGATIVE NUMBERS NOT SUPPORTED");
    		return;
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