/**
 * @(#)Model.java
 *
 *
 * @author
 * @version 1.00 2015/3/24
 */

import java.io.*;
import java.nio.charset.*;	// charset

public class Model {
	public BoysNames boysNames;
	public GirlsNames girlsNames;
    public Model() {

    	boysNames=new BoysNames();
    	girlsNames=new GirlsNames();

    }

    public static void main(String[] args) {

    	Model model = new Model();
    	model.boysNames.search("Henry");
    	if(model.boysNames.found()) {
    		Util.out("\n Henry found!");
    	} else {
    		Util.out("\n Henry not found!");
    	}

    }


}
/*
 * the data file loads all the data from the give file
 * into an array - easier
 */
class DataFile {

	// this is for the xAxis
	public static String[] xDataPoints=new String[] {"1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013"};
	public static String[] yDataPoints;

	static int minRank=0;
	static int maxRank=1100;

	private Record[] data=new Record[1000];	// reserve a 1000, but ideally, just resize the array, adding blocks of 100
	private int nextEl=0;
	private String filename;

	private boolean isFound=false;
	public Record currRec=null; // the current record or the search result

	public DataFile(String filename) {
		this.filename=filename;
		yDataPoints=new String[12];
		int j=1;
		for(int i=0;i<yDataPoints.length;i++) {
			yDataPoints[i]=""+j;
			if(j>1){
				j+=100;	// patch
			} else {
				j+=99;
			}
		}
		readFile();
	}

	public void readFile() {

		Util.debug("DataFile.readFile();");

		String line;
		BufferedReader br=null;	// needs to be out here
		try{	// catch file/io error
		    InputStream fis = new FileInputStream(filename);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    br = new BufferedReader(isr);
		} catch(Exception e)  {
			Util.error("File error " + filename +"\n "+e.getMessage());
		}

		try{
			int i=1;
		    while ((line = br.readLine()) != null) {
		        // save the line
				data[nextEl++]=new Record(line,i++);
		    }
		} catch(Exception e) {
			Util.error("IO error " + filename +"\n "+e.getMessage());
		}

	}

	public boolean search(String name) {
		name=name.trim().toUpperCase();
		Util.debug("Search for ["+name+"]");
		int i=0;
		isFound=false;
		while(!isFound && i<nextEl) {
			currRec=data[i++];	// INC
			String temp = currRec.name.toUpperCase();
			isFound = temp.equals(name);// uppercase
			// Util.debug("data : ["+temp+"]==["+name+"] "+isFound);
		}
		return isFound;
	}
	public boolean found() {
		return isFound;
	}
}


class BoysNames extends DataFile {

	static int MinRank=0;
	static int MaxRank=1100;

	public BoysNames() {
		super("BoysNames1998-2013.csv");
	}
}
class GirlsNames extends DataFile {

	static int MinRank=0;
	static int MaxRank=1100;

	public GirlsNames() {
		super("GirlsNames1998-2013.csv");
	}

}


class Record {

	public static final int YEARS=16;	// the number of years for which ranking data is available

	public String name;
	public int[] rank=new int[YEARS];	// goes from 1 (highest) to 1100 (lowest)

	public Record(String line,int lineNo) {
		String[] fields=line.split(",");
		if(fields.length!=(YEARS+1)) {
			Util.error("Invalid record length : " + fields.length +" on record " + lineNo);
		} else {
			/*
			 * name is a bit of a kludge - surrounded in quotes - need to eliminate quotes
			 */
			String[] temp = fields[0].split("\"");
			name=temp[1].trim();
			for(int i=1;i<fields.length;i++) {
				rank[i-1]=Integer.parseInt(fields[i]);
				// *** should test/save for min/max rankings!!! save in Model/Gender static
				// can't use static - don't know which class!! and can't override DataFile!!
			}
		}
	}


}
