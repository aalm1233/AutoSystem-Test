package nwpu.autosysteamtest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import nwpu.autosysteamtest.entity.Operation;
import nwpu.autosysteamtest.entity.RequestElement;
import nwpu.autosysteamtest.entity.RequestParam;
import nwpu.autosysteamtest.entity.Service;
import nwpu.autosysteamtest.run.DocumentPrepcessing;

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
		File folder = new File(path);
		File[] fileSet = folder.listFiles();
		 DocumentPrepcessing dp;
		try {
			dp = DocumentPrepcessing.getInstance(fileSet);
			while(Thread.activeCount()!=1){}
			Service service= dp.searchServiceById("ISellerPersonInChargeManageService");
			for(Operation operation : service.getAdds()){
				System.out.println(operation.getName());
			}
			Operation operation = service.searchAllOperationById("addPersonincharge");
			ArrayList<RequestElement> elements = null;
			System.out.println(operation);
			System.out.println(operation.getDependency());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
