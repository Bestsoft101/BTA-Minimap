package b100.minimap.config;

public class Keybind extends IntegerOption {
	
	public boolean press = false;
	
	public Keybind(String name, int keyCode) {
		super(name, keyCode);
	}
	
}
