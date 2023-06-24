package b100.minimap.mc;

import net.minecraft.src.EntityPlayer;

public class PlayerWrapper implements Player {
	
	public EntityPlayer player;

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

}
