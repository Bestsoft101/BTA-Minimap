package b100.minimap.mc.impl;

import java.util.ArrayList;
import java.util.List;

import b100.minimap.render.WorldListener;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.world.LevelListener;

public class WorldAccessImpl implements LevelListener {

	public List<WorldListener> listeners = new ArrayList<>();
	
	@Override
	public void addParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		
	}

	@Override
	public void addParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
		
	}

	@Override
	public void allChanged() {
		for(int i=0; i < listeners.size(); i++) {
			listeners.get(i).onUpdateAllChunks();
		}
	}

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
	public void playBlockSoundEffect(Block var1, EnumBlockSoundEffectType var2, double var3, double var5, double var7) {
		
	}

	@Override
	public void playSound(String var1, SoundType var2, double var3, double var5, double var7, float var9, float var10) {
		
	}

	@Override
	public void playStreamingMusic(String var1, int var2, int var3, int var4) {
		
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

}
