package b100.minimap.gui;

import b100.minimap.gui.GuiNavigationContainer.Position;

public class GuiConfigInput extends GuiScreen {

	public GuiOptionsContainer options;
	public GuiNavigationContainer navTop;
	public GuiNavigationContainer navBottom;

	public GuiConfigInput(GuiScreen parentScreen) {
		super(parentScreen);
	}

	@Override
	public void onInit() {
		options = add(new GuiOptionsContainer(this));
		options.add("Menu", new GuiOptionsButtonKeybind(this, minimap.config.keyMap));
		options.add("Show / Hide Map", new GuiOptionsButtonKeybind(this, minimap.config.keyHideMap));
		options.add("Fullscreen Map", new GuiOptionsButtonKeybind(this, minimap.config.keyFullscreen));
		options.add("Zoom In", new GuiOptionsButtonKeybind(this, minimap.config.keyZoomIn));
		options.add("Zoom Out", new GuiOptionsButtonKeybind(this, minimap.config.keyZoomOut));
		options.add("Show Waypoints", new GuiOptionsButtonKeybind(this, minimap.config.keyWaypointList));
		options.add("Toggle Waypoints", new GuiOptionsButtonKeybind(this, minimap.config.keyWaypointToggle));
		options.add("Create Waypoint", new GuiOptionsButtonKeybind(this, minimap.config.keyWaypointCreate));

		navBottom = add(new GuiNavigationContainer(this, options, Position.BOTTOM));
		navBottom.add(new GuiButtonNavigation(this, "Back", options).addActionListener((e) -> back()));
		
		navTop = add(new GuiNavigationContainer(this, options, Position.TOP));
		navTop.add(new GuiButtonNavigation(this, "Keybinds", options));
	}

}
