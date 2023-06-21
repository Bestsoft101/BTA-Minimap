package b100.minimap.mc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import b100.utils.ReflectUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GuiChat;
import net.minecraft.src.IWorldAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraft.src.helper.Buffer;

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

	@Override
	public ByteBuffer getBufferWithCapacity(int capacity) {
		Buffer.checkBufferSize(capacity);
		return Buffer.buffer;
	}

	@Override
	public boolean isGuiVisible() {
		return mc.gameSettings.immersiveMode.value <= 1;
	}

	@Override
	public boolean isChatOpened() {
		return mc.currentScreen instanceof GuiChat;
	}

	@Override
	public boolean doesPlayerHaveItem(Item item) {
		EntityPlayer player = getThePlayer();
		
		for(int i=0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(stack != null && stack.getItem() == item) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDebugScreenOpened() {
		return mc.gameSettings.showDebugScreen.value;
	}

	@Override
	public boolean isMultiplayer(World world) {
		return world instanceof WorldClient;
	}

	@Override
	public String getWorldDirectoryName(World world) {
		SaveHandler saveHandler = (SaveHandler) world.getSaveHandler();
		File saveDirectory = ReflectUtils.getValue(ReflectUtils.getField(SaveHandler.class, "saveDirectory"), saveHandler, File.class);
		return saveDirectory.getName();
	}

	@Override
	public String getServerName(World world) {
		WorldClient worldClient = (WorldClient) world;
		NetClientHandler sendQueue = ReflectUtils.getValue(ReflectUtils.getField(WorldClient.class, "sendQueue"), worldClient, NetClientHandler.class);
		NetworkManager netManager = ReflectUtils.getValue(ReflectUtils.getField(NetClientHandler.class, "netManager"), sendQueue, NetworkManager.class);
		Socket socket = ReflectUtils.getValue(ReflectUtils.getField(NetworkManager.class, "networkSocket"), netManager, Socket.class);
		
		return socket.getInetAddress().getHostName() + ":" + socket.getPort();
	}

	@Override
	public boolean isCharacterAllowed(char c) {
		return ChatAllowedCharacters.allowedCharacters.indexOf(c) != -1;
	}

}
