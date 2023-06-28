package b100.minimap.render;

import static b100.minimap.utils.Utils.*;

import java.nio.IntBuffer;

import b100.minimap.Minimap;
import b100.minimap.render.block.BlockRenderManager;
import b100.minimap.render.block.RenderType;
import b100.minimap.render.block.TileColors;
import b100.minimap.utils.Utils;
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
		boolean renderAll = minimap.config.mapConfig.renderAllBlocks.value;
		
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

					if(renderAll && renderType == RenderType.INVISIBLE && id > 0) {
						renderType = RenderType.OPAQUE;
					}
					
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

						if(renderAll && renderType == RenderType.INVISIBLE && id > 0) {
							renderType = RenderType.OPAQUE;
						}
						
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
							
							color = mixColor(color, transparentColor, 0.5f);
						}
					}
					
					float brightness = 1.0f;
					
					if(shadeType == 0) {
						int y0 = getHeight(maxHeightCache, i0, j0+1);
						int y1 = getHeight(maxHeightCache, i0+1, j0);
						int y2 = getHeight(maxHeightCache, i0, j0-1);
						int y3 = getHeight(maxHeightCache, i0-1, j0);
						
						if(y0 > maxHeight || y1 > maxHeight || y2 > maxHeight || y3 > maxHeight) brightness *= 0.75f;
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
					}else if(shadeType == 2) {
						int offset = 0;
						
						offset += maxHeight - getHeight(maxHeightCache, i0 - 1, j0);
						offset += maxHeight - getHeight(maxHeightCache, i0, j0 - 1);
						
						offset += getHeight(maxHeightCache, i0 + 1, j0) - maxHeight;
						offset += getHeight(maxHeightCache, i0, j0 + 1) - maxHeight;
						
						offset = Utils.clamp(offset, -8, 8);
						brightness = (offset / 8.0f) / 2.0f + 1.0f;
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
	
	public void onWorldChanged(World world) {
		this.world = world;
	}
	
}
