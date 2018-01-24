package nwpu.autosysteamtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.enity.Service;
/**
 * 
 * @author Dengtong
 * @version 3.1,05/01/2018
 */
public class ScriptGeneration {
	DocumentPrepcessing  documentPrepcessing;
	private ConcurrentHashMap<String, ArrayList<String>> mode;
	private String path;
	int filenum;
	String resourcesId;
	public ScriptGeneration(String path,
			ConcurrentHashMap<String, ArrayList<String>> mode) throws InterruptedException {
		documentPrepcessing = DocumentPrepcessing.getInstance();
		this.path = path;
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
			Service service = documentPrepcessing.searchServiceById(resourcesId);
			String fileName = resourcesId;
			filenum = 1;
			generatingTestModelFiles(fileName);//生成操作模型
			GeneratingOperationSequence operationSequence = new GeneratingOperationSequence(service, fileName, path);
			operationSequence.generatingOperationSequenceFiles();
			//GeneratingInterfaceSequence generatingInterfaceSequence = new GeneratingInterfaceSequence(service, fileName, path);
			//generatingInterfaceSequence.generatingInterfaceSequenceFiles();
		}
	}

}
