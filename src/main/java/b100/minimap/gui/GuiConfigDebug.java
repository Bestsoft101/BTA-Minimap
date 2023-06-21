package b100.minimap.gui;

import b100.minimap.Minimap;
import b100.minimap.gui.GuiNavigationContainer.Position;

public class GuiConfigDebug extends GuiScreen {
	
	public GuiConfigDebug(GuiScreen parentScreen) {
		super(parentScreen);
	}

	public GuiOptionsContainer options;
	public GuiNavigationContainer nav;
	
	@Override
	public void onInit() {
		Minimap.log("Init Gui Config");
		
		options = add(new GuiOptionsContainer(this));
		options.add("Masking", new GuiOptionButtonBoolean(this, minimap.config.mask));

		nav = add(new GuiNavigationContainer(this, options, Position.BOTTOM));
		nav.add(new GuiButtonNavigation(this, "Back", options).addActionListener((e) -> back()));
	}

	@Override
	public void onResize() {
		options.onResize();
		nav.onResize();
	}
	
}
