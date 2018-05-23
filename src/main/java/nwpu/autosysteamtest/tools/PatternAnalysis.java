package nwpu.autosysteamtest.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import nl.flotsam.xeger.Xeger;
/**
 * BUG在关于[]的部分
 * @author Dengtong
 * @version 1.0,07/01/2018
 */
public class PatternAnalysis {
	String patten;
	public PatternAnalysis(String patten){
		this.patten = patten;
		this.patten = patternAnalysis();
	}
	/**
	 * 分析输入的正则表达式，转换成能够被解析的格式
	 * @return
	 */
	private String patternAnalysis() {
		Stack<Character> stack = new Stack<>();
		StringBuffer sb = new StringBuffer();
		frist: for(int i =0;i<patten.length();i++){
			char t = patten.charAt(i);
			if(t == '^'){
				continue frist;
			}if(t == '['){
				stack.push(t);
			}if(t == ']'){
				if(!stack.isEmpty()&&stack.peek() == '['){
					stack.pop();
				}
			}
			if(t == '\\'){
				stack.push(t);
				continue frist;
			}if(!stack.isEmpty()&&stack.peek() == '\\'){
				stack.pop();
				gg: switch (t) {
				case '.':
					sb.append("\\.");
					break gg;
				case 'd':
					if(!stack.isEmpty()&&stack.peek() == '['){
						sb.append("0-9");
					}else{
						sb.append("[0-9]");
					}
					break gg;
				case 'D':
					sb.append("[^0-9]");
					break gg;
				case 'w':
					if(!stack.isEmpty()&&stack.peek() == '['){
						sb.append("A-Za-z_0-9");
					}else{
						sb.append("[A-Za-z_0-9]");
					}
					break gg;
				case 'W':
					sb.append("[^A-Za-z_0-9]");
					break gg;
				default:
					sb.append("\\"+t);
					break gg;
				}
				continue frist;
			}else {
				if(t == '*'){
					sb.append("{0,}");
				}else if(t == '?'){
					sb.append("{0,1}");
				}else if(t == '+'){
					if(!stack.isEmpty()&&stack.peek() == '['){
						sb.append(t);
					}else{
						sb.append("{1,}");
					}
				}else if(t == '$'){
					continue;
				}else {
					sb.append(t);
				}
			}
		}
		return sb.toString();
		
	}
	/**
	 * 无条件获值
	 * @return
	 */
	public ArrayList<String> getValues(){
		ArrayList<String> values = new ArrayList<>();
		String regex = patten;
        Xeger generator = new Xeger(regex);    
        for(;values.size() < 5;){
        	String result = generator.generate();
            assert result.matches(regex);
            values.add(result);
        }
        return values;
	}
	/**
	 * 生成定长字符串
	 * @param length 字符串长度
	 * @return
	 */
	public ArrayList<String> getValues(int length){
		ArrayList<String> values = new ArrayList<>();
		String regex = patten;
        Xeger generator = new Xeger(regex);
        for(;values.size() < 5;){
        	 String result = generator.generate();
        	 assert result.matches(regex);
        	 if(result.length() == length){
            	 values.add(result);      		 
        	 }
        }
        return values;
	}
	/**
	 * 生成一定长度的字符串
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @return
	 */
	public ArrayList<String> getValues(int 	minLength,int maxLength){
		ArrayList<String> values = new ArrayList<>();
		String regex = patten;
        Xeger generator = new Xeger(regex);
        for(;values.size() < 5;){
         	 String result = generator.generate();
        	 assert result.matches(regex);
        	 if(minLength <= result.length()&&maxLength >= result.length()){
            	 values.add(result);
        	 }
        }
        return values;
	}

}
