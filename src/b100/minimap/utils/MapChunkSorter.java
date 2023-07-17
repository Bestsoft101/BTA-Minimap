package b100.minimap.utils;

import java.util.Comparator;

import b100.minimap.mc.Player;
import b100.minimap.render.MapChunk;

public class MapChunkSorter implements Comparator<MapChunk>{

	public Player player;
	
	public MapChunkSorter(Player player) {
		this.player = player;
	}
	
	public double getDistance(MapChunk chunk, Player player) {
		double dx = (chunk.getPosX() + 0.5) - player.getPosX(1.0f);
		double dz = (chunk.getPosZ() + 0.5) - player.getPosZ(1.0f);
		
		return dx * dx + dz * dz;
	}
	
	@Override
	public int compare(MapChunk o1, MapChunk o2) {
		double d = getDistance(o1, player) - getDistance(o2, player);
		
		if(d > 0) return 1;
		if(d < 0) return -1;
		return 0;
	}

}
