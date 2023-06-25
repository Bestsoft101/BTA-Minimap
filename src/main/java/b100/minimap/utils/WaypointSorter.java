package b100.minimap.utils;

import java.util.Comparator;

import b100.minimap.mc.Player;
import b100.minimap.waypoint.Waypoint;

public class WaypointSorter implements Comparator<Waypoint> {
	
	public Player player;
	
	public WaypointSorter(Player player) {
		this.player = player;
	}
	
	public double getDistance(Waypoint waypoint, Player player) {
		double dx = (waypoint.x + 0.5) - player.getPosX(1.0f);
		double dy = (waypoint.y + 0.5) - player.getPosY(1.0f);
		double dz = (waypoint.z + 0.5) - player.getPosZ(1.0f);
		
		return dx * dx + dy * dy + dz * dz;
	}
	
	@Override
	public int compare(Waypoint o1, Waypoint o2) {
		double d = getDistance(o1, player) - getDistance(o2, player);
		
		if(d > 0) return 1;
		if(d < 0) return -1;
		return 0;
	}

}
