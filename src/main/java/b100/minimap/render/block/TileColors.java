package b100.minimap.render.block;

import java.awt.image.BufferedImage;

import b100.minimap.Minimap;

public class TileColors {
	
	private Minimap minimap;
	private int[] tilecolors;
	
	public TileColors(Minimap minimap) {
		this.minimap = minimap;
	}
	
	public int getTileColor(int tile) {
		return tilecolors[tile];
	}
	
	public void createTileColors() {
		createTileColors(minimap.minecraftHelper.getTextureAsImage("/terrain.png"));
	}
	
	public void createTileColors(BufferedImage image) {
		int tiles = Minimap.instance.minecraftHelper.getTextureAtlasSize();
		tilecolors = new int[tiles * tiles];
		
		if(image == null) {
			Minimap.log("Cannot create tile colors because image is null!");
			for(int i=0; i < tilecolors.length; i++) {
				tilecolors[i] = 0xFFFFFFFF;
			}
		}
		
		Minimap.log("Creating tile colors...");
		long start = System.currentTimeMillis();
		
		int tileWidth = image.getWidth() / tiles;
		int tileHeight = image.getHeight() / tiles;
		
		int alphaTreshold = 0;
		
		for(int i=0; i < tiles; i++) {
			for(int j=0; j < tiles; j++) {
				int currentTile = j * tiles + i;
				
				int color = getAverageColor(image, i * tileWidth, j * tileHeight, tileWidth, tileHeight, alphaTreshold);
				
				if(((color >> 24) & 0xFF) <= alphaTreshold) {
					color = 0xFFFFFFFF;
				}
				
				tilecolors[currentTile] = color;
			}
		}
		
		Minimap.log("Created tile colors in " + (System.currentTimeMillis() - start) + " ms");
	}
	
	public int getAverageColor(BufferedImage image, int x, int y, int w, int h, int alphaTreshold) {
		int r = 0;
		int g = 0;
		int b = 0;
		int count = 0;
		
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				int color = image.getRGB(x + i, y + j);
				
				if(((color >> 24) & 0xFF) > alphaTreshold) {
					r += (color >> 16) & 0xFF;
					g += (color >>  8) & 0xFF;
					b += (color >>  0) & 0xFF;
					count++;
				}
			}
		}
		
		if(count == 0) {
			return 0x0;
		}
		
		r /= count;
		g /= count;
		b /= count;
		
		return 0xFF000000 | r << 16 | g << 8 | b;
	}
	
}
