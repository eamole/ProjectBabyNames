/**
 * @(#)Util.java
 *
 *
 * @author
 * @version 1.00 2015/2/20
 */


public class Util {

	public static boolean debugOn=true;
    public Util() {
    }
	// assert is keyword!!
    public static void test(String prompt,String expected,String actual) {
    	Util.out("\n"+prompt+":\t");
    	if(expected.equals(actual)) {
    		Util.out("pass");
    	} else {
    		Util.out("*** fail ***") ;
    		Util.out("\nExpected : " +expected);
    		Util.out("\n  Actual : " +actual);
    	}
    }
    public static void test(String test,boolean trueExpected) {
    	Util.out("\n"+test+":\t");
    	if(trueExpected) {	// comparing booleans == v &&
    		Util.out("pass");
    	} else {
    		Util.out("*** fail ***") ;
    	}
    }


	public static void debug(String output) {
		if(debugOn){
			System.out.print("\nDebug > "+output);
		}
	}

	public static void out(String output) {
		System.out.print(output);
	}

	public static void out(String output,double[] options) {
		for(int i=0;i<options.length;i++) {
			output=output.replace("%"+(i+1),""+options[i]);
		}
		System.out.print(output);
	}
	public static void out(String output,int[] options) {
		for(int i=0;i<options.length;i++) {
			output=output.replace("%"+(i+1),""+options[i]);
		}
		System.out.print(output);
	}

	public static void out(int output) {
		System.out.print(output);
	}
	public static void out(String[] output) {
    	for(int i=0;i<output.length;i++) {
			System.out.print(" "+output[i]);
		}
	}

	public static void out(long[] output) {
    	for(int i=0;i<output.length;i++) {
			System.out.print(" " + output[i]);
		}
	}
	public static void out(int[] output) {
		for(int i=0;i<output.length;i++) {
			System.out.print(" "+output[i]);

		}
	}
	public static void out(boolean[] output) {
		for(int i=0;i<output.length;i++) {
			System.out.print(" "+output[i]);
		}
	}
	public static void out(Object output) {
		System.out.print(output.toString());
	}


	public static String toString(int[] output) {
		String out="";
		for(int i=0;i<output.length;i++) {
			out+=" "+output[i];
		}
		return out;
	}
	public static String toString(double[] output) {
		String out="";
		for(int i=0;i<output.length;i++) {
			out+=" "+output[i];
		}
		return out;
	}


	public static void outln(String text) {
		System.out.println(text);
	}
	public static void outln(int text) {
		System.out.println(text);
	}

	public static void error(String text) {
		// log the error, notify sys admin....
		System.out.println("\nError : " + text);
	}

	public static void error(Exception e) {
		// log the error, notify sys admin....
		System.out.println("\nError : " + e.getMessage());
	}


    public static int compare(int num1,int num2){
		if(num1<num2) return -1;
		if(num1==num2) return 0;
		return 1;
    }

    public static int[] secsToTime(int totSecs) {
    	int[] time=new int[3];
    	time[0]=totSecs/(60*60);	// integer part only
    	totSecs-=time[0]*(60*60);	// find the remaining seconds - couyld use mod operator
    	time[1]=totSecs/60;
    	time[2]=totSecs-(time[1]*60);	// find the remaining seconds - couyld use mod operator
    	return time;

    }

    public static int timeToSecs(int hours, int mins, int secs) {
    	// add the hours to the minutes (after convert)
    	mins+=hours*60;
    	secs+=mins*60;
    	return secs;
    }

    public static long min(long[] args) {
		long min=Long.MAX_VALUE;
		for(int i=0;i<args.length;i++) {
			min=Math.min(min,args[i]);
		}
		return min;
    }
    public static double min(double[] args) {
		double min=Double.MAX_VALUE;
		for(int i=0;i<args.length;i++) {
			min=Math.min(min,args[i]);
		}
		return min;
    }

    public static long max(long[] args) {
		long max=Long.MIN_VALUE;
		for(int i=0;i<args.length;i++) {
			max=Math.max(max,args[i]);
		}
		return max;
    }
    public static double max(double[] args) {
		double max=Double.MIN_VALUE;
		for(int i=0;i<args.length;i++) {
			max=Math.max(max,args[i]);
		}
		return max;
    }

    public static boolean inRange(int val, int low, int high) {
    	return (val<=high) && (val >=low);
    }
    public static boolean[] inRange(int[] vals, int low, int high) {
    	boolean[] flags=new boolean[vals.length]; //
		for(int i=0;i<vals.length;i++) {
			flags[i]=inRange(vals[i],low,high);
		}
		return flags;
    }


    public static boolean odd(boolean[]args) {
		int count=0;
		for(int i=0;i<=args.length;i++) {
			if(args[i]) count++;
		}
		return (count%2)==0;
    }


}