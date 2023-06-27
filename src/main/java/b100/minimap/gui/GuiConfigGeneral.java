package b100.minimap.gui;

import b100.minimap.config.Config;
import b100.minimap.gui.GuiNavigationContainer.Position;
import b100.minimap.gui.waypoint.GuiWaypoints;

public class GuiConfigGeneral extends GuiScreen {

	public GuiOptionsContainer options;
	public GuiNavigationContainer navTop;
	public GuiNavigationContainer navBottom;
	
	public GuiConfigGeneral(GuiScreen parentScreen) {
		super(parentScreen);
	}
	
	@Override
	public void onInit() {
		Config config = minimap.config;
		
		options = add(new GuiOptionsContainer(this));
		options.add("Map Visible", new GuiOptionButtonBoolean(this, config.mapVisible));
		options.add("Map Config", new GuiButton(this, "->").addActionListener((e) -> utils.displayGui(new GuiConfigMap(this))));
		options.add("Require Item", new GuiOptionButtonRequireItem(this, config.requireItem));
		options.add("Update Speed", new GuiOptionButtonInteger(this, config.updateSpeed));
		options.add("Debug", new GuiButton(this, "->").addActionListener((e) -> utils.displayGui(new GuiConfigDebug(this))));
		
		navBottom = add(new GuiNavigationContainer(this, options, Position.BOTTOM));
		navBottom.add(new GuiButtonNavigation(this, "Close", options).addActionListener((e) -> back()));
		navBottom.add(new GuiButtonNavigation(this, "Waypoints", options).addActionListener((e) -> utils.displayGui(new GuiWaypoints(this))));
		navBottom.add(new GuiButtonNavigation(this, "Keybinds", options).addActionListener((e) -> utils.displayGui(new GuiConfigInput(this))));
		
		navTop = add(new GuiNavigationContainer(this, options, Position.TOP));
		navTop.add(new GuiButtonNavigation(this, "Minimap Configuration", options));
	}
	
}
