package nwpu.autosysteamtest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptGeneration {
	private ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> updateInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> mode;
	private String path;

	public ScriptGeneration(String path, ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap,
			ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap,
			ConcurrentHashMap<String, ArrayList<String>> updateInterfaceSetMap,
			ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap,
			ConcurrentHashMap<String, ArrayList<String>> mode) {
		this.path = path;
		this.addInterfaceSetMap = addInterfaceSetMap;
		this.deleteInterfaceSetMap = deleteInterfaceSetMap;
		this.updateInterfaceSetMap = updateInterfaceSetMap;
		this.findInterfaceSetMap = findInterfaceSetMap;
		this.mode = mode;
	}

	/**
	 * 
	 * 生成操作模型文件
	 * 
	 * @param fileName
	 * @param resourcesId
	 * @return
	 * @throws FileNotFoundException
	 */
	private int GeneratingTestModelFiles(String fileName, String resourcesId) throws FileNotFoundException {
		String inputfileName = fileName + "Model.txt";
		PrintWriter out = new PrintWriter(path + "testmodelfiles\\" + inputfileName);
		ArrayList<String> guizes = mode.get(resourcesId);
		int filesnum = 1;
		for (String guize : guizes) {
			out.println(filesnum + " " + guize);
			filesnum++;
			out.flush();
		}
		out.close();
		return filesnum;
	}

	/**
	 * 生成操作序列文件
	 * 
	 * @param filename
	 * @param resourcesId
	 * @return
	 * @throws IOException
	 */
	private int GeneratingOperationSequenceFiles(String filename, String resourcesId) throws IOException {
		String inputfilename = filename + "Model.txt";
		BufferedReader in = new BufferedReader(new FileReader(path + "testmodelfiles\\" + inputfilename));
		int num = 0;
		String line = null;
		while ((line = in.readLine()) != null) {
			num++;
			String outputfileName = filename + "OperationSequence" + num + ".txt";
			PrintWriter out = new PrintWriter(path + "operationsequencelfiles\\" + outputfileName);
			ArrayList<String> outputlines = AnalyticeExpression(line, resourcesId);
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
	 * 生成接口序列文件
	 * 
	 * @param filename
	 * @param num
	 * @param resourcesId
	 * @throws IOException
	 */
	private void GeneratingInterfaceSequenceFiles(String filename, int num, String resourcesId) throws IOException {
		for (int i = 1; i <= num; i++) {
			String inputfilename = filename + "OperationSequence" + i + ".txt";
			BufferedReader in = new BufferedReader(new FileReader(path + "operationsequencelfiles\\" + inputfilename));
			String line = null;
			while ((line = in.readLine()) != null) {
				int sequenceLenth = line.length();
				int cartesianNum = MaxNum(resourcesId, sequenceLenth);
				int documentNum = 0;
				for (int i1 = 0; i1 < cartesianNum; i1++) {
					// 有错
					if (APISequenceConstraint()) {
						documentNum++;
						Cartesian(filename, line, documentNum, resourcesId);
					}
				}
			}
			in.close();
		}
	}

	public void run() throws IOException {
		Set<String> key = mode.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String resourcesId = (String) it.next();
			String fileName = resourcesId;
			GeneratingTestModelFiles(fileName, resourcesId);
			int num = GeneratingOperationSequenceFiles(fileName, resourcesId);
			GeneratingInterfaceSequenceFiles(fileName, num, resourcesId);
		}
	}

	/**
	 * 求笛卡儿积
	 * 
	 * @param filename
	 * @param line
	 * @param num
	 * @throws FileNotFoundException
	 */
	private void Cartesian(String filename, String line, int num, String resourcesId) throws FileNotFoundException {
		String outputfileName = filename + "APISequence" + num + ".xml";
		PrintWriter out = new PrintWriter(path + "operationsequencelfiles\\" + outputfileName);
		int sequenceLenth = line.length();
		int cartesianNum = MaxNum(resourcesId, sequenceLenth);

	}

	/**
	 * 接口序列约束
	 * 
	 * @return
	 */
	private boolean APISequenceConstraint() {
		return false;
	}

	/**
	 * 获取笛卡儿积的数量
	 * 
	 * @param resourcesId
	 * @return
	 */
	private int MaxNum(String resourcesId, int sequenceLenth) {
		int addnum = addInterfaceSetMap.get(resourcesId).size();
		int deletenum = 0;
		int updatenum = 0;
		int findnum = 0;
		try {
			deletenum = deleteInterfaceSetMap.get(resourcesId).size();
		} catch (NullPointerException e) {
		}
		try {
			updatenum = updateInterfaceSetMap.get(resourcesId).size();
		} catch (NullPointerException e) {
		}
		try {
			findnum = findInterfaceSetMap.get(resourcesId).size();
		} catch (NullPointerException e) {
		}
		int result = 0;
		// 未完成
		for (;;) {
			break;
		}

		return 0;
	}

	/**
	 * 分析操作模型生成操作序列
	 * 
	 * @param line
	 * @param resourcesId
	 * @return
	 */
	private ArrayList<String> AnalyticeExpression(String inputline, String resourcesId) {
		ArrayList<String> result = new ArrayList<>();
		String line = inputline.split(" ")[1];
		String head = line.split("\\(")[0];
		String midle = line.split("\\(")[1].split("\\)")[0];
		String tail = line.split("\\)")[1];
		if (tail.length() < 2) {
			tail = "";
		} else {
			tail = tail.substring(1);
		}
		int expressionLength = GetMaxExpressionLength(resourcesId);
		for (int i = 0; i <= expressionLength - 2; i++) {
			// 未完成
			StringBuffer expression = new StringBuffer();
			expression.append(head);
			for (int j = 0; j <= i; j++) {

			}
			expression.append(tail);
			if (OperationSequenceConstraint(expression.toString()))
				result.add(expression.toString());
		}
		return result;
	}

	/**
	 * 操作序列约束
	 * 
	 * @param input
	 * @return
	 */
	private boolean OperationSequenceConstraint(String input) {
		// 未完成
		boolean result = false;
		return result;
	}

	/**
	 * 获取接口序列的最大长度
	 * 
	 * @param resourcesId
	 * @return
	 */
	private int GetMaxExpressionLength(String resourcesId) {
		int addnum = addInterfaceSetMap.get(resourcesId).size();
		int deletenum = 0;
		int updatenum = 0;
		try {
			deletenum = deleteInterfaceSetMap.get(resourcesId).size();
		} catch (NullPointerException e) {
		}
		try {
			updatenum = updateInterfaceSetMap.get(resourcesId).size();
		} catch (NullPointerException e) {
		}
		return (addnum + updatenum + deletenum) * 2;
	}

}
