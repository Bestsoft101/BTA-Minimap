package b100.minimap;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import org.lwjgl.input.Keyboard;

import b100.minimap.config.Config;
import b100.minimap.config.Keybind;
import b100.minimap.config.MapConfig;
import b100.minimap.gui.GuiConfigGeneral;
import b100.minimap.gui.GuiScreen;
import b100.minimap.gui.IGuiUtils;
import b100.minimap.mc.GuiUtilsImpl;
import b100.minimap.mc.IMinecraftHelper;
import b100.minimap.mc.MinecraftHelperImpl;
import b100.minimap.mc.WorldAccess;
import b100.minimap.render.MapRender;
import b100.minimap.render.block.BlockRenderManager;
import b100.minimap.render.block.TileColors;
import b100.minimap.render.style.MapStyle;
import b100.minimap.render.style.MapStyleGenerated;
import b100.minimap.render.style.MapStyleInternal;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public class Minimap {
	
	public static final Minimap instance = new Minimap();
	
	static {
		instance.init();
	}
	
	public IMinecraftHelper minecraftHelper = new MinecraftHelperImpl();
	public Minecraft mc = minecraftHelper.getMinecraftInstance();
	public MapRender mapRender;
	public WorldAccess worldAccess;
	public World theWorld;
	public Config config;
	public BlockRenderManager blockRenderManager;
	public TileColors tileColors;
	public IGuiUtils guiUtils;
	
	private Minimap() {
		if(mc == null)
			throw new NullPointerException("Minecraft is null!");
	}
	
	public void init() {
		mapRender = new MapRender(this);
		worldAccess = new WorldAccess(mapRender);
		config = new Config();
		loadConfig();
		
		tileColors = new TileColors(this);
		tileColors.createTileColors();
		blockRenderManager = new BlockRenderManager(tileColors);
		guiUtils = new GuiUtilsImpl(mc);
		
		updateStyle();
	}
	
	public void loadConfig() {
		log("Loading Configuration...");

		File configFolder = getConfigFolder();
		
		config.read(new File(configFolder, "config.txt"));
		config.mapConfig.read(new File(configFolder, "map.txt"));
		
		log("Done!");
	}
	
	public void saveConfig() {
		log("Saving Configuration...");
		
		File configFolder = getConfigFolder();
		configFolder.mkdirs();

		config.write(new File(configFolder, "config.txt"));
		config.mapConfig.write(new File(configFolder, "map.txt"));
		
		log("Done!");
	}
	
	public void updateStyle() {
		mapRender.setStyle(getMapStyleFromConfig());		
	}
	
	public MapStyle getMapStyleFromConfig() {
		boolean round = config.mapConfig.roundMap.value;
		int style = config.mapStyle.value;
		
		if(style == 1) return new MapStyleGenerated(round, 0xc22020); // Red
		if(style == 2) return new MapStyleGenerated(round, 0x22b422); // Green
		if(style == 3) return new MapStyleInternal("/rei/", round);
		if(style == 4) return new MapStyleInternal("/zan/", round);
		
		return new MapStyleGenerated(round, 0x0f39ae); // Blue
	}
	
	public File getConfigFolder() {
		return new File(minecraftHelper.getMinecraftDir(), "minimap");
	}
	
	public void onTick() {
		
	}
	
	public void onReload() {
		tileColors.createTileColors();
	}
	
	public void onRenderGui(float partialTicks) {
		World newWorld = mc.theWorld;
		if(newWorld != theWorld) {
			onWorldChange(newWorld);
		}
		
		if(theWorld == null) {
			return;
		}
		
		if(!guiUtils.isGuiOpened()) {
			updateInput();
		}
		
		boolean unlocked = false;
		if(config.requireItem.value == 0) unlocked = true;
		if(config.requireItem.value == 1) unlocked = minecraftHelper.doesPlayerHaveItem(Item.toolCompass);
		if(config.requireItem.value == 2) unlocked = minecraftHelper.doesPlayerHaveItem(Item.map);
		
		if(config.mapVisible.value && unlocked && minecraftHelper.isGuiVisible() && !minecraftHelper.isDebugScreenOpened() && (!guiUtils.isGuiOpened() || guiUtils.isMinimapGuiOpened() || minecraftHelper.isChatOpened())) {
			mapRender.renderMap(partialTicks);
		}
		
		GuiScreen screen = guiUtils.getCurrentScreen();
		if(screen != null) {
			glDisable(GL_DEPTH_TEST);
			glDisable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(770, 771);
			screen.draw(partialTicks);
		}
	}
	
	public void updateInput() {
		for(int i=0; i < config.keyBinds.length; i++) {
			Keybind key = config.keyBinds[i];
			
			boolean pressed = Keyboard.isKeyDown(key.value);
			
			if(pressed != key.press) {
				key.press = pressed;
				keyEvent(key, pressed);
			}
		}
	}
	
	public void keyEvent(Keybind keybind, boolean press) {
		if(press) {
			if(keybind == config.keyMap) guiUtils.displayGui(new GuiConfigGeneral(null));
			if(keybind == config.keyHideMap) config.mapVisible.value = !config.mapVisible.value;
			
			if(config.mapVisible.value) {
				MapConfig mapConfig = config.mapConfig;
				if(keybind == config.keyFullscreen) mapConfig.fullscreenMap.value = !mapConfig.fullscreenMap.value;
				
				if(mapConfig.fullscreenMap.value) {
					if(keybind == config.keyZoomIn && mapConfig.fullscreenZoomLevel.value < 4) mapConfig.fullscreenZoomLevel.value = mapConfig.fullscreenZoomLevel.value + 1;
					if(keybind == config.keyZoomOut && mapConfig.fullscreenZoomLevel.value > 0) mapConfig.fullscreenZoomLevel.value = mapConfig.fullscreenZoomLevel.value - 1;
				}else {
					if(keybind == config.keyZoomIn && mapConfig.zoomLevel.value < 4) mapConfig.zoomLevel.value = mapConfig.zoomLevel.value + 1;
					if(keybind == config.keyZoomOut && mapConfig.zoomLevel.value > 0) mapConfig.zoomLevel.value = mapConfig.zoomLevel.value - 1;
				}
			}
		}
	}
	
	public void onWorldChange(World newWorld) {
		log("World Changed!");
		
		if(theWorld != null) {
			minecraftHelper.removeWorldAccess(theWorld, worldAccess);
		}
		
		this.theWorld = newWorld;
		
		if(theWorld != null) {
			minecraftHelper.addWorldAccess(theWorld, worldAccess);
			
			mapRender.onWorldChange(newWorld);
		}
		
		saveConfig();
	}
	
	public static void log(String str) {
		System.out.println("[MINIMAP] " + str);
	}
	
}
