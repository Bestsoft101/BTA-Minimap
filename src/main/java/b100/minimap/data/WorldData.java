package b100.minimap.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import b100.minimap.waypoint.Waypoint;

public class WorldData {
	
	public File file;
	
	public List<Waypoint> waypoints = new ArrayList<>();
	
	public WorldData(File file) {
		this.file = file;
		
		waypoints.add(new Waypoint("Spawn", 0, 128, 0, 0xff0000));
		waypoints.add(new Waypoint("Home", 256, 128, 256, 0x00ff00));
		
		Random rand = new Random(12345);
		for(int i=0; i < 16; i++) {
			waypoints.add(new Waypoint("Test " + (i + 1), rand.nextInt(1024) - 512, 128 + rand.nextInt(128), rand.nextInt(1024) - 512, rand.nextInt()));
		}
	}
	
	public void load() {
		
	}
	
	public void save() {
		
	}

}
