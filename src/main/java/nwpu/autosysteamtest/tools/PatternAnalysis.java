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
 *
 */
public class PatternAnalysis {
	String patten;
	public PatternAnalysis(String patten){
		this.patten = patten;
		this.patten = patternAnalysis();
	}
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
	public ArrayList<String> getValues(){
		ArrayList<String> values = new ArrayList<>();
		System.out.println(patten);
		String regex = patten;
        Xeger generator = new Xeger(regex);    
        for(;values.size() < 5;){
        	String result = generator.generate();
            assert result.matches(regex);
            values.add(result);
        }
        return values;
	}
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
	public static void main(String[] args){
		try {
			BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Dengtong\\Desktop\\Travledate.txt"));
			PatternAnalysis pa = new PatternAnalysis("[\\w\n]{3,7}");
			in.close();
			System.out.println(pa.getValues().get(0));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
