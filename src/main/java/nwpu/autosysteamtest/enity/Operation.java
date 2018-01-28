package nwpu.autosysteamtest.enity;

import java.util.ArrayList;

public class Operation {
	private String name;
	private String id;
	private String path;
	private String type;
	private ArrayList<Operation> dependencys;
	private ArrayList<RequestParam> requestParams;
	private String response;
	private ArrayList<ResponseParam> responseParams;
	
	public Operation(String name,String id,String path,String type){
		this.name = name;
		this.id = id;
		this.path = path;
		this.type = type;
		this.requestParams = null;
		this.responseParams = null;
	}
	

	
	@Override
	public String toString() {
		return "Operation [name=" + name + ", id=" + id + ", path=" + path + "]";
	}
	public boolean equals(Operation obj) {
		if(this.id.equals(obj.getId())){
			return true;
		}else{
			return false;
		}	
	}



	public String getType() {
		return type;
	}



	public ArrayList<Operation> getDependency() {
		return dependencys;
	}


	public void addDependency(Operation dependency) {
		if(dependencys == null){
			dependencys = new ArrayList<>();
			dependencys.add(dependency);
		}else{
			dependencys.add(dependency);
		}
	}


	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public void setRequestParams(ArrayList<RequestParam> requestParams) {
		this.requestParams = requestParams;
	}

	public void setResponseParams(ArrayList<ResponseParam> responseParams) {
		this.responseParams = responseParams;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public ArrayList<RequestParam> getRequestParams() {
		return requestParams;
	}

	public ArrayList<ResponseParam> getResponseParams() {
		return responseParams;
	}
	
	
}
