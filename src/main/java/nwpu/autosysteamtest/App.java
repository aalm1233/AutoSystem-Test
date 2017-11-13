package nwpu.autosysteamtest;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	switch (args.length) {
		case 0:
			System.out.println("Please start in the following format >> Java App xmlpath");
			break;
		case 1:
			String path = args[0];
			File folder = new File(path+"\\resource\\inputxml");
			File[] fileSet = folder.listFiles();
			 DocumentPrepcessing dp = new DocumentPrepcessing(fileSet);
			 dp.run();
			 TestPatternGeneration tpg = new TestPatternGeneration(dp.getOperaterTypesMap());
			 tpg.run();
			 ScriptGeneration sg = new ScriptGeneration(path+"\\resource\\",dp.getAddInterfaceSetMap(), dp.getDeleteInterfaceSetMap(), dp.getUpdateInterfaceSetMap(), dp.getFindInterfaceSetMap(), tpg.getMode());
			 sg.run();
			 break;
		default:
			break;
		}
    }
}
