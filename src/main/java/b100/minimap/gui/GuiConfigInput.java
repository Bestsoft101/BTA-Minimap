package b100.minimap.gui;

public class GuiConfigInput extends GuiScreen {

	public GuiConfigInput(GuiScreen parentScreen) {
		super(parentScreen);
	}

	public GuiOptionsContainer guiOptionsContainer;

	@Override
	public void onInit() {
		guiOptionsContainer = add(new GuiOptionsContainer(this));
		guiOptionsContainer.add("Menu", new GuiOptionsButtonKeybind(this, minimap.config.keyMap));
		guiOptionsContainer.add("Fullscreen Map", new GuiOptionsButtonKeybind(this, minimap.config.keyFullscreen));
		guiOptionsContainer.add("Zoom In", new GuiOptionsButtonKeybind(this, minimap.config.keyZoomIn));
		guiOptionsContainer.add("Zoom Out", new GuiOptionsButtonKeybind(this, minimap.config.keyZoomOut));
		
		guiOptionsContainer.addNav(new GuiButtonNavigation(this, "Back", guiOptionsContainer).addActionListener((e) -> back()));
	}

	@Override
	public void onResize() {
		guiOptionsContainer.onResize();
	}

}
