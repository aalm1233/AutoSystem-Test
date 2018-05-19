package nwpu.autosysteamtest.data;


import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 
 * @author Dengtong
 * @version 1.0,05/01/2018
 */
public abstract class AbstractDateData extends BaseData{

	public AbstractDateData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}
	public AbstractDateData() {
		super();
	}
	/**
	 * 生成数据
	 * @param lowtime 最低时间
	 * @param hightime 最长时间
	 * @return 生成的数据
	 */
	abstract ArrayList<String> dataGeneration(Date lowtime, Date hightime);
}
