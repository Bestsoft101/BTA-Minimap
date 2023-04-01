package b100.minimap.config;

public class IntegerOption extends Option<Integer> {

	public Integer minValue = null;
	public Integer maxValue = null;
	
	public IntegerOption(String name, int value) {
		super(name, value);
	}

	@Override
	public Integer parse(String string) {
		return Integer.parseInt(string);
	}
	
	public IntegerOption setMinMax(Integer min, Integer max) {
		this.minValue = min;
		this.maxValue = max;
		return this;
	}

}
