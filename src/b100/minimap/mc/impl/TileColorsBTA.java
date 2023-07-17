package b100.minimap.mc.impl;

import b100.minimap.Minimap;
import b100.minimap.render.block.TileColors;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.GameSettings;
import net.minecraft.src.World;
import net.minecraft.src.option.Option;

public class TileColorsBTA extends TileColors {

	public static final TileColorsBTA instance = new TileColorsBTA(Minecraft.getMinecraft());
	
	public Minecraft mc;
	
	public int[] tileColors;
	
	private TileColorsBTA(Minecraft minecraft) {
		this.mc = minecraft;
	}

	@Override
	public void createTileColors() {
		int atlasSize = net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES;
		
		tileColors = new int[atlasSize * atlasSize];
		
		createTileColors(mc.renderEngine.getImage("/terrain.png"), atlasSize, tileColors);
		
		int waterTextureIndex = Block.fluidWaterStill.getBlockTextureFromSide(1);
		
		if(mc.gameSettings.biomeWater.value) {
			tileColors[waterTextureIndex] = 0xFF808080;
		}else {
			tileColors[waterTextureIndex] = 0xFF1828FF;
		}
	}

	@Override
	public int getTileColor(World world, int x, int y, int z, Block block) {
		return tileColors[block.getBlockTexture(world, x, y, z, 1)];
	}
	
	public void onOptionValueChanged(GameSettings settings, Option<?> option) {
		if(option == settings.biomeWater) {
			createTileColors();
			
			Minimap.instance.mapRender.updateAllTiles();
		}
	}

}
