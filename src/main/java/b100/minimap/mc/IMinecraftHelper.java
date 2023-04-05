package b100.minimap.mc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.IWorldAccess;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public interface IMinecraftHelper {
	
	public Minecraft getMinecraftInstance();
	
	public File getMinecraftDir();
	
	public abstract int getDisplayWidth();
	
	public abstract int getDisplayHeight();
	
	public int getScaledWidth();
	
	public int getScaledHeight();
	
	public int getGuiScaleFactor();
	
	public void addWorldAccess(World world, IWorldAccess worldAccess);
	
	public void removeWorldAccess(World world, IWorldAccess worldAccess);
	
	public EntityPlayerSP getThePlayer();
	
	public int generateTexture();
	
	public int getTexture(String path);
	
	public BufferedImage getTextureAsImage(String path);
	
	public ByteBuffer getBufferWithCapacity(int capacity);
	
	public boolean isGuiVisible();
	
	public boolean isChatOpened();
	
	public boolean isDebugScreenOpened();
	
	public boolean doesPlayerHaveItem(Item item);
	
}
