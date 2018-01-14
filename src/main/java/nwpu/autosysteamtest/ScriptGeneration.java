package nwpu.autosysteamtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 
 * @author Dengtong
 * @version 3.1,05/01/2018
 */
public class ScriptGeneration {
	DocumentPrepcessing  documentPrepcessing;
	private ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> updateInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> mode;
	private String path;
	int filenum;
	String resourcesId;
	public ScriptGeneration(String path,
			ConcurrentHashMap<String, ArrayList<String>> mode) throws InterruptedException {
		documentPrepcessing = DocumentPrepcessing.getInstance();
		this.path = path;
		this.addInterfaceSetMap = documentPrepcessing.getAddInterfaceSetMap();
		this.deleteInterfaceSetMap = documentPrepcessing.getDeleteInterfaceSetMap();
		this.updateInterfaceSetMap = documentPrepcessing.getUpdateInterfaceSetMap();
		this.findInterfaceSetMap = documentPrepcessing.getFindInterfaceSetMap();
		this.mode = mode;
	}

	/**
	 * 
	 * 生成操作模型文件
	 * 
	 * @param fileName

	 * @return
	 * @throws FileNotFoundException
	 */
	private void generatingTestModelFiles(String fileName) throws FileNotFoundException {
		String inputfileName = fileName + "Model.txt";
		PrintWriter out = new PrintWriter(path + "testmodelfiles\\" + inputfileName);
		ArrayList<String> guizes = mode.get(resourcesId);
		for (String guize : guizes) {
			out.println(guize);
			out.flush();
		}
		out.close();
	}

	/**
	 * 生成操作序列文件
	 * 
	 * @param filename

	 * @return
	 * @throws IOException
	 */
	private int generatingOperationSequenceFiles(String filename) throws IOException {
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
				num++;
				out.flush();
			}
			out.close();
		}
		in.close();
		return num;
	}

	/**
	 * 输入模型序列文件
	 * 
	 * @param filename

	 * @throws IOException
	 */
	private void generatingInterfaceSequenceFiles(String filename) throws IOException {
		String inputfilename = filename + "OperationSequence.txt";
		BufferedReader in = new BufferedReader(new FileReader(path + "operationsequencelfiles\\" + inputfilename));
		File file = null;
		try{
			file = new File(path + "outputxml\\"+filename);
			if(!file.exists()){
				file.mkdirs();
			}
		}catch(Exception e){
			
		}finally {
			file = null;
		}
		String line = null;
		while ((line = in.readLine()) != null) {
			new Cartesian(filename,line,resourcesId);	
		}
		in.close();
	}
	class Cartesian{
		int addnum;
		int findnum;
		int deletenum;
		int updatenum;
		ArrayList<String> addInterfaceSet;
		ArrayList<String> deleteInterfaceSet;
		ArrayList<String> findInterfaceSet;
		ArrayList<String> updateInterfaceSet;
		String line;
		String filename;
		int sequenceLenth;
		public Cartesian(String filename, String line,String resourcesId){
			this.filename = filename;
			this.addnum = addInterfaceSetMap.get(resourcesId).size();
			this.findnum = findInterfaceSetMap.get(resourcesId).size();
			this.deletenum = 0;
			this.updatenum = 0;
			try {
				this.deletenum = deleteInterfaceSetMap.get(resourcesId).size();
			} catch (NullPointerException e) {
			}
			try {
				this.updatenum = updateInterfaceSetMap.get(resourcesId).size();
			} catch (NullPointerException e) {
			}
			this.line = line;
			this.sequenceLenth = line.length();
			addInterfaceSet = addInterfaceSetMap.get(resourcesId);
			deleteInterfaceSet = deleteInterfaceSetMap.get(resourcesId);
			findInterfaceSet = findInterfaceSetMap.get(resourcesId);
			updateInterfaceSet = updateInterfaceSetMap.get(resourcesId);
			recursiveGenerationInterfaceSequence(0,new ArrayList<String>());
		}
		/**
		 * 递归生成脚本(接口序列)
		 * @param i
		 * @param arrayList 接口序列
		 */
		private void recursiveGenerationInterfaceSequence(int i,ArrayList<String> arrayList){
			if(i<sequenceLenth){
				switch (line.charAt(i)) {
				case 'A':
					for(int j = 0;j<addnum;j++){
						arrayList.add("add "+addInterfaceSet.get(j));
						if(interfaceSequenceConstraint1(arrayList)){
							recursiveGenerationInterfaceSequence(++i,arrayList);
						}
						arrayList.remove(arrayList.size()-1);
					}
					break;
				case 'U':
					for(int j = 0;j<updatenum;j++){
						arrayList.add("update "+updateInterfaceSet.get(j));
						if(interfaceSequenceConstraint1(arrayList)){
							recursiveGenerationInterfaceSequence(++i,arrayList);
						}
						arrayList.remove(arrayList.size()-1);
					}
					break;
				case 'D':
					for(int j = 0;j<deletenum;j++){
						arrayList.add("delete "+deleteInterfaceSet.get(j));
						if(interfaceSequenceConstraint1(arrayList)){
							recursiveGenerationInterfaceSequence(++i,arrayList);
						}
						arrayList.remove(arrayList.size()-1);
					}
					break;
				case 'F':
					for(int j = 0;j<findnum;j++){
						arrayList.add("find "+findInterfaceSet.get(j));
						if(interfaceSequenceConstraint2(arrayList)){
							recursiveGenerationInterfaceSequence(++i,arrayList);
						}
						arrayList.remove(arrayList.size()-1);
					}
					break;
				default:
					break;
				}
			}else{
				try {
						generatingInterfaceSequenceFiles(arrayList);
						filenum++;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * 接口序列约束规则1:同一个add，delete,update接口在一次操作接口序列中不重复使用。
		 * 
		 * @return
		 */
		private boolean interfaceSequenceConstraint1(ArrayList<String> arrayList) {
			String last = arrayList.get(arrayList.size()-1);
			int tempnum = 0;
			for(String line :arrayList){
				if(last.equals(line)){
					tempnum ++;
				}
			}
			if(tempnum != 1){
				return false;
			}
			else {
				return true;
			}
		}
		/**
		 * 接口序列约束规则2:同一个find接口不在一次操作接口序列中连续出现。
		 * 
		 * @return
		 */
		private boolean interfaceSequenceConstraint2(ArrayList<String> arrayList) {
			int size = arrayList.size()-1;
			if(arrayList.get(size).equals(arrayList.get(size-1))){
				return false;
			}else{
				return true;
			}
		}


		/**
		 * 生成接口序列文件
		 * 
		 * @param filename
		 * @param line
		 * @param num
		 * @throws FileNotFoundException
		 */
		private void generatingInterfaceSequenceFiles(ArrayList<String> output) throws FileNotFoundException {
			int xnum = sequenceLenth-2;
			File file = null;
			try{
				file = new File(path + "outputxml\\"+filename+"\\"+xnum);
				if(!file.exists()){
					file.mkdirs();
				}
			}catch(Exception e){
				
			}
			PrintWriter out = new PrintWriter(path + "outputxml\\"+filename+"\\"+xnum+"\\"+filenum+".xml");
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.flush();
			out.println("<script resourcesID=\""+resourcesId+"\""+" sequence=\"\" completed=\"false\" >");
			out.flush();
			for(String line :output){
				String operation = line.split(" ")[0];
				String step = line.split(" ")[1];
				String name = step.split("\\|")[0].split(",")[0];
				String path = step.split("\\|")[0].split(",")[1];
				out.println("	<step operation=\""+operation+"\" path=\""+path+"\" name=\""+name+"\" >");
				out.flush();
				if(line.split("\\|")[1].split("->").length > 1){
					String params = line.split("\\|")[1].split("->")[1];
					String response = line.split("\\|")[1].split("->")[0];
					String[] param = params.split("_");
					String[] responseParam = null;
					try{
						responseParam =response.split("-")[2].split("_");
					}catch(Exception e){
					}
					for(String p :param){
						out.println("		<param name=\""+p.split(",")[0]+"\" attribute=\""+p.split(",")[2]+"\" value=\"\"/>");
						out.flush();
					}
					out.println("	<response name=\""+response.split("-")[0]+"\" type=\""+response.split("-")[1]+"\" >");
					out.flush();
					if(responseParam != null&&responseParam.length > 1){
						for(String p :responseParam){
							out.println("		<param name=\""+p.split(",")[0]+"\" attribute=\""+p.split(",")[2]+"\" />");
							out.flush();
						}
					}
					out.println("	</response>");
					out.flush();
				}
				out.println("	</step>");
				out.flush();
			}
			out.println("</script>");
			out.flush();
			out.close();
		}
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
		for (int i = 0; i < expressionLength - 2; i++) {
			recursiveGenerationOperationSequence(0, i, midle, new StringBuffer(head), result);
		}
		return result;
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
				recursiveGenerationOperationSequence(++i, j, midle, sb, result);
				sb.deleteCharAt(sb.length() - 1);
			}
			if (midle.contains("U")) {
				sb.append("U");
				if (operationSequenceConstraint2(sb.toString())) {
					recursiveGenerationOperationSequence(++i, j, midle, sb, result);
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			if (midle.contains("D")) {
				sb.append("D");
				if (operationSequenceConstraint2(sb.toString())) {
					recursiveGenerationOperationSequence(++i, j, midle, sb, result);
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			if (midle.contains("F")) {
				sb.append("F");
				recursiveGenerationOperationSequence(++i, j, midle, sb, result);
				sb.deleteCharAt(sb.length() - 1);
			}
		} else if (i == j) {
			if (operationSequenceConstraint1(sb.toString())) {
				result.add(sb.toString() + "F");
			}
		}
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
			} else if (result.charAt(i) == 'F') {
				dNum++;
			}
		}
		if (aNum < dNum) {
			output = false;
		}
		return output;
	}

	/**
	 * 获取接口序列的最大长度
	 * 
	 * @return
	 */
	private int getMaxExpressionLength() {
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
	public void run() throws IOException {
		Set<String> key = mode.keySet();
		File file = null;
		try{
			file = new File(path + "outputxml");
			if(!file.exists()){
				file.mkdirs();
			}
			file = new File(path + "testmodelfiles");
			if(!file.exists()){
				file.mkdirs();
			}
			file = new File(path + "operationsequencelfiles");
			if(!file.exists()){
				file.mkdirs();
			}
		}catch(Exception e){
			
		}finally {
			file = null;
		}
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			resourcesId = (String) it.next();
			String fileName = resourcesId;
			filenum = 1;
			generatingTestModelFiles(fileName);//生成操作模型
			generatingOperationSequenceFiles(fileName);//生成操作序列
			generatingInterfaceSequenceFiles(fileName);//生成接口序列
		}
	}

}
