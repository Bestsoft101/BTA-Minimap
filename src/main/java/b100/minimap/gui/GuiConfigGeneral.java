package b100.minimap.gui;

import b100.minimap.Minimap;
import b100.minimap.config.Config;
import b100.minimap.config.MapConfig;
import b100.minimap.config.Option;

public class GuiConfigGeneral extends GuiScreen implements OptionListener {
	
	public GuiConfigGeneral(GuiScreen parentScreen) {
		super(parentScreen);
	}

	public GuiOptionsContainer guiOptionsContainer;
	
	@Override
	public void onInit() {
		Minimap.log("Init Gui Config");
		
		Config config = minimap.config;
		
		guiOptionsContainer = add(new GuiOptionsContainer(this));
		guiOptionsContainer.add("Map Visible", new GuiOptionButtonBoolean(this, config.mapVisible));
		guiOptionsContainer.add("Fullscreen Map", new GuiOptionButtonBoolean(this, config.mapConfig.fullscreenMap));
		guiOptionsContainer.add("Style", new GuiOptionButtonMapStyle(this, config.mapStyle).addOptionListener(this));
		guiOptionsContainer.add("Position", new GuiOptionButtonInteger(this, config.mapConfig.position).setScrollingEnabled(false));
		guiOptionsContainer.add("Size", new GuiOptionButtonInteger(this, config.mapConfig.width));
		guiOptionsContainer.add("Shade Type", new GuiOptionButtonInteger(this, config.mapConfig.shadeType).setScrollingEnabled(false).addOptionListener(this));
		guiOptionsContainer.add("Lighting", new GuiOptionButtonBoolean(this, config.mapConfig.lighting).addOptionListener(this));
		guiOptionsContainer.add("Update Speed", new GuiOptionButtonInteger(this, config.updateSpeed));
		guiOptionsContainer.add("Rotate Map", new GuiOptionButtonBoolean(this, config.mapConfig.rotateMap));
		guiOptionsContainer.add("Frame Opacity", new GuiOptionButtonInteger(this, config.mapConfig.frameOpacity));
		guiOptionsContainer.add("Round Map", new GuiOptionButtonBoolean(this, config.mapConfig.roundMap).addOptionListener(this));
		guiOptionsContainer.add("Debug", new GuiButton(this, "->").addActionListener((e) -> utils.displayGui(new GuiConfigDebug(this))));
		
		guiOptionsContainer.addNav(new GuiButtonNavigation(this, "Close", guiOptionsContainer).addActionListener((e) -> back()));
		guiOptionsContainer.addNav(new GuiButtonNavigation(this, "Keybinds", guiOptionsContainer).addActionListener((e) -> utils.displayGui(new GuiConfigInput(this))));
	}

	@Override
	public void onResize() {
		guiOptionsContainer.onResize();
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
