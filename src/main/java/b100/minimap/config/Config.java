package b100.minimap.config;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class Config extends ConfigBase {
	
	public MapConfig mapConfig = new MapConfig();

	public BooleanOption mapVisible = new BooleanOption("mapVisible", true);
	public IntegerOption updateSpeed = new IntegerOption("updateSpeed", 1).setMinMax(1, 16);
	public IntegerOption mapStyle = new IntegerOption("mapStyle", 0).setMinMax(0, 4);
	
	public BooleanOption mask = new BooleanOption("mask", true);

	public Keybind keyMap = new Keybind("keyMap", Keyboard.KEY_M);
	public Keybind keyHideMap = new Keybind("keyHideMap", Keyboard.KEY_N);
	public Keybind keyFullscreen = new Keybind("keyFullscreen", Keyboard.KEY_X);
	public Keybind keyZoomIn = new Keybind("keyZoomIn", Keyboard.KEY_ADD);
	public Keybind keyZoomOut = new Keybind("keyZoomOut", Keyboard.KEY_SUBTRACT);
	
	public Keybind[] keyBinds = new Keybind[] { keyMap, keyHideMap, keyFullscreen, keyZoomIn, keyZoomOut };
	
	@Override
	public List<Option<?>> getAllOptions() {
		List<Option<?>> options = new ArrayList<>();

		options.add(mapVisible);
		options.add(updateSpeed);
		options.add(mapStyle);
		
		options.add(mask);
		
		for(int i=0; i < keyBinds.length; i++) {
			options.add(keyBinds[i]);
		}
		
		return options;
	}
	
}
