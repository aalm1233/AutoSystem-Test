package nwpu.autosysteamtest;

import java.io.File;
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
			TestPatternGeneration tpg = new TestPatternGeneration(dp.getOperaterTypesMap());
			tpg.run();
			ScriptGeneration sg = new ScriptGeneration(path+"\\", tpg.getMode());
			sg.run();
			AutomatedTestData atd = new AutomatedTestData(path+"\\");
			atd.run1();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
