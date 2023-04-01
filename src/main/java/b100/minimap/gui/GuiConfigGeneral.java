package b100.minimap.gui;

import b100.minimap.Minimap;
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
		
		guiOptionsContainer = add(new GuiOptionsContainer(this));
		guiOptionsContainer.add("Map Visible", new GuiOptionButtonBoolean(this, minimap.config.mapVisible));
		guiOptionsContainer.add("Fullscreen Map", new GuiOptionButtonBoolean(this, minimap.config.mapConfig.fullscreenMap));
		guiOptionsContainer.add("Position", new GuiOptionButtonInteger(this, minimap.config.mapConfig.position));
		guiOptionsContainer.add("Size", new GuiOptionButtonScrollable(this, minimap.config.mapConfig.width));
		guiOptionsContainer.add("Shade Type", new GuiOptionButtonInteger(this, minimap.config.mapConfig.shadeType).addOptionListener(this));
		guiOptionsContainer.add("Lighting", new GuiOptionButtonBoolean(this, minimap.config.mapConfig.lighting).addOptionListener(this));
		
		guiOptionsContainer.addNav(new GuiButtonNavigation(this, "Close", guiOptionsContainer).addActionListener((e) -> back()));
		guiOptionsContainer.addNav(new GuiButtonNavigation(this, "Keybinds", guiOptionsContainer).addActionListener((e) -> utils.displayGui(new GuiConfigInput(this))));
	}

	@Override
	public void onResize() {
		guiOptionsContainer.onResize();
	}

	@Override
	public void onOptionValueChanged(Option<?> option) {
		MapConfig config = minimap.config.mapConfig;
		if(option == config.shadeType || option == config.lighting) {
			minimap.mapRender.updateAllTiles();
		}
	}
	
}
