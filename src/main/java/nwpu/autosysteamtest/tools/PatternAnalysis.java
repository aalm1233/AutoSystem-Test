package nwpu.autosysteamtest.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import nl.flotsam.xeger.Xeger;

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
			}if(t == '\\'){
				stack.push(t);
				continue frist;
			}if(!stack.isEmpty()&&stack.peek() == '\\'){
				stack.pop();
				gg: switch (t) {
				case '.':
					sb.append("\\.");
					break gg;
				case 'd':
					sb.append("[0-9]");
					break gg;
				case 'D':
					sb.append("[^0-9]");
					break gg;
				case 'w':
					sb.append("[A-Za-z_0-9]");
					break gg;
				case 'W':
					sb.append("[^A-Za-z_0-9]");
					break gg;
				default:
					sb.append("\\"+t);
					break gg;
				}
				continue frist;
			}if(t == '$'){
				continue;
			}
			sb.append(t);
		}
		System.out.println(sb.toString());
		return sb.toString();
		
	}
	public ArrayList<String> getValues(){
		ArrayList<String> values = new ArrayList<>();
		String regex = patten;
        Xeger generator = new Xeger(regex);    
        for(int i = 0;i<5;i++){
        	String result = generator.generate();
            assert result.matches(regex);
            values.add(result);
        }
        return values;
	}
	public ArrayList<String> getValues(int length){
		ArrayList<String> values = new ArrayList<>();
		String regex = patten+"{"+length+"}";
        Xeger generator = new Xeger(regex);
        String result = generator.generate();
        assert result.matches(regex);
        values.add(result);
        return values;
	}
	public ArrayList<String> getValues(int 	minLength,int maxLength){
		ArrayList<String> values = new ArrayList<>();
		String regex = patten+"{"+minLength+","+maxLength+"}";
        Xeger generator = new Xeger(regex);
        String result = generator.generate();
        assert result.matches(regex);
        values.add(result);
        return values;
	}
	public static void main(String[] args){
		try {
			BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Dengtong\\Desktop\\Travledate.txt"));
			PatternAnalysis pa = new PatternAnalysis(in.readLine());
			System.out.println(pa.getValues().toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
