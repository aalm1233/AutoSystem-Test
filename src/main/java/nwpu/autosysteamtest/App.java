package nwpu.autosysteamtest;

import java.io.IOException;

/**
 * @author Dengtong
 * @version 2.0,23/11/2017
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException
    {
    	switch (args.length) {
		case 0:
			System.out.println("Please start in the following format >> Java App xmldirpath");
			break;
		case 1:
			String path = args[0];
			new RunThread(path);
			break;
		default:
			break;
		}
    }
    
}
