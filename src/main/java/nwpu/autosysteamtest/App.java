package nwpu.autosysteamtest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import nwpu.autosysteamtest.enity.Operation;
import nwpu.autosysteamtest.enity.RequestElement;
import nwpu.autosysteamtest.enity.RequestParam;
import nwpu.autosysteamtest.enity.Service;

/**
 * @author Dengtong
 * @version 2.0,23/11/2017
 */
public class App 
{
	public static void main(String[] args) throws IOException, InterruptedException {
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
