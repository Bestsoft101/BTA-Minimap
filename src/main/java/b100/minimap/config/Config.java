package b100.minimap.config;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class Config extends ConfigBase {
	
	public MapConfig mapConfig = new MapConfig();

	public BooleanOption mapVisible = new BooleanOption("mapVisible", true);
	public IntegerOption updateSpeed = new IntegerOption("updateSpeed", 1).setMinMax(1, 16);
	public IntegerOption requireItem = new IntegerOption("requireItem", 0).setMinMax(0, 1);

	public BooleanOption mask = new BooleanOption("mask", true);
	public BooleanOption showTiles = new BooleanOption("showTiles", false);

	public Keybind keyMap = new Keybind("keyMap", Keyboard.KEY_M).setCanBeUnbound(false);
	public Keybind keyHideMap = new Keybind("keyHideMap", Keyboard.KEY_NONE);
	public Keybind keyFullscreen = new Keybind("keyFullscreen", Keyboard.KEY_X);
	public Keybind keyZoomIn = new Keybind("keyZoomIn", Keyboard.KEY_ADD);
	public Keybind keyZoomOut = new Keybind("keyZoomOut", Keyboard.KEY_SUBTRACT);
	public Keybind keyWaypointList = new Keybind("keyWaypointList", Keyboard.KEY_NONE);
	public Keybind keyWaypointToggle = new Keybind("keyWaypointToggle", Keyboard.KEY_NONE);
	public Keybind keyWaypointCreate = new Keybind("keyWaypointCreate", Keyboard.KEY_NONE);
	
	public Keybind[] keyBinds = new Keybind[] { keyMap, keyHideMap, keyFullscreen, keyZoomIn, keyZoomOut, keyWaypointList, keyWaypointToggle, keyWaypointCreate };
	
	@Override
	public List<Option<?>> getAllOptions() {
		List<Option<?>> options = new ArrayList<>();

		options.add(mapVisible);
		options.add(updateSpeed);
		options.add(requireItem);

		options.add(mask);
		options.add(showTiles);
		
		for(int i=0; i < keyBinds.length; i++) {
			options.add(keyBinds[i]);
		}
		
		return options;
	}
	
}
