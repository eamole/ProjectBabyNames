import java.awt.*;
import javax.swing.*;


/*
 * a generic graphing class
 */
class Graph extends JPanel {

	Graphics g;

	public int initMaxX=700;	// length of the x-axis (pixels)
	public int initMaxY=800;	// height of the y-axis (pixels)

	public int minX=0;
	public int maxX=0;

	public int minY=0;
	public int maxY=0;

	public int defaultMargin=50;			// margin(inside) between border and the drawing area
	public int marginBottom=defaultMargin;	// not really used
	public int marginLeft=defaultMargin;
	public int marginRight=defaultMargin;
	public int marginTop=defaultMargin;

	public int xOriginOffset=10;	// the distance from the y-axis of the first x data point

	public int dataMaxY=0;		// the largest data value - for scaling
	public int dataMinY=0;		// not used

	String[] xLabels;			// labels for the x-axis data points
	String[] yLabels;			// labels for the y-axis data points

	String xTitle="X Axis";		// title for the x-axis
	String yTitle="Y Axis";		// title for the y-axis
	String title="Graph";		// title for the graph

	Color plotColor=Color.WHITE;	// the color used for axes and titles and data points  - can be changed

	public Graph() {
		Util.debug("Graph()");

		setBorder(BorderFactory.createLineBorder(Color.black));
		setPreferredSize(new Dimension(initMaxX,initMaxY));
		setVisible(true);
		setSize();	// has to be visible before dimensions available

	}
	// doesn't work!!
	public static void main(String[] args) {
		Model model = new Model();
		DataFile data=model.boysNames;
		if(data.search("Tom")) {
			Graph graph=new Graph();
			graph.plotData(data.currRec.rank);
		}

	}



	public void setSize() {

		minX=0+marginLeft;
		maxX=initMaxX-marginRight;	// reduce X to get bottokm of drawing area

		minY=0+marginTop;
		maxY=initMaxY-marginBottom;

	}


	public String toString() {
		return 	"\nmin("+minX+","+minY+")" +
				"\nmax("+maxX+","+maxY+")";
	}

	public void paintComponent(Graphics g) {
		Util.debug("Graph.paintComponent(Graphics g)");
		super.paintComponent(g);
		this.g=g;	// save the context

		setBackground(Color.BLACK);
		g.setColor(Color.WHITE);

		// note that g counts from top left, but we are plotting from bottom left
		init();
		// have to wait for this "event" before g is avaiulable
		// addContents();

	}

	public void init() {
		Util.debug("Graph.init()");
		// need to draw the x-axis & the y - axis;
		// g.drawString( this.toString() , 10,10);

		Font saveFont=g.getFont();
		g.setFont(new Font("default", Font.BOLD, 16));
		drawString(title,minX + (maxX-minX)/2,30,CENTER);
		g.setFont(saveFont);

		drawXAxis();
		drawXLabels();

		drawYAxis();
		drawYLabels();


	}


	public void setXLabels(String[] labels){
		xLabels=labels;
	}
	// get the x ordinate for a given datapoint
	public int getX(int dataPoint) {
		Util.debug("Graph.getX(int)");

		int width=maxX-minX;
		int xOffset=width/xLabels.length;
		// xOffset is a more accurate description for the startgap
		int x=minX+xOriginOffset+(xOffset*dataPoint);	// move the first plot point to the right
		return x;
	}
	public void drawXLabels() {
		Util.debug("Graph.drawXLabels()");
		int width=maxX-minX;
		int xOffset=width/xLabels.length;

		for(int i=0;i<xLabels.length;i++) {
			int x=getX(i);
			// Util.debug("xLabel : " + xLabels[i]);
			// draw a vertical line
			g.drawLine(x,maxY+4,x,maxY-4);
			// should centre text on x
			drawString(xLabels[i],x,maxY+16,CENTER);
		}

		// center the x label on the axis
		int x=minX + (width/2);
		Font saveFont=g.getFont();
		g.setFont(new Font("default", Font.BOLD, 16));
		drawString(xTitle,x,maxY+30,CENTER);
		g.setFont(saveFont);


	}
	public void drawXAxis() {
		Util.debug("Graph.drawXAxis()");
		//g.drawString( this.toString() , 10,10);
		//g.drawString("This is some text",100,100);
		// double thick line
		setSize();
		g.drawLine(minX,maxY,maxX,maxY);
		g.drawLine(minX,maxY+1,maxX,maxY+1);

	}

	public void setYLabels(String[] labels){
		yLabels=labels;
	}

	public void drawYLabels() {
		Util.debug("Graph.drawYLabels()");
		int height=maxY-minY;
		// might use datapoints!!
		int yOffset=height/yLabels.length;

		for(int i=0;i<yLabels.length;i++) {
			int y=minY+(yOffset*i);
			// Util.debug("yLabel : " + yLabels[i]);
			// draw a vertical line
			g.drawLine(minX-4,y,minX+4,y);
			// should centre text on x
			drawString(yLabels[i],minX-5,y,RIGHT);
		}

		// need to rotate

		int y=maxY - (height/2);
		Util.debug("Y Title (y)" + y + " x " + (minX-40));
		Font saveFont=g.getFont();
		g.setFont(new Font("default", Font.BOLD, 16));
		drawString(yTitle,minX-40,y,MIDDLE);
		g.setFont(saveFont);
	}

	public void drawYAxis() {
		Util.debug("Graph.drawYAxis()");
		setSize();
		// double thick line
		g.drawLine(minX,maxY,minX,minY);
		g.drawLine(minX-1,maxY,minX-1,minY);

	}

	public void plotData(int[] data) {
		Util.debug("Graph.plotData(int[])");
		// cannot rely on saved g!!!
		Graphics g=getGraphics();

		// there should be as many data entries as xDataPoints
		int width=maxX-minX;
		int xOffset=width/xLabels.length;

		int height=maxY-minY;
		// might use datapoints!!
		int yOffset=height/yLabels.length;

		// use this to connect the dots
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath() ;

		Util.debug("maxY : "+maxY);
		for(int i=0;i<xLabels.length;i++) {
			/*
			 * the plotting is actual quite counter-intuitive
			 * the closer to 1, the closer to the top
			 * the closer to dataMax, the closer to the bottom
			 * and if data=0, its the x-axis
			 * therefore, we need to invert the data - ie the
			 */
			int x=getX(i);
			int y;
			if(data[i]==0) {
				y=0;
			} else {
				// calc the height of the bar based on closest to 1
				float dataY=dataMaxY;
				// see the invert below
				float ratio=( (dataY-data[i])/dataY);
				// Util.debug("ratio "+ratio);
				// the y value is based on the data
				float fy=height*ratio;
				y=(int) fy;
			}

			// to find the correct offset for y
			// distance from X axis - invert it
			y=maxY-y;

			Util.debug("point : (" + xLabels[i]+","+data[i]+") => ("+x+","+y+")");
			// draw a crosshairs
			g.setColor(Color.RED);
			int size=4;	// size of arms of crosshairs
			g.drawLine(x,y+size,x,y-size);
			g.drawLine(x-size,y,x+size,y);
			g.setColor(plotColor);
			if(i==0) {
				path.moveTo(x,y);
			} else {
				path.lineTo(x,y);
			}
			// repaint();
			// should centre text on x
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.draw(path);


	}

	/*
	 * ideally these should be additive - based on how to handle the
	 * alignment relative x and y ords
	 * that means they need to be extractable from a compound (added) value
	 * horiz : is 2 bits (to cover the three values)
	 * vert : needs to be offset above 4, therefore 5,6,7
	 * remove the horiz and the vert by dividing the align by 4
	 */


	// horizontal
	public final static int LEFT=0;
	public final static int CENTER=1;
	public final static int RIGHT=2;
	// vertical
	public final static int TOP=5;
	public final static int MIDDLE=6;
	public final static int BOTTOM=7;

	// x,y should really be relative to the graph origin!!!
	public void newDrawString(String text,int x, int y, int align) {
		Util.debug("Graph.drawString(\""+text+"\") @ ("+x+","+y+")");

		int horiz = align % 4;	// the lower bits
		int vert = align / 4;	// the higher bits

		// in this context, getG() is incorrect!!
		// Graphics g=getGraphics();
		Graphics2D g2d=(Graphics2D) g;
		g.setColor(plotColor);
		int width = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
		int height=g.getFontMetrics(getFont()).getHeight() * text.length();
		int oldX=x;
		int oldY=y;

		switch(horiz) {
			case LEFT :
				break;
			case CENTER :
				// I want the string to be centered on x
				x=x-(width/2);
				break;
			case RIGHT :
				x=x-width;
				break;
			default :
				Util.error("Invalid drawString alignment "+align);
				break;
		}
		// damn - forgot that I use a diff method for vertical strings
		switch(vert){

			case TOP:
				// y is unchanged
				break;
			case MIDDLE:
				// starting y
				y=y-(height/2);
				break;
			case BOTTOM :
				// starting y
				y=y-height;
				break;
			default :
				Util.error("Invalid drawString alignment "+align);
				break;
		}
		// How do I signal to rotate the text?
		if(vert>0) {
			drawVString(text,x,y);
			Util.debug("y->y' ("+oldY + "->" + y +")");
		} else {
			g.drawString(text,x,y);
			Util.debug("x->x' ("+oldX + "->" + x +")");
		}

	}
	// x,y should really be relative to the graph origin!!!
	public void drawString(String text,int x, int y, int align) {
		Util.debug("Graph.drawString(\""+text+"\") @ ("+x+","+y+")");

		// in this context, getG() is incorrect!!
		// Graphics g=getGraphics();
		Graphics2D g2d=(Graphics2D) g;
		g.setColor(plotColor);
		int width = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
		int height=g.getFontMetrics(getFont()).getHeight() * text.length();
		int oldX=x;
		int oldY=y;

		switch(align) {
			case LEFT :
				g.drawString(text,x,y);
				break;
			case CENTER :
				// I want the string to be centered on x
				x=x-(width/2);
				g.drawString(text,x,y);
				Util.debug("x->x' ("+oldX + "->" + x +")");
				break;
			case RIGHT :
				x=x-width;

				g.drawString(text,x,y);
				Util.debug("x->x' ("+oldX + "->" + x +")");
				break;
			case TOP:
				// y is unchanged
				drawVString(text,x,y);
				Util.debug("y->y' ("+oldY + "->" + y +")");
				break;
			case MIDDLE:
				// starting y
				y=y-(height/2);
				drawVString(text,x,y);
				Util.debug("y->y' ("+oldY + "->" + y +")");
				break;
			case BOTTOM :
				// starting y
				y=y-height;
				drawVString(text,x,y);
				Util.debug("y->y' ("+oldY + "->" + y +")");
				break;
			case 10 :
				Util.debug("Rotate text " + text);
				// save the current rotation
				java.awt.geom.AffineTransform origG2D = g2d.getTransform();
				g2d.rotate(-Math.PI/2);
				g2d.drawString(text,x,y);
				//restore
				g2d.setTransform(origG2D);
				break;
			default :
				Util.error("Invalid drawString alignment "+align);
				break;
		}



	}

	public void drawVString(String text,int x, int y) {
		for(int i=0;i<text.length();i++) {
			String ch=text.substring(i,i+1);
			// recurse
			drawString(ch,x,y,CENTER);
			// g.drawString(ch,x,y);
			y+=g.getFontMetrics(getFont()).getHeight()+1;
		}

	}


}

