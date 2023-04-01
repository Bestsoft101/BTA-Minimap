package b100.minimap.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;

import b100.minimap.Minimap;

public class MapTileManager {
	
	public static final int TILEWIDTH = 16;
	
	public Minimap minimap;
	
	public final int width;
	public int texture;
	
	private boolean[] tiles;
	
	public MapTileManager(Minimap minimap, int width) {
		this.minimap = minimap;
		this.width = width;
		
		tiles = new boolean[width * width];
		
		createTexture();
	}
	
	private void createTexture() {
		texture = minimap.minecraftHelper.generateTexture();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width * TILEWIDTH, width * TILEWIDTH, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
	}
	
	public int getEmptyTile() {
		for(int i=0; i < tiles.length; i++) {
			if(tiles[i] == false) {
				return i;
			}
		}
		return -1;
	}
	
	public void setTileInUse(int tile) {
		tiles[tile] = true;
	}
	
	public void setTileNotInUse(int tile) {
		tiles[tile] = false;
	}
	
	public void setTile(int tile, ByteBuffer color) {
		for(int i = 0; i < color.capacity() / 4; i++) {
			int j = i << 2;
			byte a = color.get(j + 3);
			byte r = color.get(j + 2);
			byte g = color.get(j + 1);
			byte b = color.get(j + 0);
			
			color.put(j + 0, (byte) r); //r
			color.put(j + 1, (byte) g); //g
			color.put(j + 2, (byte) b); //b
			color.put(j + 3, (byte) a); //a
		}
		
		int tileX = tile % width;
		int tileY = tile / width;
		
		int x0 = tileX * TILEWIDTH;
		int y0 = tileY * TILEWIDTH;
		
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexSubImage2D(GL_TEXTURE_2D, 0, x0, y0, TILEWIDTH, TILEWIDTH, GL_RGBA, GL_UNSIGNED_BYTE, color);
		
		setTileInUse(tile);
	}
	
}
