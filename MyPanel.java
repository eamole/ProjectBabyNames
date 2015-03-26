/**
 * @(#)MyPanel.java
 *
 *
 * @author
 * @version 1.00 2015/3/20
 */

import javax.swing.*;

abstract public class MyPanel extends JPanel{

    public MyPanel() {
    	super();

    }
    public void init() {
    	addContents();
    }

    abstract void addContents();


}