package nwpu.autosysteamtest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
			Service service= dp.searchServiceById("Group_Service");
			for(Operation operation : service.getAdds()){
				System.out.println(operation.getName());
			}
			Operation operation = service.searchAllOperationById("Group_New");
			ArrayList<RequestElement> elements = null;
			for(RequestParam param: operation.getRequestParams()){
				if("param".equals(param.getName())){
					elements = param.getElements();
					break;
				}
			}
			for(RequestElement element : elements){
				if("parentId".equals(element.getName())){
					System.out.println(element.getLocation());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
