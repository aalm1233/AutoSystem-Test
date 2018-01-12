package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 
 * @author Dengtong
 * @version 1.0,05/01/2018
 */
public abstract class BaseData {
	ConcurrentHashMap<String, String> constraint;
	public BaseData(ConcurrentHashMap<String, String> constraint){
		this.constraint = constraint;
	}
	public BaseData(){
	}
	/**
	 * 
	 * @return 生成的数据
	 * @throws ParseException
	 */
	abstract public ArrayList<String> constraintAnalysis() throws ParseException;
	
}
enum Constraints{
	//约束
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