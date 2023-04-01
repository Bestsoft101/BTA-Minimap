package b100.minimap.mc;

import b100.minimap.render.IWorldListener;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IWorldAccess;
import net.minecraft.src.TileEntity;

public class WorldAccess implements IWorldAccess {

	private IWorldListener worldListener;
	
	public WorldAccess(IWorldListener worldListener) {
		this.worldListener = worldListener;
	}
	
	@Override
	public void markBlockAndNeighborsNeedsUpdate(int x, int y, int z) {
		worldListener.onUpdateBlock(x, y, z);
	}

	@Override
	public void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
		worldListener.onUpdateBlocks(var1, var2, var3, var4, var5, var6);
	}

	@Override
	public void playSound(String var1, double var2, double var4, double var6, float var8, float var9) {
		
	}

	@Override
	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		
	}

	@Override
	public void trackEntity(Entity var1) {
		
	}

	@Override
	public void untrackEntity(Entity var1) {
		
	}

	@Override
	public void updateAllRenderers() {
		worldListener.onUpdateAllChunks();
	}

	@Override
	public void playRecord(String var1, int var2, int var3, int var4) {
		
	}

	@Override
	public void sendTileEntityToPlayer(int var1, int var2, int var3, TileEntity var4) {
		
	}

	@Override
	public void playSoundEffectForPlayer(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) {
		
	}

}
