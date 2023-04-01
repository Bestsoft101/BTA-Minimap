package b100.minimap.config;

import java.util.ArrayList;
import java.util.List;

public class MapConfig extends ConfigBase {
	
	public IntegerOption width = new IntegerOption("width", 32).setMinMax(1, 100);
	public IntegerOption position = new IntegerOption("position", 1).setMinMax(0, 3);
	public IntegerOption shadeType = new IntegerOption("shadeType", 1).setMinMax(0, 1);
	public BooleanOption lighting = new BooleanOption("lighting", true);
	public IntegerOption zoomLevel = new IntegerOption("zoomLevel", 0);
	public BooleanOption fullscreenMap = new BooleanOption("fullscreenMap", false);
	public IntegerOption fullscreenZoomLevel = new IntegerOption("zoomLevel", 0);
	public BooleanOption rotateMap = new BooleanOption("rotateMap", false);
	
	@Override
	public List<Option<?>> getAllOptions() {
		List<Option<?>> options = new ArrayList<>();

		options.add(width);
		options.add(position);
		options.add(shadeType);
		options.add(lighting);
		options.add(zoomLevel);
		options.add(fullscreenMap);
		options.add(fullscreenZoomLevel);
		options.add(rotateMap);
		
		return options;
	}

}
