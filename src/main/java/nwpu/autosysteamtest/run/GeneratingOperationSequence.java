package nwpu.autosysteamtest.run;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import nwpu.autosysteamtest.entity.Service;
/**
 * 
 * @author Dengtong
 * @version 1.0,28/01/2018
 */
public class GeneratingOperationSequence {
	private Service service;
	private String filename;
	private String path;
	public GeneratingOperationSequence(Service service,String filename,String path){
		this.service = service;
		this.filename = filename;
		this.path = path;
	}
	/**
	 * 生成操作序列文件
	 * 
	 * @param filename

	 * @return
	 * @throws IOException
	 */
	public int generatingOperationSequenceFiles() throws IOException {
		String inputfilename = filename + "Model.txt";
		BufferedReader in = new BufferedReader(new FileReader(path + "testmodelfiles\\" + inputfilename));
		int num = 0;
		String line = null;
		while ((line = in.readLine()) != null) {
			String outputfileName = filename + "OperationSequence.txt";
			PrintWriter out = new PrintWriter(path + "operationsequencelfiles\\" + outputfileName);
			ArrayList<String> outputlines = analyticeExpression(line);
			for (String outputline : outputlines) {
				out.println(outputline);
				out.flush();
			}
			out.close();
		}
		in.close();
		return num;
	}
	/**
	 * 分析操作模型生成操作序列
	 * 
	 * @param line

	 * @return
	 */
	private ArrayList<String> analyticeExpression(String inputline) {
		ArrayList<String> result = new ArrayList<>();
		String line = inputline;
		String head = line.split("\\(")[0];
		String midle = line.split("\\(")[1].split("\\)")[0];
		String tail = line.split("\\)")[1];
		if (tail.length() < 2) {
			tail = "";
		} else {
			tail = tail.substring(1);
		}
		int expressionLength = getMaxExpressionLength();
		for (int i = 0; i < expressionLength-1; i++) {
			recursiveGenerationOperationSequence(0, i, midle, new StringBuffer(head), result);
		}
		result.add(0, "AF");
		return result;
		
	}
	/**
	 * 获取接口序列的最大长度
	 * 
	 * @return
	 */
	private int getMaxExpressionLength() {
		int addnum = service.getAdds().size();
		int deletenum = 0;
		int updatenum = 0;
		try {
			deletenum = service.getDeletes().size();
		} catch (NullPointerException e) {
		}
		try {
			updatenum = service.getUpdates().size();
		} catch (NullPointerException e) {
		}
		return (addnum + updatenum + deletenum) * 2;
	}
	/**
	 * 递归生成操作序列
	 * 
	 * @param result
	 */
	private void recursiveGenerationOperationSequence(int i, int j, String midle, StringBuffer sb, ArrayList<String> result) {
		if (i < j) {
			if (midle.contains("A")) {
				sb.append("A");
				recursiveGenerationOperationSequence(i+1, j, midle, sb, result);
				sb.deleteCharAt(sb.length() - 1);
			}
			if (midle.contains("U")) {
				sb.append("U");
				if (operationSequenceConstraint2(sb.toString())) {
					recursiveGenerationOperationSequence(i+1, j, midle, sb, result);
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			if (midle.contains("D")) {
				sb.append("D");
				if (operationSequenceConstraint2(sb.toString())) {
					recursiveGenerationOperationSequence(i+1, j, midle, sb, result);
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			if (midle.contains("F")) {
				if(operationSequenceConstraint4(sb.toString())){
					sb.append("F");
					recursiveGenerationOperationSequence(i+1, j, midle, sb, result);
					sb.deleteCharAt(sb.length() - 1);
				}
			}
		}else if (i == j) {
			if (operationSequenceConstraint3(sb.toString())&&operationSequenceConstraint1(sb.toString())) {
				result.add(sb.toString() + "F");
			}
		}
	}
	/**
	 * 操作序列约束规则3:当没有D和U时，只保留AF
	 * 
	 * @param result
	 * @return
	 */
	private boolean operationSequenceConstraint3(String result) {
		boolean output = true;
		int dNum = 0;// delete操作数量
		int uNum = 0;// update操作数量 
		for (int i = 0; i < result.length(); i++) {
			if (result.charAt(i) == 'U') {
				uNum++;
			} else if (result.charAt(i) == 'D') {
				dNum++;
			}
		}
		if (uNum == 0 && dNum == 0) {
			output = false;
		}
		return output;
	}
	/**
	 * 操作序列约束规则4:F不连续使用
	 * 
	 * @param result
	 * @return
	 */
	private boolean operationSequenceConstraint4(String result) {
		boolean output = true;
		if (result.charAt(result.length()-1) == 'F') {
			output = false;
		}
		return output;
	}
	/**
	 * 操作序列约束规则2:一个操纵作序列中，update之前add数量应当大于delete数量.
	 * 
	 * @param result
	 * @return
	 */
	private boolean operationSequenceConstraint2(String result) {
		boolean output = true;
		int aNum = 0;// add操作数量
		int dNum = 0;// delete操作数量
		for (int i = 0; i < result.length(); i++) {
			if (result.charAt(i) == 'A') {
				aNum++;
			} else if (result.charAt(i) == 'D') {
				dNum++;
			}
		}
		if (aNum < dNum) {
			output = false;
		}
		return output;
	}

	/**
	 * 操作序列约束规则1:一个操作序列中，add数量应当大于delete数量。
	 * 
	 * @param result
	 * @return
	 */
	private boolean operationSequenceConstraint1(String result) {
		boolean output = true;
		int aNum = 0;// add操作数量
		int dNum = 0;// delete操作数量
		for (int i = 0; i < result.length(); i++) {
			if (result.charAt(i) == 'A') {
				aNum++;
			} else if (result.charAt(i) == 'D') {
				dNum++;
			}
		}
		if (aNum < dNum) {
			output = false;
		}
		return output;
	}
}
