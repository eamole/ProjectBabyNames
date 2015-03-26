/**
 * @(#)View.java
 *
 *
 * @author
 * @version 1.00 2015/3/24
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
//import javax.swing.event.ChangeEvent;

public class View extends MyWindow {
	Graph graph;
	Model model;

	// allow multi names - each has its own color
	Color[] colors=new Color[] {Color.RED,Color.BLUE,Color.GREEN,Color.CYAN,Color.YELLOW,Color.LIGHT_GRAY,Color.PINK,Color.ORANGE,Color.WHITE};
	String[] names = new String[colors.length];
	int nextName=0;

    public View(String title) {
    	super(title);
    	model = new Model();
    }


    public static void main(String[] args) {
    	View app = new View("Irish Baby Names 1998 - 2013");
    	app.init();
    }

    public void addContents() {
		Util.debug("View.addContents()");
    	Canvas canvas=new Canvas();
		add(canvas);

    }

	/*
	 * the Canvas is the main drawing area for the UI
	 */
    class Canvas extends JPanel {

		JPanel padding;
		Graphics g;	// save it for reuse

		public Canvas() {
			super();
			Util.debug("View.Canvas() - after super()->JPanel");

			// either here or in paintComponent();
			addContents();
		}

		// not required
		public void xpaintComponent(Graphics g) {
			Util.debug("View.Canvas.paintComponent(Graphics g)");
			super.paintComponent(g);
			this.g=g;	// save the context
			setPreferredSize(new Dimension(1000,800));

			// have to wait for this "event" before g is avaiulable
			addContents();
		}

		public void addContents() {

			Util.debug("View.Canvas.addContents()");

			setLayout(new BorderLayout());

			add( graph = new Graph(), BorderLayout.CENTER );
			// might be better to extend Graph and put this stuff in constructor
			graph.title="Popular Children's Names";
			graph.xTitle="Year";
			graph.yTitle="Rank";

			graph.setXLabels(DataFile.xDataPoints);
			graph.setYLabels(DataFile.yDataPoints);
			// used for scaling dataPoints - should really have a min as well!! assuming 0
			graph.dataMaxY=DataFile.maxRank;

			add( new Form() , BorderLayout.EAST );

			//add( padding=new JPanel(), BorderLayout.EAST);
			//padding.setPreferredSize(new Dimension(100,500));
			pack();

		}

    }

    class Form extends JPanel implements ChangeListener,ActionListener{

		RadioButtons radio;
		JTextField txtName;
		JButton btnSubmit;

		String name;

		Graphics g;

    	public Form() {
    		Util.debug("View.Form()");

			addContents();

    	}

    	public void addContents() {

    		setLayout( new GridLayout(26,1));
			setPreferredSize(new Dimension(150,200));

			JLabel lbl;
			add( lbl = new JLabel("Gender:"));
			radio = new RadioButtons(new String[] {"Boy","Girl"});
			MyPanel.add( this, radio.buttons);
			// MyPanel.add( this , MyPanel.radioButtons(new String[] {"Boy","Girl"}));
			//lbl.setLabelFor();	// need the group!!

			add( lbl= new JLabel("Name :"));
			add( txtName = new JTextField("") );
			txtName.addActionListener(this);	// same as button below!!
			txtName.setColumns(20);
			lbl.setLabelFor(txtName);

			add( btnSubmit = new JButton("Display Ranking"));
			btnSubmit.addActionListener(this);

			// pack();


    	}

		// not needed
    	public void xpaintComponent(Graphics g) {
    		Util.debug("Form.paintComponent()");
    		g.drawString("Hello",10,10);
    	}



		public void stateChanged(ChangeEvent e) {
			Util.debug("stateChanged");
		}
		public void actionPerformed(ActionEvent e) {
			// check for empty name
			name=txtName.getText().trim();
			if(name.equals("")) {
				txtName.setText("Enter a name");
			} else {
				Util.debug("search for " + name);
				search();
			}
		}

		// search for the name
		public void search() {

			Util.debug("Form.search() " + name);
			// first, need to determine which sex
			DataFile data=null;
			// should use "resource" strings!!!
			if(radio.value.equals("Boy")){
				data = model.boysNames;
			} else if(radio.value.equals("Girl"))  {
				data = model.girlsNames;
			} else {
				Util.debug("Invalid model : " + radio.value);
			}
			data.search(name);
			if(data.found()) {
				Util.debug("Name found " + name);
				//graph.init();


				names[nextName]=name;
				graph.plotColor=colors[nextName];
				// graph.drawString(name,200,100+(50*nextName),Graph.LEFT);

				Graphics g=getGraphics();
				g.setColor(colors[nextName]);
				g.setFont(new Font("default", Font.BOLD, 16));
				g.drawString(name,20,250+(20*nextName));
				nextName++; // INC

				if(nextName>=colors.length) {
					repaint();
					nextName=0;
				}

				graph.plotData(data.currRec.rank);
			} else {
				txtName.setText("Cannot find name " + name);
			}
		}
    }



}



class RadioButtons implements ActionListener{

	public JRadioButton[] buttons;
	public String value;

	public int defaultOption=0;

	public RadioButtons(String[] options) {

		ButtonGroup group=new ButtonGroup();
		buttons=new JRadioButton[options.length];

		for(int i=0;i<options.length;i++) {
			JRadioButton b = new JRadioButton(options[i]);
			buttons[i]=b;
			b.addActionListener(this);
			group.add(b);
		}
		// select the first option
		buttons[defaultOption].setSelected(true);
		value=options[defaultOption];
		// don't know how to return the array!!
	}

	public void actionPerformed(ActionEvent e) {
		value=e.getActionCommand();
		Util.debug("radio:selected : "+value);
	}
}
/*
 * some simple factory methods
 */
class MyPanel {

	public final static MyCheckBox[] checkBoxes(MyWindow frame,String[] options) {
		MyCheckBox[] controls=new MyCheckBox[options.length];
		for(int i=0;i<options.length;i++) {
			controls[i]=new MyCheckBox(frame,options[i]);
		}
		// don't know how to return the array!!
		return controls;
	}
	/*
	 * to add buttons and group them
	 */
	public final static JRadioButton[] radioButtons(String[] options) {

		ButtonGroup group=new ButtonGroup();
		JRadioButton[] buttons=new JRadioButton[options.length];

		for(int i=0;i<options.length;i++) {
			JRadioButton b = new JRadioButton(options[i]);
			buttons[i]=b;
			group.add(b);
		}
		// select the first option
		buttons[0].setSelected(true);
		// don't know how to return the array!!
		return buttons;
	}

	public final static void add(JPanel p, JComponent[] cb) {
		for(int i=0;i<cb.length;i++) {
			p.add(cb[i]);
		}

	}
}
class MyCheckBox extends JCheckBox implements ChangeListener {
	MyWindow frame;	// a bit clunky - needs some interface
	String option;
	public MyCheckBox(MyWindow frame,String option) {
		super(option);
		this.frame=frame;
		this.option=option;
		this.addChangeListener(this);
	}
	// ChangeListener interface
	public void stateChanged(ChangeEvent event) {
		Util.debug("onClick : "+option);
	}
}