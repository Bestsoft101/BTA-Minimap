package b100.minimap.waypoint;

import b100.json.element.JsonObject;
import b100.minimap.Minimap;
import b100.minimap.data.WorldData;
import b100.minimap.mc.IDimension;
import b100.minimap.utils.Utils;

public class Waypoint {
	
	public String name;
	public int x;
	public int y;
	public int z;
	public int color = 0xFF00FF;
	public boolean visible = true;
	public IDimension dimension;
	
	public Waypoint(WorldData worldData, String name, int x, int y, int z, int color, boolean visible) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.visible = visible;
		this.dimension = Minimap.instance.minecraftHelper.getDimensionFromWorld(worldData.world);
	}
	
	public Waypoint(WorldData worldData, JsonObject jsonObject) {
		this.name = jsonObject.getString("name");
		this.x = jsonObject.getInt("x");
		this.y = jsonObject.getInt("y");
		this.z = jsonObject.getInt("z");
		this.color = Utils.parseColor(jsonObject.getString("color"));
		this.visible = jsonObject.getBoolean("visible");
		this.dimension = Minimap.instance.minecraftHelper.getDimension(jsonObject.getString("dimension"));
		if(dimension == null) {
			Minimap.log("Unknown dimension '"+dimension+"' saved in waypoint '"+name+"', using default");
			dimension = Minimap.instance.minecraftHelper.getDefaultDimension(worldData.world);
		}
	}
	
	public Waypoint(Waypoint waypoint) {
		set(waypoint);
	}
	
	public void set(Waypoint waypoint) {
		this.name = waypoint.name;
		this.x = waypoint.x;
		this.y = waypoint.y;
		this.z = waypoint.z;
		this.color = waypoint.color;
		this.visible = waypoint.visible;
		this.dimension = waypoint.dimension;
	}
	
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.set("name", this.name);
		jsonObject.set("x", this.x);
		jsonObject.set("y", this.y);
		jsonObject.set("z", this.z);
		jsonObject.set("color", Utils.toColorString(color, false));
		jsonObject.set("visible", this.visible);
		jsonObject.set("dimension", this.dimension.getId());
		jsonObject.setCompact(true);
		return jsonObject;
	}
	
	public Waypoint copy() {
		return new Waypoint(this);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
}
