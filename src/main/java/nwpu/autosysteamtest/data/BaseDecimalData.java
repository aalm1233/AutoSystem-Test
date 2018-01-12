package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 
 * @author Dengtong
 * @version 1.0,05/01/2018
 */
public abstract class BaseDecimalData extends BaseData{

	public BaseDecimalData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}
	public BaseDecimalData() {
		super();
	}
	/**
	 * 
	 * @param pattern 正则表达式
	 * @param minValue 最小值
	 * @param maxValue 最大值
	 * @return 生成的参数
	 */
	abstract ArrayList<String> dataGeneration(String pattern,double minValue, double maxValue);

}
