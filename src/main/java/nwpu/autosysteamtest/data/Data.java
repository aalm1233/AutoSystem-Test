package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Data {
	ConcurrentHashMap<String, String> constraint;
	public Data(ConcurrentHashMap<String, String> constraint){
		this.constraint = constraint;
	}
	public Data(){
	}
	abstract public ArrayList<String> constraintAnalysis() throws ParseException;
	
}
enum Constraints{
	enumeration
	,totalDigits
	,fractionDigit
	,minExclusive
	,maxExclusive
	,minInclusive
	,maxInclusive
	,length
	,minLength
	,maxLength
	,pattern
	,format
	,size
	,minSize
	,maxSize
	,whiteSpace
}