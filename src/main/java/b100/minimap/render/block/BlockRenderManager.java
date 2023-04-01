package b100.minimap.render.block;

import net.minecraft.src.Block;

public class BlockRenderManager {
	
	public RenderType[] renderTypes = new RenderType[Block.blocksList.length];
	
	public BlockRenderManager(TileColors tileColors) {
		addDefaultColors();
	}
	
	public void addDefaultColors() {
		for(int i=0; i < renderTypes.length; i++) {
			renderTypes[i] = RenderType.OPAQUE;
		}
		
		renderTypes[0] = RenderType.INVISIBLE;

		setRenderType(Block.glass, RenderType.INVISIBLE);
		setRenderType(Block.torchCoal, RenderType.INVISIBLE);
		
		setRenderType(Block.tallgrass, RenderType.INVISIBLE);
		setRenderType(Block.tallgrassFern, RenderType.INVISIBLE);
		setRenderType(Block.flowerRed, RenderType.INVISIBLE);
		setRenderType(Block.flowerYellow, RenderType.INVISIBLE);
		setRenderType(Block.algae, RenderType.INVISIBLE);

		setRenderType(Block.fluidWaterFlowing, RenderType.TRANSPARENT);
		setRenderType(Block.fluidWaterStill, RenderType.TRANSPARENT);
		setRenderType(Block.ice, RenderType.TRANSPARENT);
	}
	
	public void setRenderType(Block block, RenderType renderType) {
		renderTypes[block.blockID] = renderType;
	}
	
	public RenderType getRenderType(int id) {
		return renderTypes[id];
	}
	
}
