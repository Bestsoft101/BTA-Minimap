package b100.minimap.gui;

import b100.minimap.config.Config;
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
		Config config = minimap.config;

		options = add(new GuiOptionsContainer(this));
		options.add("Fullscreen Map", new GuiOptionButtonBoolean(this, config.mapConfig.fullscreenMap));
		options.add("Round Map", new GuiOptionButtonBoolean(this, config.mapConfig.roundMap).addOptionListener(this));
		options.add("Rotate Map", new GuiOptionButtonBoolean(this, config.mapConfig.rotateMap));
		options.add("Position", new GuiOptionButtonInteger(this, config.mapConfig.position).setScrollingEnabled(false));
		options.add("Size", new GuiOptionButtonInteger(this, config.mapConfig.width));
		options.add("Frame Opacity", new GuiOptionButtonInteger(this, config.mapConfig.frameOpacity));
		options.add("Shade Type", new GuiOptionButtonInteger(this, config.mapConfig.shadeType).setScrollingEnabled(false).addOptionListener(this));
		options.add("Lighting", new GuiOptionButtonBoolean(this, config.mapConfig.lighting).addOptionListener(this));
		options.add("Show All Blocks", new GuiOptionButtonBoolean(this, config.mapConfig.renderAllBlocks).addOptionListener(this));
		options.add("Show Waypoints", new GuiOptionButtonBoolean(this, config.mapConfig.showWaypoints));
		
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
		if(option == mapConfig.roundMap) {
			minimap.updateStyle();
		}
	}

}
