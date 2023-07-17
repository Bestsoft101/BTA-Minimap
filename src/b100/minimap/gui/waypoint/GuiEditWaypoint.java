package b100.minimap.gui.waypoint;

import b100.minimap.gui.GuiScreen;
import b100.minimap.waypoint.Waypoint;

public class GuiEditWaypoint extends GuiEditWaypointBase {
	
	private Waypoint original;
	
	public GuiEditWaypoint(GuiScreen parentScreen, Waypoint waypoint) {
		super(parentScreen);
		
		this.waypoint = waypoint;
		this.original = waypoint.copy();
		this.title = "Edit Waypoint";
	}

	@Override
	public void ok() {
		back();
	}

	@Override
	public void cancel() {
		this.waypoint.set(original);
		back();
	}

}
