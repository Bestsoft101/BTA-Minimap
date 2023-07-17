package b100.minimap.gui;

import b100.minimap.gui.GuiNavigationContainer.Position;

public class GuiConfigDebug extends GuiScreen {

	public GuiOptionsContainer options;
	public GuiNavigationContainer navTop;
	public GuiNavigationContainer navBottom;
	
	public GuiConfigDebug(GuiScreen parentScreen) {
		super(parentScreen);
	}
	
	@Override
	public void onInit() {
		options = add(new GuiOptionsContainer(this));
		options.add("Masking", new GuiOptionButtonBoolean(this, minimap.config.mask));
		options.add("Show Tiles", new GuiOptionButtonBoolean(this, minimap.config.showTiles));

		navBottom = add(new GuiNavigationContainer(this, options, Position.BOTTOM));
		navBottom.add(new GuiButtonNavigation(this, "Back", options).addActionListener((e) -> back()));
		
		navTop = add(new GuiNavigationContainer(this, options, Position.TOP));
		navTop.add(new GuiButtonNavigation(this, "Debug", options));
	}
	
}
