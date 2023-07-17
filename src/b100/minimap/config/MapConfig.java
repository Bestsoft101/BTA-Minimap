package b100.minimap.config;

import java.util.ArrayList;
import java.util.List;

public class MapConfig extends ConfigBase {

	public IntegerOption mapStyle = new IntegerOption("mapStyle", 0).setMinMax(0, 6);
	public IntegerOption width = new IntegerOption("width", 16).setMinMax(1, 100);
	public IntegerOption position = new IntegerOption("position", 1).setMinMax(0, 5);
	public IntegerOption shadeType = new IntegerOption("shadeType", 1).setMinMax(0, 2);
	public BooleanOption lighting = new BooleanOption("lighting", true);
	public IntegerOption zoomLevel = new IntegerOption("zoomLevel", 0);
	public BooleanOption fullscreenMap = new BooleanOption("fullscreenMap", false);
	public IntegerOption fullscreenType = new IntegerOption("fullscreenType", 0).setMinMax(0, 1);
	public IntegerOption fullscreenZoomLevel = new IntegerOption("fullscreenZoomLevel", 0);
	public BooleanOption rotateMap = new BooleanOption("rotateMap", false);
	public BooleanOption roundMap = new BooleanOption("roundMap", false);
	public IntegerOption frameOpacity = new IntegerOption("frameOpacity", 100).setMinMax(0, 100);
	public BooleanOption renderAllBlocks = new BooleanOption("showAllBlocks", false);
	public BooleanOption showWaypoints = new BooleanOption("showWaypoints", true);
	
	@Override
	public List<Option<?>> getAllOptions() {
		List<Option<?>> options = new ArrayList<>();

		options.add(mapStyle);
		options.add(width);
		options.add(position);
		options.add(shadeType);
		options.add(lighting);
		options.add(zoomLevel);
		options.add(fullscreenMap);
		options.add(fullscreenZoomLevel);
		options.add(rotateMap);
		options.add(roundMap);
		options.add(frameOpacity);
		options.add(renderAllBlocks);
		options.add(showWaypoints);
		options.add(fullscreenType);
		
		return options;
	}

}
