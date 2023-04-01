package b100.minimap.config;

import java.util.ArrayList;
import java.util.List;

public class MapConfig extends ConfigBase {
	
	public IntegerOption width = new IntegerOption("width", 32);
	public RangedIntegerOption position = new RangedIntegerOption("position", 1, new int[] {0, 1, 2, 3});
	public RangedIntegerOption shadeType = new RangedIntegerOption("shadeType", 1, new int[] {0, 1});
	public BooleanOption lighting = new BooleanOption("lighting", true);
	public IntegerOption zoomLevel = new IntegerOption("zoomLevel", 0);
	public BooleanOption fullscreenMap = new BooleanOption("fullscreenMap", false);
	public IntegerOption fullscreenZoomLevel = new IntegerOption("zoomLevel", 0);
	
	{
		width.minValue = 1;
		width.maxValue = 100;
	}
	
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
		
		return options;
	}

}
