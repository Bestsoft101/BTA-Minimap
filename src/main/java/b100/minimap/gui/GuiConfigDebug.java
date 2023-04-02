package b100.minimap.gui;

import b100.minimap.Minimap;

public class GuiConfigDebug extends GuiScreen {
	
	public GuiConfigDebug(GuiScreen parentScreen) {
		super(parentScreen);
	}

	public GuiOptionsContainer guiOptionsContainer;
	
	@Override
	public void onInit() {
		Minimap.log("Init Gui Config");
		
		guiOptionsContainer = add(new GuiOptionsContainer(this));
		guiOptionsContainer.add("Masking", new GuiOptionButtonBoolean(this, minimap.config.mask));
		
		guiOptionsContainer.addNav(new GuiButtonNavigation(this, "Back", guiOptionsContainer).addActionListener((e) -> back()));
	}

	@Override
	public void onResize() {
		guiOptionsContainer.onResize();
	}
	
}
