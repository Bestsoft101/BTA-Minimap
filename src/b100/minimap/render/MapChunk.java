package b100.minimap.render;

public class MapChunk {
	
	private int posX;
	private int posZ;
	private int index;
	
	public int tile = -1;
	
	public boolean needsUpdate = false;
	
	public MapChunk() {
		
	}
	
	public MapChunk setPosition(int posX, int posZ) {
		this.posX = posX;
		this.posZ = posZ;
		this.index = MapRender.getChunkIndex(posX, posZ);
		return this;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosZ() {
		return posZ;
	}
	
	public int getIndex() {
		return index;
	}

}
