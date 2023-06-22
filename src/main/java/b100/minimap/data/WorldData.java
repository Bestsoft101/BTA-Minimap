package b100.minimap.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import b100.json.element.JsonEntry;
import b100.json.element.JsonObject;
import b100.minimap.Minimap;
import b100.minimap.waypoint.Waypoint;
import b100.utils.StringReader;
import b100.utils.StringUtils;

public class WorldData {
	
	public File directory;
	
	public List<Waypoint> waypoints = new ArrayList<>();
	
	public WorldData(File directory) {
		this.directory = directory;
	}
	
	public void load() {
		loadWaypoints();
	}
	
	public void save() {
		directory.mkdirs();
		
		saveWaypoints();
	}
	
	public void loadWaypoints() {
		waypoints.clear();
		
		File waypointsFile = new File(directory, "waypoints.json");
		
		if(waypointsFile.exists()) {
			try {
				JsonObject jsonObject = new JsonObject(new StringReader(StringUtils.getFileContentAsString(waypointsFile)));
				for(JsonEntry entry : jsonObject) {
					waypoints.add(new Waypoint(entry.name, entry.value.getAsObject()));
				}
			}catch (Exception e) {
				throw new RuntimeException("Could not load waypoints from '"+waypointsFile.getAbsolutePath()+"'", e);
			}
		}else {
			Minimap.log("Waypoint file '" + waypointsFile.getAbsolutePath() + "' does not exist!");
		}
	}
	
	public void saveWaypoints() {
		File waypointsFile = new File(directory, "waypoints.json");
		File waypointsFileOld = new File(directory, "waypoints.json_old");
		
		if(waypointsFileOld.exists()) waypointsFileOld.delete();
		if(waypointsFile.exists()) waypointsFile.renameTo(waypointsFileOld);
		
		JsonObject root = new JsonObject();
		for(Waypoint waypoint : waypoints) {
			root.set(waypoint.name, waypoint.toJson());
		}
		
		StringUtils.saveStringToFile(waypointsFile, root.toString());
		Minimap.log("Saved waypoints to '" + waypointsFile.getAbsolutePath() + "'");
	}

}
