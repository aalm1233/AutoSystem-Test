package nwpu.autosysteamtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import nwpu.autosysteamtest.enity.Operation;
import nwpu.autosysteamtest.enity.RequestElement;
import nwpu.autosysteamtest.enity.RequestParam;
import nwpu.autosysteamtest.enity.ResponseParam;
import nwpu.autosysteamtest.enity.Service;

public class GeneratingInterfaceSequence {
	private int addnum;
	private int findnum;
	private int deletenum;
	private int updatenum;
	private Service service;
	private String filename;
	private String path;
	private int sequenceLenth;
	private int filenum;
	private String line;
	private ArrayList<Operation> finds;
	private ArrayList<Operation> adds;
	private ArrayList<Operation> deletes;
	private ArrayList<Operation> updates;
	
	public GeneratingInterfaceSequence(Service service,String filename,String path ){
		this.service = service;
		this.filename = filename;
		this.path = path;
		this.adds = service.getAdds();
		this.deletes = service.getDeletes();
		this.updates = service.getUpdates();
		this.finds = service.getFinds();
		this.addnum = 0;
		this.deletenum = 0;
		this.updatenum = 0;
		this.findnum = 0;
		this.filenum = 0;
	}
	/**
	 * 
	 * 输入模型序列文件
	 * 
	 * @param filename

	 * @throws IOException
	 */
	
	public void generatingInterfaceSequenceFiles() throws IOException {
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
			this.line = line;
			cartesian();	
		}
		in.close();
	}
	private void cartesian() {
		if(adds != null){
			addnum = adds.size();
		}
		if(deletes != null){
			deletenum = deletes.size();
		}if(updates != null){
			updatenum = updates.size();
		}if(finds != null){
			findnum = finds.size();
		}
		sequenceLenth = line.length();
		recursiveGenerationInterfaceSequence(0,new ArrayList<Operation>());
		
	}
	/**
	 * 递归生成脚本(接口序列)
	 * @param i
	 * @param arrayList 接口序列
	 */
	private void recursiveGenerationInterfaceSequence(int i,ArrayList<Operation> arrayList){
		if(i<sequenceLenth){
			switch (line.charAt(i)) {
			case 'A':
				for(int j = 0;j<addnum;j++){
					arrayList.add(adds.get(j));
					if(interfaceSequenceConstraint1(arrayList)){
						recursiveGenerationInterfaceSequence(++i,arrayList);
					}
					arrayList.remove(arrayList.size()-1);
				}
				break;
			case 'U':
				for(int j = 0;j<updatenum;j++){
					arrayList.add(updates.get(j));
					if(interfaceSequenceConstraint1(arrayList)){
						recursiveGenerationInterfaceSequence(++i,arrayList);
					}
					arrayList.remove(arrayList.size()-1);
				}
				break;
			case 'D':
				for(int j = 0;j<deletenum;j++){
					arrayList.add(deletes.get(j));
					if(interfaceSequenceConstraint1(arrayList)){
						recursiveGenerationInterfaceSequence(++i,arrayList);
					}
					arrayList.remove(arrayList.size()-1);
				}
				break;
			case 'F':
				for(int j = 0;j<findnum;j++){
					arrayList.add(finds.get(j));
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
	private boolean interfaceSequenceConstraint1(ArrayList<Operation> arrayList) {
		Operation last = arrayList.get(arrayList.size()-1);
		int tempnum = 0;
		for(Operation operation :arrayList){
			if(last.equals(operation)){
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
	private boolean interfaceSequenceConstraint2(ArrayList<Operation> arrayList) {
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
	private void generatingInterfaceSequenceFiles(ArrayList<Operation> output) throws FileNotFoundException {
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
		out.println("<script resourcesID=\""+service.getId()+"\""+" sequence=\"\" completed=\"false\" >");
		out.flush();
		for(Operation operation :output){
			printlnOperation(operation,out);
		}
		out.println("</script>");
		out.flush();
		out.close();
	}
	private void printlnDependency(ArrayList<Operation> operations, PrintWriter out) {
		for(Operation operation : operations){
			printlnOperation(operation,out);
		}
	}
	private void printlnOperation(Operation operation,PrintWriter out){
		String operationtype = operation.getType();
		String name = operation.getName();
		String path = operation.getPath();
		ArrayList<Operation> dependencys = operation.getDependency();
		if(dependencys != null&&dependencys.size() != 0){
			printlnDependency(dependencys,out);
		}
		out.println("	<step operation=\""+operationtype+"\" path=\""+path+"\" name=\""+name+"\" >");
		out.flush();
		ArrayList<ResponseParam> responseParams = operation.getResponseParams();
		ArrayList<RequestParam> requestParams = operation.getRequestParams();
		if(requestParams != null&&requestParams.size() != 0){
			for(RequestParam requestParam : requestParams){
				out.println("		<param name=\""+requestParam.getName()+"\" attribute=\""+requestParam.getAttribute()+"\" location=\""+requestParam.getLocation()+"\">");
				out.flush();
				ArrayList<RequestElement> requestElements = requestParam.getElements();
				if(requestElements != null&&requestElements.size() != 0){
					for(RequestElement requestElement:requestElements){
						printlnElement(requestElement,out);
						break;
					}
				}
				out.println("		</param>");
				out.flush();
			}
		}
		if(responseParams != null){
			out.println("	<response type=\""+operation.getResponse()+"\" >");
			out.flush();
			if(responseParams.size() != 0){
				for(ResponseParam responseParam : responseParams){
					out.println("		<param name=\""+responseParam.getName()+"\" attribute=\""+responseParam.getAttribute()+"\" />");
					out.flush();
				}
			}
			out.println("	</response>");
			out.flush();
		}
		out.println("	</step>");
		out.flush();
	}
	private void printlnElement(RequestElement requestElement, PrintWriter out) {
		out.println("			<element"+requestElement.getLevel()+" name=\""+requestElement.getName()+"\" attribute=\""+requestElement.getAttribute()+"\" >");
		out.flush();
		ArrayList<RequestElement> requestElements = requestElement.getElements();
		if(requestElements != null&&requestElements.size() != 0){
			for(RequestElement re:requestElements){
				printlnElement(re,out);
				break;
			}
		}
		out.println("			</element"+requestElement.getLevel()+">");
		out.flush();
		
	}
	
}
