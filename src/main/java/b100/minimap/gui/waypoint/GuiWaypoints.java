package b100.minimap.gui.waypoint;

import java.util.ArrayList;
import java.util.List;

import b100.minimap.Minimap;
import b100.minimap.gui.GuiButton;
import b100.minimap.gui.GuiButtonNavigation;
import b100.minimap.gui.GuiNavigationContainer;
import b100.minimap.gui.GuiNavigationContainer.Position;
import b100.minimap.gui.GuiScreen;
import b100.minimap.gui.GuiScrollableContainer;
import b100.minimap.mc.IDimension;
import b100.minimap.utils.WaypointSorter;
import b100.minimap.waypoint.Waypoint;

public class GuiWaypoints extends GuiScreen {
	
	public GuiScrollableContainer waypointsContainer;
	public GuiNavigationContainer navTop;
	public GuiNavigationContainer navBottom;
	
	public String title;
	
	public GuiButton titleButton;
	
	public GuiWaypoints(GuiScreen parentScreen) {
		super(parentScreen);
	}

	@Override
	public void onInit() {
		waypointsContainer = add(new GuiScrollableContainer(this, 10));
		
		updateWaypointList(Minimap.instance.worldData.dimension);
		
		navBottom = add(new GuiNavigationContainer(this, waypointsContainer, Position.BOTTOM));
		navBottom.add(new GuiButtonNavigation(this, "Back", waypointsContainer).addActionListener((e) -> back()));
		navBottom.add(new GuiButtonNavigation(this, "Create", waypointsContainer).addActionListener((e) -> createWaypoint()));
		
		navTop = add(new GuiNavigationContainer(this, waypointsContainer, Position.TOP));
		titleButton = navTop.add(new GuiButtonNavigation(this, title, waypointsContainer));
	}
	
	public void updateWaypointList(IDimension dimension) {
		waypointsContainer.elements.clear();
		
		this.title = "Waypoints [" + dimension.getDisplayName() + "]";
		
		List<Waypoint> allWaypoints = minimap.worldData.getWaypoints();
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		
		for(int i=0; i < allWaypoints.size(); i++) {
			Waypoint waypoint = allWaypoints.get(i);
			if(waypoint.dimension == dimension) {
				waypoints.add(waypoint);
			}
		}
		
		waypoints.sort(new WaypointSorter(minecraftHelper.getThePlayer()));
		for(int i=0; i < waypoints.size(); i++) {
			final Waypoint waypoint = waypoints.get(i);
			
			waypointsContainer.addScrollable(new GuiWaypointButton(this, waypoint));
		}		
	}
	
	@Override
	public void onResize() {
		waypointsContainer.elementsPerPage = Math.max(10, (height - 140) / (waypointsContainer.elementHeight + 1));
		
		super.onResize();
	}
	
	public void createWaypoint() {
		utils.displayGui(new GuiCreateWaypoint(this));
	}
	
	@Override
	public void onGuiOpened() {
		init();
	}

}
