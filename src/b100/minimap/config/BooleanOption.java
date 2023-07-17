package b100.minimap.config;

public class BooleanOption extends Option<Boolean> {

	public BooleanOption(String name, boolean value) {
		super(name, value);
	}

	@Override
	public Boolean parse(String string) {
		return "true".equalsIgnoreCase(string);
	}
	
	public void toggle() {
		value = !value;
	}
}
