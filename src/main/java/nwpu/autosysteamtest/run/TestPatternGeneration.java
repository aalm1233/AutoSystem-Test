package nwpu.autosysteamtest.run;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 
 * @author Dengtong
 * @version 2.0,24/11/2017
 */
public class TestPatternGeneration {
	private ConcurrentHashMap<String, String> operaterTypesMap;
	private ConcurrentHashMap<String, ArrayList<String>> mode;
	public TestPatternGeneration(ConcurrentHashMap<String, String> operaterTypesMap){
		this.operaterTypesMap = operaterTypesMap;
		mode = new ConcurrentHashMap<>();
	} 
	public void run(){	
		Set<String> key = operaterTypesMap.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();){
			ArrayList<String> guize = new ArrayList<>();
			String resourcesId = (String) it.next();
			String operaterTypes = operaterTypesMap.get(resourcesId);
			switch (operaterTypes) {
			case "<add><delete><update><find>":
				guize.add("A(A|U|D|F)*F");
				break;
			case "<add><delete><find>":
				guize.add("A(A|D|F)*F");
				break;
			case "<add><update><find>":
				guize.add("A(A|U|F)*F");
				break;
			case "<add><find>":
				guize.add("A(A|F)*F");
				break;
			default:
				break;
			}
			mode.put(resourcesId, guize);
		}
	}
	public ConcurrentHashMap<String, ArrayList<String>> getMode() {
		return mode;
	}

}
