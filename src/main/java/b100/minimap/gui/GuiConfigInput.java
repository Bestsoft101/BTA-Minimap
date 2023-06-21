package b100.minimap.gui;

import b100.minimap.gui.GuiNavigationContainer.Position;

public class GuiConfigInput extends GuiScreen {

	public GuiConfigInput(GuiScreen parentScreen) {
		super(parentScreen);
	}

	public GuiOptionsContainer options;
	public GuiNavigationContainer nav;

	@Override
	public void onInit() {
		options = add(new GuiOptionsContainer(this));
		options.add("Menu", new GuiOptionsButtonKeybind(this, minimap.config.keyMap));
		options.add("Show / Hide Map", new GuiOptionsButtonKeybind(this, minimap.config.keyHideMap));
		options.add("Fullscreen Map", new GuiOptionsButtonKeybind(this, minimap.config.keyFullscreen));
		options.add("Zoom In", new GuiOptionsButtonKeybind(this, minimap.config.keyZoomIn));
		options.add("Zoom Out", new GuiOptionsButtonKeybind(this, minimap.config.keyZoomOut));

		nav = add(new GuiNavigationContainer(this, options, Position.BOTTOM));
		nav.add(new GuiButtonNavigation(this, "Back", options).addActionListener((e) -> back()));
	}

	@Override
	public void onResize() {
		options.onResize();
		nav.onResize();
	}

}
