/**
 * @(#)Controller.java
 *
 *
 * @author
 * @version 1.00 2015/3/24
 */


public class Controller {

    public Controller() {

		View view = new View("Irish Baby Names 1998-2013");
		Model model = view.model;

		view.init();

    }

    public static void main(String[] args) {
    	Controller app = new Controller();

    }


}