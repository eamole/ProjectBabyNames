/**
 * @(#)SampleButtonListener.java
 *
 *
 * @author
 * @version 1.00 2015/3/12
 */

import java.awt.*;
import java.awt.event.*;

/*
 * extend this class to quickly get a window
 *
 */
abstract public class MyWindow extends Frame implements WindowListener{


    public MyWindow() {

		// super(title);

		setLayout(new FlowLayout());
		addWindowListener(this);	// for WindowClose - and other WindowEvents
		/*
		 	in here goes the custom  rubbish
		 	call it abstract init()

		*/
		// init(); // cannot call this cos constructor never returns

    }

    public MyWindow(LayoutManager layout) {
		setLayout(layout);
		addWindowListener(this);	// for WindowClose - and other WindowEvents
    }


    public MyWindow(String title) {
    	this();
		Util.debug("MyWindow(String) (after this())");
    	this.setTitle(title);
    }


	// this should really be overriden by user!!
    public void init() {
		Util.debug("MyWindow.init()");
		Util.debug("WyWindow.init() - calling abstract addContents()");
		addContents();
		pack();
		setVisible(true);
    }

	abstract public void addContents() ;


    // required for WindowListener
	public void windowDeactivated(WindowEvent e) {
	}
	public void windowActivated(WindowEvent e) {
	}
	public void windowDeiconified(WindowEvent e) {
	}
	public void windowIconified(WindowEvent e) {
	}
	public void windowClosed(WindowEvent e) {
	}
	// WindowCloseEvent!!
	public void windowClosing(WindowEvent e) {
		dispose();
		System.exit(0);
	}
	public void windowOpened(WindowEvent e) {
	}
	public void windowOpening(WindowEvent e) {
	}


}