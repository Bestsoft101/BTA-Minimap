package b100.minimap.config;

public class RangedIntegerOption extends IntegerOption {
	
	public int[] values;
	
	public RangedIntegerOption(String name, int defaultValue, int[] values) {
		super(name, defaultValue);
		this.values = values;
	}
	
	public RangedIntegerOption(String name, int[] values) {
		this(name, values[0], values);
	}

}
