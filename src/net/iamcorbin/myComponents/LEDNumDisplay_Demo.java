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
        f.add(new LEDNumDisplay(10, 20, 120, 3));
        f.setSize(400,300);
        f.setVisible(true);
    } 

}

class LEDNumDisplay extends JPanel {
	private static final long serialVersionUID = 1L;
	//debug, counting number of times paintComponent is called
	private int count = 0;
	private String msg;
	//the number of digits in the display
	private final int digits;
	//size reference
	private final int size;
	//x reference
	private final int xPos;
	//y reference
	private final int yPos;
	//the maximum number that can be displayed with this many digits
	private double maxnum;
    LEDNum[] led_nums;
    /**
     * Constructor
     * @param d set number of digits in the display
     * @param x set upper left x-coordinate of display 
     * @param y set upper left y-coordinate of display
     * @param s size of display
     */
    public LEDNumDisplay(int d, int x, int y, int s) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.digits = d;
        this.xPos = x;
        this.yPos = y;
        this.size = s;
        //set max number
        this.maxnum = Math.pow(10,d)-1;
        //create digits
        led_nums = new LEDNum[10];
        for(int n=0; n<this.digits; n++) {
        	//setup digit carrying
        	LEDNum c;
        	if(n>0)
        		c = led_nums[n-1];
        	else
        		c = null;
            //create digit
        	led_nums[n] = new LEDNum(x+((9*s)*n),y,s,c);
        }
        //setup initial display number
        led_nums[digits-1].add(count);
        
        addMouseListener(new MouseAdapter(){
	        public void mousePressed(MouseEvent e){
	           led_nums[digits-1].add(225);
	           count += 225;
		       //led_nums[n].setNum(led_nums[n].getNum()+1);
		       //repaint the digit
		       repaint(xPos,yPos,(size*9)*digits,size*16);
	        }
	    });
        /*
        //Listener that cycles each digit onClick
	    addMouseListener(new MouseAdapter(){
	        public void mousePressed(MouseEvent e){
	        	int mouseX = e.getX();
	        	int mouseY = e.getY();
	        	//check location of mouse and only change cooresponding digit
	        	for(int n=0; n<digits; n++) {
	        		int[] loc = led_nums[n].getLoc();
	        		if( (mouseX >= loc[0]) && (mouseX <= loc[0]+loc[2])
	        		 && (mouseY >= loc[1]) && (mouseY <= loc[1]+loc[3])) {
		        		led_nums[n].setNum(led_nums[n].getNum()+1);
		        		//repaint the digit
		            	repaint(loc[0],loc[1],loc[2],loc[3]);
	        		}
	        	}
	        }
	    });
	    */
    }

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        // Draw Text
        msg = "LEDNumDisplay";
        g.drawString(msg,10,20);

        for(int n=0; n<this.digits; n++) {
        	//paint digit
        	led_nums[n].paintNum(g);
        }
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
	//Reference to digit for carrying (or null if can't carry)
	private LEDNum carry;
	
    public LEDNum(int x, int y, int size, LEDNum c) {
    	width = size;
    	height = size*6;
    	padding = width;
    	xPos = x+padding;
    	yPos = y+padding;
    	carry = c;
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
    /**
     * Add to a digit
     * @param n the number to add
     */
    public void add(int n) {
    	int o,c;
    	num+=n;
    	o = num;
    	if(num>9) {
    		if(this.carry != null) {
    			//add to this digit
    			System.out.print("%10 ");
    			System.out.println(num % 10);
    			num = num % 10;
    			
    			//carry remainder up
    			c = (int)(o-num)/10;
    			this.carry.add(c);
    			System.out.print("Carrying ");
    			System.out.println(c);
    		} else {
    			System.out.println("MAXNUM HIT!");
    			num-=n;
    		}
    	}
    	System.out.println("Num = "+num+"");
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