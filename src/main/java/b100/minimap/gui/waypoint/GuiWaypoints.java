package b100.minimap.gui.waypoint;

import java.util.List;

import b100.minimap.Minimap;
import b100.minimap.gui.GuiButtonNavigation;
import b100.minimap.gui.GuiNavigationContainer;
import b100.minimap.gui.GuiNavigationContainer.Position;
import b100.minimap.gui.GuiScreen;
import b100.minimap.gui.GuiScrollableContainer;
import b100.minimap.waypoint.Waypoint;

public class GuiWaypoints extends GuiScreen {
	
	public GuiScrollableContainer waypointsContainer;
	public GuiNavigationContainer navTop;
	public GuiNavigationContainer navBottom;
	
	public GuiWaypoints(GuiScreen parentScreen) {
		super(parentScreen);
	}

	@Override
	public void onInit() {
		waypointsContainer = new GuiScrollableContainer(this, 10);
		
		List<Waypoint> waypoints = Minimap.instance.worldData.waypoints;
		for(int i=0; i < waypoints.size(); i++) {
			final Waypoint waypoint = waypoints.get(i);
			
			waypointsContainer.addScrollable(new GuiWaypointButton(this, waypoint));
		}
		
		add(waypointsContainer);
		
		navBottom = add(new GuiNavigationContainer(this, waypointsContainer, Position.BOTTOM));
		navBottom.add(new GuiButtonNavigation(this, "Back", waypointsContainer).addActionListener((e) -> back()));
		navBottom.add(new GuiButtonNavigation(this, "Create", waypointsContainer).addActionListener((e) -> createWaypoint()));
		
		navTop = add(new GuiNavigationContainer(this, waypointsContainer, Position.TOP));
		navTop.add(new GuiButtonNavigation(this, "Waypoints", waypointsContainer));
	}
	
	public void createWaypoint() {
		utils.displayGui(new GuiCreateWaypoint(this));
	}
	
	@Override
	public void onGuiOpened() {
		init();
	}

}
