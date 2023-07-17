package b100.minimap.mc.impl;

import b100.minimap.Minimap;
import b100.minimap.render.block.TileColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.Option;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class TileColorsBTA extends TileColors {
	
	public static final TileColorsBTA instance = new TileColorsBTA(Minecraft.getMinecraft(Minecraft.class));
	
	public Minecraft mc;
	
	public int[] tileColors;
	
	private TileColorsBTA(Minecraft minecraft) {
		this.mc = minecraft;
	}

	@Override
	public void createTileColors() {
		int atlasSize = Global.TEXTURE_ATLAS_WIDTH_TILES;
		
		tileColors = new int[atlasSize * atlasSize];
		
		createTileColors(mc.renderEngine.getImage("/terrain.png"), atlasSize, tileColors);
		
		int waterTextureIndex = Block.fluidWaterStill.getBlockTextureFromSideAndMetadata(Side.TOP, 0);
		
		if(mc.gameSettings.biomeWater.value) {
			tileColors[waterTextureIndex] = 0xFF808080;
		}else {
			tileColors[waterTextureIndex] = 0xFF1828FF;
		}
	}

	@Override
	public int getTileColor(World world, int x, int y, int z, Block block) {
		return tileColors[block.getBlockTexture(world, x, y, z, Side.TOP)];
	}
	
	public void onOptionValueChanged(GameSettings settings, Option<?> option) {
		if(option == settings.biomeWater) {
			createTileColors();
			
			Minimap.instance.mapRender.updateAllTiles();
		}
	}

}
