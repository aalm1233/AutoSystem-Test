package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DecimalData extends Data{

	public DecimalData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}
	public DecimalData() {
		super();
	}
	abstract ArrayList<String> dataGeneration(String pattern,double minValue, double maxValue);

}
