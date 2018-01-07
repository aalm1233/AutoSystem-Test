package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class NumericalData extends Data{

	public NumericalData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}
	public NumericalData() {
		super();
	}
	abstract ArrayList<String> dataGeneration(String pattern,long minValue, long maxValue);
}
