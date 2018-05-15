package nwpu.autosysteamtest;

import java.util.ArrayList;

import nwpu.autosysteamtest.tools.PatternAnalysis;

public class ToolTest {

	public static void main(String[] args){
		PatternAnalysis pa = new PatternAnalysis("");
		ArrayList<String> a = pa.getValues();
		System.out.println(a.get(0));
	}
}
