package b100.minimap.gui;

import b100.minimap.config.MapConfig;
import b100.minimap.config.Option;
import b100.minimap.gui.GuiNavigationContainer.Position;

public class GuiConfigMap extends GuiScreen implements OptionListener {

	public GuiOptionsContainer options;
	public GuiNavigationContainer navTop;
	public GuiNavigationContainer navBottom;

	public GuiConfigMap(GuiScreen parentScreen) {
		super(parentScreen);
	}

	@Override
	public void onInit() {
		MapConfig config = minimap.config.mapConfig;

		options = add(new GuiOptionsContainer(this));
		options.add("Style", new GuiOptionButtonMapStyle(this, config.mapStyle).addOptionListener(this));
		options.add("Fullscreen Map", new GuiOptionButtonBoolean(this, config.fullscreenMap));
		options.add("Fullscreen Map Type", new GuiOptionButtonInteger(this, config.fullscreenType));
		options.add("Round Map", new GuiOptionButtonBoolean(this, config.roundMap).addOptionListener(this));
		options.add("Rotate Map", new GuiOptionButtonBoolean(this, config.rotateMap));
		options.add("Rotate Frame", new GuiOptionButtonBoolean(this, config.rotateFrame));
		options.add("Position", new GuiOptionButtonInteger(this, config.position).setScrollingEnabled(false));
		options.add("Size", new GuiOptionButtonInteger(this, config.width));
		options.add("Frame Opacity", new GuiOptionButtonInteger(this, config.frameOpacity));
		options.add("Shade Type", new GuiOptionButtonInteger(this, config.shadeType).setScrollingEnabled(false).addOptionListener(this));
		options.add("Lighting", new GuiOptionButtonBoolean(this, config.lighting).addOptionListener(this));
		options.add("Show All Blocks", new GuiOptionButtonBoolean(this, config.renderAllBlocks).addOptionListener(this));
		options.add("Show Waypoints", new GuiOptionButtonBoolean(this, config.showWaypoints));
		
		navBottom = add(new GuiNavigationContainer(this, options, Position.BOTTOM));
		navBottom.add(new GuiButtonNavigation(this, "Back", options).addActionListener((e) -> back()));
		
		navTop = add(new GuiNavigationContainer(this, options, Position.TOP));
		navTop.add(new GuiButtonNavigation(this, "Map Configuration", options));
	}

	@Override
	public void onOptionValueChanged(Option<?> option) {
		MapConfig mapConfig = minimap.config.mapConfig;
		if(option == mapConfig.shadeType || option == mapConfig.lighting || option == mapConfig.renderAllBlocks) {
			minimap.mapRender.updateAllTiles();
		}
		if(option == mapConfig.mapStyle || option == mapConfig.roundMap) {
			minimap.updateStyle();
		}
	}

}
