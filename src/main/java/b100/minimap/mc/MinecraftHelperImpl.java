package b100.minimap.mc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.IWorldAccess;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.World;

public class MinecraftHelperImpl implements IMinecraftHelper {

	private Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public Minecraft getMinecraftInstance() {
		return mc;
	}

	@Override
	public File getMinecraftDir() {
		return Minecraft.getMinecraftDir();
	}

	@Override
	public int getDisplayWidth() {
		return mc.resolution.width;
	}

	@Override
	public int getDisplayHeight() {
		return mc.resolution.height;
	}

	@Override
	public int getScaledWidth() {
		return mc.resolution.scaledWidth;
	}

	@Override
	public int getScaledHeight() {
		return mc.resolution.scaledHeight;
	}

	@Override
	public int getGuiScaleFactor() {
		return mc.resolution.scale;
	}

	@Override
	public void addWorldAccess(World world, IWorldAccess worldAccess) {
		world.addWorldAccess(worldAccess);
	}

	@Override
	public void removeWorldAccess(World world, IWorldAccess worldAccess) {
		world.removeWorldAccess(worldAccess);
	}

	@Override
	public EntityPlayerSP getThePlayer() {
		return mc.thePlayer;
	}

	@Override
	public int generateTexture() {
		return GLAllocation.generateTexture();
	}

	@Override
	public int getTexture(String path) {
		return mc.renderEngine.getTexture(path);
	}

	@Override
	public BufferedImage getTextureAsImage(String path) {
		TexturePackBase texturePack = mc.texturePackList.selectedTexturePack;
		
		InputStream stream = null;
		
		try {
			stream = texturePack.getResourceAsStream(path);
			
			return ImageIO.read(stream);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				stream.close();
			}catch (Exception e) {}
		}
		
		return null;
	}

}
