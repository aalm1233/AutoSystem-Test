package nwpu.autosysteamtest;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AutomatedTestData {
	DocumentPrepcessing  documentPrepcessing;
	private ConcurrentHashMap<String, String> operaterTypesMap;
	protected ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> updateInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> parameterConstrainsMap;
	protected String resourcesid;
	protected String resourceid;
	protected String path;
	
	public AutomatedTestData(String path) throws InterruptedException{
		this.path = path;
		documentPrepcessing = DocumentPrepcessing.getInstance();
		this.operaterTypesMap = documentPrepcessing.getOperaterTypesMap();
		this.addInterfaceSetMap = documentPrepcessing.getAddInterfaceSetMap();
		this.deleteInterfaceSetMap = documentPrepcessing.getDeleteInterfaceSetMap();
		this.updateInterfaceSetMap = documentPrepcessing.getUpdateInterfaceSetMap();
		this.findInterfaceSetMap = documentPrepcessing.getFindInterfaceSetMap();
		this.run1();
	}
	protected void run1(){
		Set<String> key = operaterTypesMap.keySet();
		File file = null;
		try{
			
			file = new File(path + "date");
			if(!file.exists()){
				file.mkdirs();
			}
		}catch(Exception e){
			
		}finally {
			file = null;
		}
		this.path = path + "date\\";
		for(Iterator<String> it = key.iterator(); it.hasNext();){
			this.resourcesid = (String) it.next();
			try{
				file = new File(path+this.resourcesid);
				if(!file.exists()){
					file.mkdirs();
				}
			}catch(Exception e){
				
			}finally {
				file = null;
			}
			run2();
		}
	}
	protected void run2(){
		addDateAutomated();
		deleteDateAutomated();
		upDateAutomated();
		//findDateAutomated();
	}
	private void findDateAutomated() {

	}
	private void upDateAutomated() {
		ArrayList<String> updateInterfaceSets = this.updateInterfaceSetMap.get(this.resourcesid);
		for(String updateInterface:updateInterfaceSets){
			this.resourceid = updateInterface.split("\\|")[0].split(",")[0];
			File file = null;
			try{
				file = new File(path+this.resourcesid+"\\update\\"+this.resourceid);
				if(!file.exists()){
					file.mkdirs();
				}
			}catch(Exception e){
				
			}finally {
				file = null;
			}
		}
		
	}
	private void deleteDateAutomated() {
		ArrayList<String> deleteInterfaceSets = this.deleteInterfaceSetMap.get(this.resourcesid);
		for(String updateInterface:deleteInterfaceSets){
			this.resourceid = updateInterface.split("\\|")[0].split(",")[0];
			File file = null;
			try{
				file = new File(path+this.resourcesid+"\\delete\\"+this.resourceid);
				if(!file.exists()){
					file.mkdirs();
				}
			}catch(Exception e){
				
			}finally {
				file = null;
			}
		}
		
	}
	private void addDateAutomated() {
		ArrayList<String> addInterfaceSets = this.addInterfaceSetMap.get(this.resourcesid);
		for(String updateInterface:addInterfaceSets){
			this.resourceid = updateInterface.split("\\|")[0].split(",")[0];
			File file = null;
			try{
				file = new File(path+this.resourcesid+"\\add\\"+this.resourceid);
				if(!file.exists()){
					file.mkdirs();
				}
			}catch(Exception e){
				
			}finally {
				file = null;
			}
		}
		
	}
}
