package b100.minimap.gui;

import b100.minimap.Minimap;
import b100.minimap.config.Config;
import b100.minimap.config.MapConfig;
import b100.minimap.config.Option;
import b100.minimap.gui.GuiNavigationContainer.Position;

public class GuiConfigGeneral extends GuiScreen implements OptionListener {
	
	public GuiConfigGeneral(GuiScreen parentScreen) {
		super(parentScreen);
	}

	public GuiOptionsContainer options;
	public GuiNavigationContainer nav;
	
	@Override
	public void onInit() {
		Minimap.log("Init Gui Config");
		
		Config config = minimap.config;
		
		options = add(new GuiOptionsContainer(this));
		options.add("Map Visible", new GuiOptionButtonBoolean(this, config.mapVisible));
		options.add("Fullscreen Map", new GuiOptionButtonBoolean(this, config.mapConfig.fullscreenMap));
		options.add("Style", new GuiOptionButtonMapStyle(this, config.mapStyle).addOptionListener(this));
		options.add("Round Map", new GuiOptionButtonBoolean(this, config.mapConfig.roundMap).addOptionListener(this));
		options.add("Position", new GuiOptionButtonInteger(this, config.mapConfig.position).setScrollingEnabled(false));
		options.add("Size", new GuiOptionButtonInteger(this, config.mapConfig.width));
		options.add("Shade Type", new GuiOptionButtonInteger(this, config.mapConfig.shadeType).setScrollingEnabled(false).addOptionListener(this));
		options.add("Lighting", new GuiOptionButtonBoolean(this, config.mapConfig.lighting).addOptionListener(this));
		options.add("Update Speed", new GuiOptionButtonInteger(this, config.updateSpeed));
		options.add("Rotate Map", new GuiOptionButtonBoolean(this, config.mapConfig.rotateMap));
		options.add("Frame Opacity", new GuiOptionButtonInteger(this, config.mapConfig.frameOpacity));
		options.add("Require Item", new GuiOptionButtonRequireItem(this, config.requireItem));
		options.add("Debug", new GuiButton(this, "->").addActionListener((e) -> utils.displayGui(new GuiConfigDebug(this))));
		
		nav = add(new GuiNavigationContainer(this, options, Position.BOTTOM));
		nav.add(new GuiButtonNavigation(this, "Close", options).addActionListener((e) -> back()));
		nav.add(new GuiButtonNavigation(this, "Waypoints", options).addActionListener((e) -> utils.displayGui(new GuiWaypoints(this))));
		nav.add(new GuiButtonNavigation(this, "Keybinds", options).addActionListener((e) -> utils.displayGui(new GuiConfigInput(this))));
	}

	@Override
	public void onResize() {
		options.onResize();
		nav.onResize();
	}

	@Override
	public void onOptionValueChanged(Option<?> option) {
		Config config = minimap.config;
		MapConfig mapConfig = config.mapConfig;
		if(option == mapConfig.shadeType || option == mapConfig.lighting) {
			minimap.mapRender.updateAllTiles();
		}
		if(option == mapConfig.roundMap || option == config.mapStyle) {
			minimap.updateStyle();
		}
	}
	
}
