package b100.minimap.mc.impl;

import java.util.ArrayList;
import java.util.List;

import b100.minimap.render.WorldListener;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.LevelListener;

public class WorldAccessImpl implements LevelListener {

	public List<WorldListener> listeners = new ArrayList<>();
	
	@Override
	public void blockChanged(int x, int y, int z) {
		for(int i=0; i < listeners.size(); i++) {
			listeners.get(i).onUpdateBlock(x, y, z);
		}
	}

	@Override
	public void entityAdded(Entity var1) {
		
	}

	@Override
	public void entityRemoved(Entity var1) {
		
	}

	@Override
	public void levelEvent(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) {
		
	}
	
	@Override
	public void playSound(Entity player, String var1, SoundCategory var2, double var3, double var5, double var7, float var9, float var10) {
		
	}

	@Override
	public void setBlocksDirty(int var1, int var2, int var3, int var4, int var5, int var6) {
		for(int i=0; i < listeners.size(); i++) {
			listeners.get(i).onUpdateBlocks(var1, var2, var3, var4, var5, var6);
		}
	}

	@Override
	public void tileEntityChanged(int var1, int var2, int var3, TileEntity var4) {
		
	}

	@Override
	public void addParticle(String arg0, double arg1, double arg2, double arg3, double arg4, double arg5, double arg6, int arg7) {
		
	}

	@Override
	public void addParticle(String arg0, double arg1, double arg2, double arg3, double arg4, double arg5, double arg6, int arg7, double arg8) {
		
	}

	@Override
	public void allChanged(boolean arg0, boolean arg1) {
		for(int i=0; i < listeners.size(); i++) {
			listeners.get(i).onUpdateAllChunks();
		}
	}

	@Override
	public void playStreamingMusic(String arg0, String arg1, int arg2, int arg3, int arg4) {
		
	}

}
