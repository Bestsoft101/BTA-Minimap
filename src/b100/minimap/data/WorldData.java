package b100.minimap.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import b100.json.element.JsonArray;
import b100.json.element.JsonElement;
import b100.json.element.JsonObject;
import b100.minimap.Minimap;
import b100.minimap.mc.IDimension;
import b100.minimap.waypoint.Waypoint;
import b100.utils.StringReader;
import b100.utils.StringUtils;
import net.minecraft.src.World;

public class WorldData {
	
	public File directory;
	public World world;
	public IDimension dimension;
	
	private List<Waypoint> waypoints = new ArrayList<>();
	
	private boolean savingAll = false;
	
	public WorldData(File directory, World world) {
		this.directory = directory;
		this.world = world;
		this.dimension = Minimap.instance.minecraftHelper.getDimensionFromWorld(world);
	}
	
	public void load() {
		Minimap.log("Loading world data: '"+directory.getAbsolutePath()+"'");
		
		loadWaypoints();
	}
	
	public void save() {
		Minimap.log("Saving world data: '"+directory.getAbsolutePath()+"'");
		savingAll = true;
		directory.mkdirs();
		
		saveWaypoints();
		savingAll = false;
	}
	
	public void loadWaypoints() {
		waypoints.clear();
		
		File waypointsFile = new File(directory, "waypoints.json");
		
		if(waypointsFile.exists()) {
			try {
				JsonObject jsonObject = new JsonObject(new StringReader(StringUtils.getFileContentAsString(waypointsFile)));
				JsonArray jsonArray = jsonObject.getArray("waypoints");
				if(jsonArray != null) {
					for(JsonElement element : jsonArray) {
						waypoints.add(new Waypoint(this, element.getAsObject()));
					}	
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
		
		JsonArray array = new JsonArray(waypoints.size());
		for(int i=0; i < waypoints.size(); i++) {
			array.set(i, waypoints.get(i).toJson());
		}
		
		JsonObject root = new JsonObject();
		root.set("waypoints", array);
		
		StringUtils.saveStringToFile(waypointsFile, root.toString());
		if(!savingAll) {
			Minimap.log("Saved waypoints to '" + waypointsFile.getAbsolutePath() + "'");
		}
	}
	
	public void addWaypoint(Waypoint waypoint) {
		this.waypoints.add(waypoint);
	}
	
	public boolean remove(Waypoint waypoint) {
		return this.waypoints.remove(waypoint);
	}
	
	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

}
