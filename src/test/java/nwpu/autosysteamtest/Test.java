package nwpu.autosysteamtest;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import nwpu.autosysteamtest.enity.Operation;
import nwpu.autosysteamtest.enity.RequestElement;
import nwpu.autosysteamtest.enity.RequestParam;
import nwpu.autosysteamtest.enity.Service;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
		switch (args.length) {
		case 0:
			System.out.println("Please start in the following format >> Java App xmldirpath");
			break;
		case 1:
			String path = args[0];
			new Test().runThread(path);
			break;
		default:
			break;
		}
	}
	private void runThread(String path) {
		File folder = new File(path+"\\resource\\inputxml");
		File[] fileSet = folder.listFiles();
		 DocumentPrepcessing dp;
		try {
			dp = DocumentPrepcessing.getInstance(fileSet);
			while(Thread.activeCount()!=1){}
			Set<String> key =dp.getOperaterTypesMap().keySet();
			for (Iterator<String> it = key.iterator(); it.hasNext();){
				String resourcesId = (String) it.next();
				Service service= dp.searchServiceById(resourcesId);
				for(Operation operation: service.getAdds()){
					String resourceId = operation.getId();
					for(RequestParam param:operation.getRequestParams()){
						System.out.println(param.getName()+":");
						for(RequestElement element:param.getElements()){
							System.out.println("  "+element.getName());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
