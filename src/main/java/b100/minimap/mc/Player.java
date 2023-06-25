package b100.minimap.mc;

public interface Player {
	
	public double getPosX(float partialTicks);
	
	public double getPosY(float partialTicks);
	
	public double getPosZ(float partialTicks);
	
	public double getRotationPitch();
	
	public double getRotationYaw();
	
	public void teleportTo(int x, int y, int z);

}
