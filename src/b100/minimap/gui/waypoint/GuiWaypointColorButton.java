package b100.minimap.gui.waypoint;

import b100.minimap.gui.GuiButton;
import b100.minimap.gui.GuiScreen;
import b100.minimap.waypoint.Waypoint;

public class GuiWaypointColorButton extends GuiButton {

	public Waypoint waypoint;
	
	public GuiWaypointColorButton(GuiScreen screen, Waypoint waypoint) {
		super(screen);
		
		this.waypoint = waypoint;
	}
	
	@Override
	public int getColor() {
		return waypoint.color | 0xFF000000;
	}

}
