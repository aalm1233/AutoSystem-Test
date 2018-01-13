package nwpu.autosysteamtest.tools;

import java.math.BigInteger;
import java.util.ArrayList;

import nl.flotsam.xeger.Xeger;
/**
 * 根据正则表达式生成数字
 * @author Dengtong
 * @version 0.2,08/01/2018
 */
public class PatternAnalysisNum extends PatternAnalysis{

	public PatternAnalysisNum(String patten) {
		super(patten);
	}
	public ArrayList<String> getValues(long min,long max){
		ArrayList<String> values = new ArrayList<>();
		String regex = patten;
        Xeger generator = new Xeger(regex);
        for(;values.size() < 5;){
        	 String result = generator.generate();
        	 assert result.matches(regex);
        	 try{
        		 long resultNum = Long.parseLong(result);
        		 if(min <= resultNum&&max >= resultNum){
        			 values.add(result);
        		 }
        	 }catch (Exception e) {
			}       	
        }
        return values;
	}
	public ArrayList<String> getValues(double min,double max){
		ArrayList<String> values = new ArrayList<>();
		String regex = patten;
        Xeger generator = new Xeger(regex);
        for(;values.size() < 5;){
        	 String result = generator.generate();
        	 assert result.matches(regex);
        	 try{
        		 double resultNum = Double.parseDouble(result);
        		 if(min <= resultNum&&max >= resultNum){
        			 values.add(result);
        		 }
        	 }catch (Exception e) {
			}       	
        }
        return values;
	}
	public ArrayList<String> getValues(BigInteger min, BigInteger max) {
		ArrayList<String> values = new ArrayList<>();
		String regex = patten;
        Xeger generator = new Xeger(regex);
        for(;values.size() < 5;){
        	 String result = generator.generate();
        	 assert result.matches(regex);
        	 try{
        		 BigInteger resultNum = new BigInteger(result);
        		 if(min.compareTo(resultNum) != -1&&resultNum.compareTo(max) != 1){
        			 values.add(result);
        		 }
        	 }catch (Exception e) {
			}       	
        }
        return values;
	}

}