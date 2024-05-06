package b100.minimap.mc.impl;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import b100.minimap.Minimap;
import b100.minimap.render.block.TileColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.AtlasStitcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class TileColorsBTA extends TileColors {
	
	public static final TileColorsBTA instance = new TileColorsBTA(Minecraft.getMinecraft(Minecraft.class));
	
	public Minecraft mc;
	
	public final Map<IconCoordinate, Integer> mapColors = new HashMap<>();
	
	private TileColorsBTA(Minecraft minecraft) {
		this.mc = minecraft;
	}

	@Override
	public void createTileColors() {
		mapColors.clear();
		
		AtlasStitcher atlas = TextureRegistry.blockAtlas;
		BufferedImage atlasImage = atlas.atlas;
		
		Set<IconCoordinate> allTextures = new HashSet<>();
		allTextures.addAll(atlas.textureMap.values());
		
		for(IconCoordinate texture : allTextures) {
			mapColors.put(texture, getAverageColor(atlasImage, texture.iconX, texture.iconY, texture.width, texture.height, 0));
		}
		
		int waterColorOverride;
		if(mc.gameSettings.biomeWater.value) {
			waterColorOverride = 0xFF808080;
		}else {
			waterColorOverride = 0xFF1828FF;
		}
		
		IconCoordinate water_still = TextureRegistry.blockAtlas.textureMap.get("minecraft:water_still");
		IconCoordinate water_flowing = TextureRegistry.blockAtlas.textureMap.get("minecraft:water_flowing");
		
		mapColors.put(water_still, waterColorOverride);
		mapColors.put(water_flowing, waterColorOverride);
	}

	@Override
	public int getTileColor(World world, int x, int y, int z, Block block) {
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
		
		IconCoordinate texture = model.getBlockTexture(world, x, y, z, Side.TOP);
		
		Integer color = mapColors.get(texture);
		if(color == null) {
			return 0xFFFFFF;
		}
		return color;
	}
	
	public void onOptionValueChanged(GameSettings settings, Option<?> option) {
		if(option == settings.biomeWater) {
			createTileColors();
			
			Minimap.instance.mapRender.updateAllTiles();
		}
	}

}
