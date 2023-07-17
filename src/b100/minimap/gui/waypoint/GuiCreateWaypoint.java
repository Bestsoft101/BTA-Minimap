package b100.minimap.gui.waypoint;

import java.util.Random;

import b100.minimap.gui.GuiScreen;
import b100.minimap.waypoint.Waypoint;

public class GuiCreateWaypoint extends GuiEditWaypointBase {

	public GuiCreateWaypoint(GuiScreen parentScreen) {
		super(parentScreen);
		
		this.title = "Create Waypoint";
		this.waypoint = new Waypoint(minimap.worldData, "", playerOffsetX, playerOffsetY, playerOffsetZ, new Random().nextInt() | 0xFF000000, true);
	}

	@Override
	public void ok() {
		minimap.worldData.addWaypoint(waypoint);
		minimap.worldData.saveWaypoints();
		back();
	}

	@Override
	public void cancel() {
		back();
	}

	

}
