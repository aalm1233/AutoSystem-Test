package nwpu.autosysteamtest;

import java.io.File;
import java.util.Date;
/**
 * 专门启动该系列算法的类
 * @author Dengtong
 * @version 1.0,23/11/2017
 */
public class RunThread {

	public RunThread(String path) {
		File folder = new File(path);
		File[] fileSet = folder.listFiles();
		DocumentPrepcessing dp;
		try {
			dp = DocumentPrepcessing.getInstance(fileSet);
			while(Thread.activeCount()!=1){}
			System.out.println("DocumentPrepcessing Generation at "+new Date());
			TestPatternGeneration tpg = new TestPatternGeneration(dp.getOperaterTypesMap());
			tpg.run();
			ScriptGeneration sg = new ScriptGeneration(path+"\\", tpg.getMode());
			new Thread(sg).start();
			AutomatedTestData atd = new AutomatedTestData(path+"\\");
			new Thread(atd).start();
			while(Thread.activeCount()!=1){}
			System.out.println("Finich Generation at "+new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
