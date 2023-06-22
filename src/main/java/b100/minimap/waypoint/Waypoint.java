package b100.minimap.waypoint;

import b100.json.element.JsonObject;

public class Waypoint {
	
	public String name;
	public int x;
	public int y;
	public int z;
	public int color;
	public boolean visible = true;
	
	public Waypoint(String name, int x, int y, int z, int color, boolean visible) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.visible = visible;
	}
	
	public Waypoint(String name, JsonObject jsonObject) {
		this.name = name;
		this.x = jsonObject.getInt("x");
		this.y = jsonObject.getInt("y");
		this.z = jsonObject.getInt("z");
		this.color = jsonObject.getInt("color");
		this.visible = jsonObject.getBoolean("visible");
	}
	
	public Waypoint copy() {
		return new Waypoint(name, x, y, z, color, visible);
	}
	
	public void set(Waypoint waypoint) {
		this.name = waypoint.name;
		this.x = waypoint.x;
		this.y = waypoint.y;
		this.z = waypoint.z;
		this.color = waypoint.color;
		this.visible = waypoint.visible;
	}
	
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.set("x", this.x);
		jsonObject.set("y", this.y);
		jsonObject.set("z", this.z);
		jsonObject.set("color", this.color);
		jsonObject.set("visible", this.visible);
		return jsonObject;
	}
	
}
