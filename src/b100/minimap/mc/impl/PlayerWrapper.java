package b100.minimap.mc.impl;

import b100.minimap.mc.Player;
import net.minecraft.src.EntityPlayerSP;

public class PlayerWrapper implements Player {
	
	public EntityPlayerSP player;

	@Override
	public double getRotationYaw() {
		return player.rotationYaw;
	}

	@Override
	public double getRotationPitch() {
		return player.rotationPitch;
	}

	@Override
	public double getPosX(float partialTicks) {
		return player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
	}

	@Override
	public double getPosY(float partialTicks) {
		return player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
	}

	@Override
	public double getPosZ(float partialTicks) {
		return player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
	}

	@Override
	public void teleportTo(int x, int y, int z) {
		double x1 = x + 0.5;
		double y1 = (y - 1) + player.yOffset + 0.01f;
		double z1 = z + 0.5;
		
		if(player.worldObj.isMultiplayerAndNotHost) {
			player.sendChatMessage("/tp "+player.username+" "+x1+" "+y1+" "+z1);
		}else {
			player.setPosition(x1, y1, z1);
		}
	}

}
