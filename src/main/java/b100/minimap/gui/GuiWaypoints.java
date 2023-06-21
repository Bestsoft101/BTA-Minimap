package b100.minimap.gui;

import java.util.List;

import b100.minimap.Minimap;
import b100.minimap.gui.GuiNavigationContainer.Position;
import b100.minimap.waypoint.Waypoint;

public class GuiWaypoints extends GuiScreen {
	
	public GuiScrollableContainer waypointsContainer;
	public GuiNavigationContainer nav;
	
	public GuiWaypoints(GuiScreen parentScreen) {
		super(parentScreen);
	}

	@Override
	public void onInit() {
		waypointsContainer = new GuiScrollableContainer(this, 10);
		
		List<Waypoint> waypoints = Minimap.instance.worldData.waypoints;
		for(int i=0; i < waypoints.size(); i++) {
			final Waypoint waypoint = waypoints.get(i);
			
			waypointsContainer.addScrollable(new GuiButton(this, waypoint.name).addActionListener((e) -> utils.displayGui(new GuiEditWaypoint(this, waypoint))));
		}
		
		add(waypointsContainer);
		
		nav = add(new GuiNavigationContainer(this, waypointsContainer, Position.BOTTOM));
		nav.add(new GuiButtonNavigation(this, "Back", waypointsContainer).addActionListener((e) -> back()));
		nav.add(new GuiButtonNavigation(this, "Create", waypointsContainer).addActionListener((e) -> utils.displayGui(new GuiEditWaypoint(this))));
	}
	
	@Override
	public void onResize() {
		waypointsContainer.onResize();
		nav.onResize();
	}

}
