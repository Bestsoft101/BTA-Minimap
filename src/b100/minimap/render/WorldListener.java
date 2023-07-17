package b100.minimap.render;

public interface WorldListener {
	
	public void onUpdateBlock(int x, int y, int z);
	
	public void onUpdateBlocks(int minX, int minY, int minZ, int maxX, int maxY, int maxZ);
	
	public void onUpdateAllChunks();
	
}
