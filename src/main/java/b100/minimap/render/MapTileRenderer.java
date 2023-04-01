package b100.minimap.render;

import java.nio.IntBuffer;

import b100.minimap.Minimap;
import b100.minimap.render.block.BlockRenderManager;
import b100.minimap.render.block.RenderType;
import b100.minimap.render.block.TileColors;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class MapTileRenderer {
	
	public Minimap minimap;
	
	public World world;
	public int worldHeight = 256;
	
	private int[] maxHeightCache = new int[18 * 18];
	private int[] minHeightCache = new int[18 * 18];
	
	public MapTileRenderer(Minimap minimap) {
		this.minimap = minimap;
	}
	
	public boolean render(IntBuffer colorBuffer, int posX, int posZ, int width, int height) {
		boolean rendered = false;
		
		if(world == null) {
			throw new NullPointerException();
		}
		
		BlockRenderManager blockColors = minimap.blockRenderManager;
		TileColors tileColors = minimap.tileColors;
		
		final int worldHeight = 256;
		
		int shadeType = minimap.config.mapConfig.shadeType.value;
		boolean lighting = minimap.config.mapConfig.lighting.value;
		
		for(int i=0; i < 18; i++) {
			for(int j=0; j < 18; j++) {
				int x = (posX << 4) + i - 1;
				int z = (posZ << 4) + j - 1;
				
				setHeight(minHeightCache, i, j, -1);
				setHeight(maxHeightCache, i, j, -1);
				
				boolean maxHeightSet = false;
				
				for(int y = worldHeight - 1; y >= 0; y--) {
					int id = world.getBlockId(x, y, z);
					RenderType renderType = blockColors.getRenderType(id);
					if(renderType != RenderType.INVISIBLE) {
						rendered = true;
						if(!maxHeightSet) {
							setHeight(maxHeightCache, i, j, y);
							maxHeightSet = true;	
						}
					}
					if(renderType == RenderType.OPAQUE) {
						setHeight(minHeightCache, i, j, y);
						break;
					}
				}
			}
		}
		
		for(int i=0; i < 16; i++) {
			for(int j=0; j < 16; j++) {
				int x = (posX << 4) + i;
				int z = (posZ << 4) + j;
				
				int color = 0x0;

				int i0 = i + 1;
				int j0 = j + 1;
				
				int minHeight = getHeight(minHeightCache, i0, j0);
				int maxHeight = getHeight(maxHeightCache, i0, j0);
				
				if(minHeight != -1 && maxHeight != -1) {
					for(int y = minHeight; y <= maxHeight; y++) {
						int id = world.getBlockId(x, y, z);
						Block block = Block.getBlock(id);
						RenderType renderType = blockColors.getRenderType(id);
						
						if(renderType == RenderType.OPAQUE) {
							color = tileColors.getTileColor(block.getBlockTexture(world, x, y, z, 1));
							color = multiplyColor(color, block.colorMultiplier(world, world, x, y, z));
							if(lighting) {
								color = multiplyColor(color, block.getBlockBrightness(world, x, y + 1, z));
							}
						}else if(renderType == RenderType.TRANSPARENT) {
							int transparentColor = tileColors.getTileColor(block.getBlockTexture(world, x, y, z, 1));
							transparentColor = multiplyColor(transparentColor, block.colorMultiplier(world, world, x, y, z));
							if(lighting) {
								transparentColor = multiplyColor(transparentColor, block.getBlockBrightness(world, x, y + 1, z));
							}
							
							color = mixColor(color, transparentColor, 0.66f);
						}
					}
					
					float brightness = 1.0f;
					
					if(shadeType == 0) {
						int y0 = getHeight(maxHeightCache, i0, j0+1);
						int y1 = getHeight(maxHeightCache, i0+1, j0);
						int y2 = getHeight(maxHeightCache, i0, j0-1);
						int y3 = getHeight(maxHeightCache, i0-1, j0);
						
						if(y0 > maxHeight || y1 > maxHeight || y2 > maxHeight || y3 > maxHeight) brightness *= 0.8f;
					}else if(shadeType == 1) {
						int y0 = getHeight(maxHeightCache, i0, j0-1);
						int y1 = getHeight(maxHeightCache, i0-1, j0-1);
						int y2 = getHeight(maxHeightCache, i0-1, j0);
						
						int brightCount = 0;
						int darkCount = 0;
						
						if(y0 < maxHeight) brightCount++;
						if(y1 < maxHeight) brightCount++;
						if(y2 < maxHeight) brightCount++;
						
						if(y0 > maxHeight) darkCount++;
						if(y1 > maxHeight) darkCount++;
						if(y2 > maxHeight) darkCount++;
						
						if(brightCount > darkCount) brightness *= 1.25f;
						if(darkCount > brightCount) brightness *= 0.65f;
					}
					
					color = multiplyColor(color, brightness);
					color |= 0xFF000000;
				}
				
				colorBuffer.put(j * 16 + i, color);
			}
		}
		return rendered;
	}
	
	private void setHeight(int[] heightCache, int i, int j, int height) {
		heightCache[j * 18 + i] = height;
	}
	
	private int getHeight(int[] heightCache, int i, int j) {
		return heightCache[j * 18 + i];
	}
	
	private int multiplyColor(int color0, int color1) {
		float a0 = ((color0 >> 24) & 0xFF) / 255.0f;
		float r0 = ((color0 >> 16) & 0xFF) / 255.0f;
		float g0 = ((color0 >>  8) & 0xFF) / 255.0f;
		float b0 = ((color0 >>  0) & 0xFF) / 255.0f;
		
		float a1 = ((color1 >> 24) & 0xFF) / 255.0f;
		float r1 = ((color1 >> 16) & 0xFF) / 255.0f;
		float g1 = ((color1 >>  8) & 0xFF) / 255.0f;
		float b1 = ((color1 >>  0) & 0xFF) / 255.0f;
		
		int a = (int) ((a0 * a1) * 255.0f);
		int r = (int) ((r0 * r1) * 255.0f);
		int g = (int) ((g0 * g1) * 255.0f);
		int b = (int) ((b0 * b1) * 255.0f);
		
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	private int multiplyColor(int color0, float brightness) {
		float a0 = ((color0 >> 24) & 0xFF) / 255.0f;
		float r0 = ((color0 >> 16) & 0xFF) / 255.0f;
		float g0 = ((color0 >>  8) & 0xFF) / 255.0f;
		float b0 = ((color0 >>  0) & 0xFF) / 255.0f;
		
		int a = (int) ((a0 * brightness) * 255.0f);
		int r = (int) ((r0 * brightness) * 255.0f);
		int g = (int) ((g0 * brightness) * 255.0f);
		int b = (int) ((b0 * brightness) * 255.0f);
		
		a = clamp(a, 0, 255);
		r = clamp(r, 0, 255);
		g = clamp(g, 0, 255);
		b = clamp(b, 0, 255);
		
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public int mixColor(int color0, int color1, float f) {
		float a0 = ((color0 >> 24) & 0xFF) / 255.0f;
		float r0 = ((color0 >> 16) & 0xFF) / 255.0f;
		float g0 = ((color0 >>  8) & 0xFF) / 255.0f;
		float b0 = ((color0 >>  0) & 0xFF) / 255.0f;
		
		float a1 = ((color1 >> 24) & 0xFF) / 255.0f;
		float r1 = ((color1 >> 16) & 0xFF) / 255.0f;
		float g1 = ((color1 >>  8) & 0xFF) / 255.0f;
		float b1 = ((color1 >>  0) & 0xFF) / 255.0f;
		
		float a2 = mix(a0, a1, f);
		float r2 = mix(r0, r1, f);
		float g2 = mix(g0, g1, f);
		float b2 = mix(b0, b1, f);

		int a = (int) ((a2) * 255.0f);
		int r = (int) ((r2) * 255.0f);
		int g = (int) ((g2) * 255.0f);
		int b = (int) ((b2) * 255.0f);
		
		a = clamp(a, 0, 255);
		r = clamp(r, 0, 255);
		g = clamp(g, 0, 255);
		b = clamp(b, 0, 255);

		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public void onWorldChanged(World world) {
		this.world = world;
	}
	
	public static int brightnessToColor(float brightness) {
		int r = clamp((int) (brightness * 255.0f), 0, 255);
		
		return 255 << 24 | r << 16 | r << 8 | r;
	}
	
	public static int clamp(int a, int min, int max) {
		if(a > max) return max;
		if(a < min) return min;
		return a;
	}
	
	public static float mix(float a, float b, float c) {
		return a * (1.0f - c) + b * c;
	}
	
}
