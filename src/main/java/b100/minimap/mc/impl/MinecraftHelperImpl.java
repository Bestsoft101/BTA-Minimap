package b100.minimap.mc.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import b100.minimap.mc.IDimension;
import b100.minimap.mc.IMinecraftHelper;
import b100.minimap.mc.Player;
import b100.minimap.render.WorldListener;
import b100.minimap.render.block.BlockRenderManager;
import b100.minimap.render.block.RenderType;
import b100.minimap.render.block.TileColors;
import b100.utils.ReflectUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.Dimension;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.Gamemode;
import net.minecraft.src.GuiChat;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraft.src.helper.Buffer;

public class MinecraftHelperImpl implements IMinecraftHelper {

	private Minecraft mc = Minecraft.getMinecraft();
	private PlayerWrapper playerWrapper = new PlayerWrapper();
	public WorldAccessImpl worldAccessImpl = new WorldAccessImpl();
	private Map<Dimension, DimensionWrapper> dimensionWrappers = new HashMap<>();
	
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
	public void addWorldListener(World world, WorldListener listener) {
		worldAccessImpl.listeners.add(listener);
	}

	@Override
	public void removeWorldListener(World world, WorldListener listener) {
		worldAccessImpl.listeners.remove(listener);
	}

	@Override
	public Player getThePlayer() {
		EntityPlayerSP player = mc.thePlayer;
		if(player != playerWrapper.player) {
			playerWrapper.player = player;
		}
		return playerWrapper;
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
	public boolean doesPlayerHaveCompass() {
		PlayerWrapper playerWrapper = (PlayerWrapper) getThePlayer();
		EntityPlayerSP player = playerWrapper.player;
		
		if(player.getGamemode() == Gamemode.creative) {
			return true;
		}
		
		InventoryPlayer inventory = player.inventory;
		for(int i=0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack != null && stack.getItem() == Item.toolCompass) {
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
		return socket.getInetAddress().toString() + ":" + socket.getPort();
	}

	@Override
	public boolean isCharacterAllowed(char c) {
		return ChatAllowedCharacters.allowedCharacters.indexOf(c) != -1;
	}

	@Override
	public float getScreenPaddingPercentage() {
		return mc.gameSettings.screenPadding.value * 0.125f;
	}

	@Override
	public void onWorldChanged(World world) {
		if(world != null) {
			world.addWorldAccess(worldAccessImpl);	
		}
	}

	@Override
	public void setupBlockRenderTypes(BlockRenderManager m) {
		m.setRenderType(Block.glass, RenderType.INVISIBLE);
		m.setRenderType(Block.torchCoal, RenderType.INVISIBLE);
		
		m.setRenderType(Block.tallgrass, RenderType.INVISIBLE);
		m.setRenderType(Block.tallgrassFern, RenderType.INVISIBLE);
		m.setRenderType(Block.flowerRed, RenderType.INVISIBLE);
		m.setRenderType(Block.flowerYellow, RenderType.INVISIBLE);
		m.setRenderType(Block.algae, RenderType.INVISIBLE);

		m.setRenderType(Block.fluidWaterFlowing, RenderType.TRANSPARENT);
		m.setRenderType(Block.fluidWaterStill, RenderType.TRANSPARENT);
		m.setRenderType(Block.ice, RenderType.TRANSPARENT);
	}

	@Override
	public boolean getEnableCheats() {
		return mc.theWorld.isMultiplayerAndNotHost || mc.theWorld.getWorldInfo().getCheatsEnabled();
	}

	public DimensionWrapper getDimensionWrapper(Dimension dimension) {
		DimensionWrapper wrapper = dimensionWrappers.get(dimension);
		if(wrapper == null) {
			wrapper = new DimensionWrapper(dimension);
			dimensionWrappers.put(dimension, wrapper);
		}
		return wrapper;
	}
	
	@Override
	public IDimension getDimension(String id) {
		try {
			return getDimensionWrapper(Dimension.dimensionList[Integer.parseInt(id)]);
		}catch (NumberFormatException e) {
			return null;
		}
	}
	
	@Override
	public IDimension getDimensionFromWorld(World world) {
		return getDimensionWrapper(world.dimension);
	}

	@Override
	public IDimension getDefaultDimension(World world) {
		return getDimensionWrapper(Dimension.overworld);
	}

	@Override
	public File getCurrentTexturePackFile() {
		TexturePackBase texturePackBase = mc.texturePackList.selectedTexturePack;
		if(texturePackBase instanceof TexturePackCustom) {
			return ReflectUtils.getValue(ReflectUtils.getField(TexturePackCustom.class, "texturePackFile"), texturePackBase, File.class);
		}
		return null;
	}

	@Override
	public int getTextureAtlasSize() {
		return 32;
	}

	@Override
	public TileColors getTileColors() {
		return TileColorsBTA.instance;
	}

}
