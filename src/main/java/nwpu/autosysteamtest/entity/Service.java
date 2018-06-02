package nwpu.autosysteamtest.entity;

import java.util.ArrayList;
/**
 * 
 * @author Dengtong
 * @version 1.0,28/01/2018
 */
public class Service {
	private String name;
	private String id;
	private String base;
	private ArrayList<Operation> finds;
	private ArrayList<Operation> adds;
	private ArrayList<Operation> deletes;
	private ArrayList<Operation> updates;

	public Service(String name, String id, String base) {
		this.name = name;
		this.id = id;
		this.base = base;
		finds = null;
		adds = null;
		deletes = null;
		updates = null;
	}

	public Operation searchAllOperationById(String id) {
		Operation reslut = null;
		if(adds != null&&(reslut = searchOperationById(adds,id)) != null){
			return reslut;
		}
		if(deletes != null&&(reslut = searchOperationById(deletes,id)) != null){
			return reslut;
		}
		if(updates != null&&(reslut = searchOperationById(updates,id)) != null){
			return reslut;
		}
		if(finds != null&&(reslut = searchOperationById(finds,id)) != null){
			return reslut;
		}
		return reslut;
	}
	private Operation searchOperationById(ArrayList<Operation> operations,String id){
		Operation reslut = null;
		for(Operation o:operations){
			String oid = o.getId();
			if(oid.equals(id)){
				reslut = o;
				break;
			}
		}
		return reslut;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getBase() {
		return base;
	}

	public ArrayList<Operation> getFinds() {
		return finds;
	}

	public void setFinds(ArrayList<Operation> finds) {
		this.finds = finds;
	}

	public ArrayList<Operation> getAdds() {
		return adds;
	}

	public void setAdds(ArrayList<Operation> adds) {
		this.adds = adds;
	}

	public ArrayList<Operation> getDeletes() {
		return deletes;
	}

	public void setDeletes(ArrayList<Operation> deletes) {
		this.deletes = deletes;
	}

	public ArrayList<Operation> getUpdates() {
		return updates;
	}

	public void setUpdates(ArrayList<Operation> updates) {
		this.updates = updates;
	}

}
