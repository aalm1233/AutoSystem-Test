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
		xAutomated("add",this.addInterfaceSetMap);
		xAutomated("update",this.updateInterfaceSetMap);
		xAutomated("delete",this.deleteInterfaceSetMap);
		//findDateAutomated();
	}
	private void findDateAutomated() {

	}
	private void xAutomated(String operation, ConcurrentHashMap<String, ArrayList<String>> xInterfaceSetMap) {
		ArrayList<String> xInterfaceSets = xInterfaceSetMap.get(this.resourcesid);
		ArrayList<String> parameterConstrains = this.parameterConstrainsMap.get(this.resourcesid);
		for(String xInterface:xInterfaceSets){
			this.resourceid = xInterface.split("\\|")[0].split(",")[0];
			File file = null;
			try{
				file = new File(path+this.resourcesid+"\\"+operation+"\\"+this.resourceid);
				if(!file.exists()){
					file.mkdirs();
				}
			}catch(Exception e){
				
			}finally {
				file = null;
			}
			ArrayList<String> resourceParameterConstrains = new ArrayList<>();
			for(String parameterConstrain:parameterConstrains){
				if(parameterConstrain.startsWith(resourceid)){
					resourceParameterConstrains.add(parameterConstrain);
				}
			}
		}
		
	}

}
