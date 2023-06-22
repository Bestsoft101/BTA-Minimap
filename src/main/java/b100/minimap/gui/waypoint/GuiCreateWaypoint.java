package b100.minimap.gui.waypoint;

import java.util.Random;

import b100.minimap.Minimap;
import b100.minimap.gui.GuiScreen;
import b100.minimap.waypoint.Waypoint;

public class GuiCreateWaypoint extends GuiEditWaypointBase {

	public GuiCreateWaypoint(GuiScreen parentScreen) {
		super(parentScreen);
		
		this.title = "Create Waypoint";
		this.waypoint = new Waypoint("", playerOffsetX, playerOffsetY, playerOffsetZ, new Random().nextInt() | 0xFF000000, true);
	}

	@Override
	public void ok() {
		Minimap.instance.worldData.waypoints.add(waypoint);
		Minimap.instance.worldData.saveWaypoints();
		back();
	}

	@Override
	public void cancel() {
		back();
	}

	

}
