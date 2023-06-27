package b100.minimap.config;

public class Keybind extends IntegerOption {
	
	public boolean press = false;
	public boolean canBeUnbound = true;
	
	public Keybind(String name, int keyCode) {
		super(name, keyCode);
	}
	
	public Keybind setCanBeUnbound(boolean b) {
		this.canBeUnbound = b;
		return this;
	}
	
}
