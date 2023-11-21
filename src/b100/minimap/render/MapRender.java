package b100.minimap.render;

import static b100.minimap.utils.Utils.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import b100.minimap.Minimap;
import b100.minimap.config.MapConfig;
import b100.minimap.mc.IDimension;
import b100.minimap.mc.Player;
import b100.minimap.render.style.MapStyle;
import b100.minimap.waypoint.Waypoint;
import net.minecraft.client.render.Tessellator;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

public class MapRender implements WorldListener {
	
	private Minimap minimap;
	private World world;
	
	private RenderHelper renderHelper = new RenderHelper();
	
	private List<MapChunk> renderChunksUnused = new ArrayList<>();
	private List<MapChunk> renderChunksUsed = new ArrayList<>();
	private Map<Integer, MapChunk> chunks = new HashMap<>();
	
	public final MapTileManager mapTileManager;
	public final MapTileRenderer mapTileRenderer;
	
	public final int maskTexture;
	public final int mapTexture;
	
	private ByteBuffer colorBuffer = ByteBuffer.allocateDirect(16 * 16 * 4).order(ByteOrder.nativeOrder());
	private IntBuffer colorBufferInt = colorBuffer.asIntBuffer();
	
	///////////////////////////////
	
	public Tessellator tessellator;
	public MapConfig mapConfig;
	
	public int viewRadius = 16;
	
	public double playerPosX;
	public double playerPosY;
	public double playerPosZ;
	
	public int playerBlockX;
	public int playerBlockZ;
	
	public double playerRotation;
	
	public int playerChunkX;
	public int playerChunkZ;
	
	public int zoom;
	
	public int mapPosX;
	public int mapPosY;
	public int mapWidth;
	public int mapHeight;
	public int mapCenterX;
	public int mapCenterY;
	
	public int tileSize;
	public int iconSize = 16;
	
	public boolean roundMap;
	public boolean enableMask;
	public boolean enableMaskTexture;
	
	public float frameOpacity;
	public boolean rotateFrame;
	
	public MapRender(Minimap minimap) {
		this.minimap = minimap;
		
		mapTileManager = new MapTileManager(minimap, 64);
		mapTileRenderer = new MapTileRenderer(minimap);
		
		maskTexture = minimap.minecraftHelper.generateTexture();
		mapTexture = minimap.minecraftHelper.generateTexture();
	}
	
	public void setStyle(MapStyle style) {
		setTextureImage(mapTexture, style.getMapTexture(), style.useLinearFiltering(), false);
		setTextureImage(maskTexture, style.getMaskTexture(), style.useLinearFiltering(), false);
		style.closeStreams();
	}
	
	public void renderMap(float partialTicks) {
		Player player = minimap.minecraftHelper.getThePlayer();
		
		playerPosX = player.getPosX(partialTicks);
		playerPosY = player.getPosY(partialTicks);
		playerPosZ = player.getPosZ(partialTicks);
		
		playerChunkX = MathHelper.floor_double(playerPosX) >> 4;
		playerChunkZ = MathHelper.floor_double(playerPosZ) >> 4;
		
		playerRotation = player.getRotationYaw();
		
		mapConfig = minimap.config.mapConfig;

		viewRadius = 16;
		
		updateChunks();
		
		drawMapOnScreen();
	}
	
	private void drawMapOnScreen() {
		tessellator = Tessellator.instance;

		int displayWidth = minimap.minecraftHelper.getDisplayWidth();
		int displayHeight = minimap.minecraftHelper.getDisplayHeight();

		mapWidth = mapConfig.width.value * 16;
		mapHeight = mapConfig.width.value * 16;
		
		roundMap = mapConfig.roundMap.value;
		enableMask = enableMaskTexture = minimap.config.mask.value;
		frameOpacity = minimap.config.mapConfig.frameOpacity.value / 100.0f;
		rotateFrame = roundMap && minimap.config.mapConfig.rotateFrame.value;
		
		final int pad = iconSize;
		
		if(mapConfig.fullscreenMap.value) {
			// Enhance
			mapWidth = mapHeight = Math.min(displayWidth - pad * 2, displayHeight - pad * 2);
			mapPosX = (displayWidth - mapWidth) / 2;
			mapPosY = (displayHeight - mapHeight) / 2;
			if(mapConfig.fullscreenType.value == 1) {
				roundMap = false;
				frameOpacity = 0.0f;
				enableMaskTexture = false;
			}
			zoom = (int) Math.pow(2, mapConfig.fullscreenZoomLevel.value);
		}else {
			zoom = (int) Math.pow(2, mapConfig.zoomLevel.value);
			int position = mapConfig.position.value;
			if(position == 1 || position == 3) {
				mapPosX = displayWidth - mapWidth - pad;
			}else {
				mapPosX = pad;
			}
			if(position == 2 || position == 3) {
				mapPosY = displayHeight - mapHeight - pad;
			}else {
				mapPosY = pad;
			}
			if(position == 4) {
				mapPosX = (displayWidth - mapWidth) / 2;
				mapPosY = pad;
			}
			if(position == 5) {
				mapPosX = (displayWidth - mapWidth) / 2;
				mapPosY = (displayHeight - mapHeight) / 2;
			}
		}
		
		mapCenterX = mapPosX + mapWidth / 2;
		mapCenterY = mapPosY + mapHeight / 2;
		
		tileSize = 16 * zoom;

		///////////////////////////////
		
		glEnable(GL_BLEND);
		glBlendFunc(770, 771);
		
		if(enableMask) {
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_ALWAYS);
			glColorMask(false, false, false, false);
			glDisable(GL_TEXTURE_2D);
			glColor3d(1.0, 1.0, 1.0);
			
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
			
			renderHelper.drawRectangle(tessellator, 0, 0, displayWidth, displayHeight, 0, 0, 0, 0, 32);
			
			tessellator.setColorOpaque_F(0.0f, 0.0f, 1.0f);
			renderHelper.drawRectangle(tessellator, mapPosX, mapPosY, mapWidth, mapHeight, 0, 0, 1, 1, -32);
			tessellator.draw();
			
			glDepthFunc(GL_LEQUAL);
			if(enableMaskTexture) {
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, maskTexture);
				tessellator.startDrawingQuads();
				tessellator.setColorOpaque_F(0.0f, 1.0f, 0.0f);
				renderHelper.drawRectangle(tessellator, mapPosX, mapPosY, mapWidth, mapHeight, 0, 0, 1, 1, 64);
				tessellator.draw();	
			}
			
			glColorMask(true, true, true, true);
		}
		
		glDisable(GL_TEXTURE_2D);
		glColor4d(0.1, 0.1, 0.1, 0.9);
		tessellator.startDrawingQuads();
		renderHelper.drawRectangle(tessellator, mapPosX, mapPosY, mapWidth, mapHeight, 0, 0, 0, 0, 0);
		tessellator.draw();
		
		glPushMatrix();
		glTranslated(mapCenterX, mapCenterY, 0);
		
		boolean rotate = minimap.config.mapConfig.rotateMap.value; 
		if(rotate) {
			glPushMatrix();
			glRotated(-playerRotation + 180.0f, 0, 0, 1);
		}
		
		renderMapTiles();
		
		if(rotate) {
			glPopMatrix();
		}
		
		glPopMatrix();

		if(frameOpacity > 0.0f) {
			glDisable(GL_ALPHA_TEST);
			glBindTexture(GL_TEXTURE_2D, mapTexture);
			glColor4f(1.0f, 1.0f, 1.0f, frameOpacity);
			tessellator.startDrawingQuads();
			if(rotateFrame) {
				renderHelper.drawRotatedRectangle(tessellator, mapPosX, mapPosY, mapWidth, mapHeight, 0.0f, 0.0f, 1.0f, 1.0f, 64, Math.toRadians(playerRotation));
			}else {
				renderHelper.drawRectangle(tessellator, mapPosX, mapPosY, mapWidth, mapHeight, 0.0f, 0.0f, 1.0f, 1.0f, 64);
			}
			
			tessellator.draw();
			glEnable(GL_ALPHA_TEST);
		}

		glDisable(GL_DEPTH_TEST);

		renderWaypoints();
		renderPlayerArrow();
		
		if(minimap.config.showTiles.value) {
			glBindTexture(GL_TEXTURE_2D, mapTileManager.texture);
			glDisable(GL_TEXTURE_2D);
			glColor3d(0.0, 0.0,0.0);
			tessellator.startDrawingQuads();
			renderHelper.drawRectangle(tessellator, 0, 0, 512, 512, 0.0f, 0.0f, 1.0f, 1.0f, 0);
			tessellator.draw();	
			glEnable(GL_TEXTURE_2D);
			glColor3d(1.0, 1.0, 1.0);
			tessellator.startDrawingQuads();
			renderHelper.drawRectangle(tessellator, 0, 0, 512, 512, 0.0f, 0.0f, 1.0f, 1.0f, 0);
			tessellator.draw();	
		}
	}
	
	public void renderMapTiles() {
		glColor3d(1.0, 1.0, 1.0);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, mapTileManager.texture);
		
		boolean startedDrawing = false;
		
		playerBlockX = MathHelper.floor_double(playerPosX);
		playerBlockZ = MathHelper.floor_double(playerPosZ);

		int wh = mapWidth / 2;
		int hh = mapHeight / 2;
		
		for(int i=0; i < renderChunksUsed.size(); i++) {
			MapChunk mapChunk = renderChunksUsed.get(i);

			int distance = Math.max(Math.abs(playerChunkX - mapChunk.getPosX()), Math.abs(playerChunkZ - mapChunk.getPosZ()));
			if(distance > viewRadius + 2) {
				renderChunksUsed.remove(i--);
				setChunkNotInUse(mapChunk);
				continue;
			}
			
			int tile = mapChunk.tile;
			if(tile == -1) {
				continue;
			}
			
			if(!startedDrawing) {
				startedDrawing = true;
				tessellator.startDrawingQuads();
			}
			
			int x0 = mapChunk.getPosX() * 16 * zoom - (int) (playerPosX * zoom);
			int z0 = mapChunk.getPosZ() * 16 * zoom - (int) (playerPosZ * zoom);
			
			int extend = 0;
			if(mapConfig.rotateMap.value && !roundMap) {
				extend = 48;
			}
			
			if(x0 < wh + extend && z0 < hh + extend && x0 + tileSize > -wh - extend && z0 + tileSize > -hh - extend) {
				int x1 = (int) x0;
				int y1 = (int) z0;
				
				renderTile(tessellator, tile, x1, y1, tileSize, tileSize, 0);
			}
		}
		
		if(startedDrawing) {
			tessellator.draw();
		}
	}
	
	public void renderPlayerArrow() {
		int tex = minimap.minecraftHelper.getTexture("%blur%/player_arrow.png");
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, tex);
		glColor3d(1.0, 0.0, 0.0);
		
		Double angle = null;
		if(!mapConfig.rotateMap.value) {
			angle = Math.toRadians(-playerRotation - 90);
		}
		
		tessellator.startDrawingQuads();
		renderHelper.drawIcon(tessellator, mapCenterX, mapCenterY, iconSize, 0, angle);
		tessellator.draw();
	}
	
	public void renderWaypoints() {
		if(!mapConfig.showWaypoints.value) {
			return;
		}
		
		List<Waypoint> waypoints = Minimap.instance.worldData.getWaypoints();
		if(waypoints.size() == 0) {
			return;
		}
		
		int waypointTex = minimap.minecraftHelper.getTexture("%blur%/waypoint.png");
		int waypointArrowTex = minimap.minecraftHelper.getTexture("%blur%/waypoint_arrow.png");
		
		glBindTexture(GL_TEXTURE_2D, waypointTex);
		int currentTex = waypointTex;
		
		IDimension currentDimension = minimap.worldData.dimension;
		for(int i=0; i < waypoints.size(); i++) {
			Waypoint waypoint = waypoints.get(i);
			if(!waypoint.visible || waypoint.dimension != currentDimension) {
				continue;
			}
			
			// Casted to int match map exactly
			double offsetX = (waypoint.x + 0.5) * zoom - (int) (playerPosX * zoom);
			double offsetZ = (waypoint.z + 0.5) * zoom - (int) (playerPosZ * zoom);
			
			// Used to render waypoints that are outside the visible map, no int cast for very smooth movement
			double offsetXSmooth = waypoint.x - playerPosX;
			double offsetZSmooth = waypoint.z - playerPosZ;
			
			Double angle = null;
			
			if(mapConfig.rotateMap.value) {
				double rot = Math.toRadians(playerRotation - 90);
				
				double sin = Math.sin(rot);
				double cos = Math.cos(rot);
				
				double p0xNew = offsetX * sin - offsetZ * cos;
				double p0yNew = offsetX * cos + offsetZ * sin;
				
				offsetX = p0xNew;
				offsetZ = p0yNew;
				
				p0xNew = offsetXSmooth * sin - offsetZSmooth * cos;
				p0yNew = offsetXSmooth * cos + offsetZSmooth * sin;
				
				offsetXSmooth = p0xNew;
				offsetZSmooth = p0yNew;
			}
			
			double x = mapCenterX + offsetX;
			double y = mapCenterY + offsetZ;
			
			boolean isOnMap = true;
			int border = (int) (mapWidth / 24.0f);
			
			if(roundMap) {
				double dx = x - mapCenterX;
				double dy = y - mapCenterY;
				double rad = (mapWidth - border) / 2;
				double rad2 = (mapWidth + iconSize / 2) / 2;
				
				double distance = Math.sqrt(dx * dx + dy * dy); 
				isOnMap = distance < rad;
				
				if(!isOnMap) {
					distance = Math.sqrt(offsetXSmooth * offsetXSmooth + offsetZSmooth * offsetZSmooth);
					
					offsetXSmooth = (offsetXSmooth / distance) * rad2;
					offsetZSmooth = (offsetZSmooth / distance) * rad2;
					
					x = offsetXSmooth + mapCenterX;
					y = offsetZSmooth + mapCenterY;
				}
			}else {
				isOnMap = x >= mapPosX + border && y >= mapPosY + border && x < mapPosX + mapWidth - border && y < mapPosY + mapHeight - border;
				
				if(!isOnMap) {
					double offXAbs = Math.abs(offsetXSmooth);
					double offZAbs = Math.abs(offsetZSmooth);
					
					if(offZAbs > offXAbs) {
						x = (offsetXSmooth / offZAbs) * (mapWidth / 2) + mapCenterX;
						y = (offsetZSmooth / offZAbs) * (mapWidth / 2) + mapCenterY;
					}else {
						x = (offsetXSmooth / offXAbs) * (mapWidth / 2) + mapCenterX;
						y = (offsetZSmooth / offXAbs) * (mapWidth / 2) + mapCenterY;
					}
				}
			}
			
			if(isOnMap) {
				if(currentTex != waypointTex) {
					glBindTexture(GL_TEXTURE_2D, waypointTex);
					currentTex = waypointTex;
				}
			}else {
				angle = Math.atan2(-offsetZSmooth, offsetXSmooth);
				if(currentTex != waypointArrowTex) {
					glBindTexture(GL_TEXTURE_2D, waypointArrowTex);
					currentTex = waypointArrowTex;
				}
			}
			
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(waypoint.color);
			renderHelper.drawIcon(tessellator, x, y, iconSize, 0, angle);
			tessellator.draw();
		}
	}
	
	public void setChunkNotInUse(MapChunk chunk) {
		if(chunk.tile != -1) {
			mapTileManager.setTileNotInUse(chunk.tile);
			chunk.tile = -1;
		}
		chunks.remove(chunk.getIndex());
		renderChunksUnused.add(chunk);
	}
	
	private void renderTile(Tessellator tessellator, int tile, int x, int y, int width, int height, int zLevel) {
		int tileX = tile % 64;
		int tileY = tile / 64;
		
		float w = 1.0f / 64.0f;

		float u0 = tileX / 64.0f;
		float v0 = tileY / 64.0f;
		
		float u1 = u0 + w;
		float v1 = v0 + w;
		
		renderHelper.drawRectangleInt(tessellator, x, y, width, height, u0, v0, u1, v1, zLevel);
	}
	
	private void updateChunks() {
		int updates = 0;
		int max = minimap.config.updateSpeed.value;
		
		for(int i = -viewRadius; i <= viewRadius; i++) {
			for(int j = -viewRadius; j <= viewRadius; j++) {
				int chunkX = playerChunkX + i;
				int chunkZ = playerChunkZ + j;
				int index = getChunkIndex(chunkX, chunkZ);
				
				int x0 = chunkX << 4;
				int z0 = chunkZ << 4;
				
				MapChunk chunk = chunks.get(index);
				if(chunk != null && !chunk.needsUpdate) {
					continue;
				}
				
				if(!world.areBlocksLoaded(x0 - 8, 0, z0 - 8, x0 + 24, 0, z0 + 24)) {
					continue;
				}
				
				if(chunk == null) {
					chunk = getNewMapChunk(chunkX, chunkZ);
					chunks.put(chunk.getIndex(), chunk);
					renderChunksUsed.add(chunk);
				}
				
				int tile = chunk.tile;
				if(tile == -1) {
					tile = mapTileManager.getEmptyTile();
					if(tile == -1) {
						return;
					}
					chunk.tile = tile;
				}
				
				chunk.needsUpdate = false;
				
				boolean rendered = mapTileRenderer.render(colorBufferInt, chunkX, chunkZ, 16, 16);
				if(rendered) {
					colorBuffer.position(0);
					mapTileManager.setTile(tile, colorBuffer);
				}else {
					mapTileManager.setTileNotInUse(chunk.tile);
					chunk.tile = -1;
				}
				
				updates++;
				if(updates >= max) {
					return;
				}
			}
		}
	}
	
	public static int getChunkIndex(int posX, int posZ) {
		return (posZ & 0xFFFF) << 16 | (posX & 0xFFFF);
	}
	
	private MapChunk getNewMapChunk(int posX, int posZ) {
		if(renderChunksUnused.size() > 0) {
			return renderChunksUnused.remove(0).setPosition(posX, posZ);
		}
		return new MapChunk().setPosition(posX, posZ);
	}
	
	public void updateBlocks(int x0, int y0, int z0, int x1, int y1, int z1) {
		int chunkX0 = x0 >> 4;
		int chunkZ0 = z0 >> 4;
		int chunkX1 = x1 >> 4;
		int chunkZ1 = z1 >> 4;
		
		for(int chunkX = chunkX0; chunkX <= chunkX1; chunkX++) {
			for(int chunkZ = chunkZ0; chunkZ <= chunkZ1; chunkZ++) {
				int index = getChunkIndex(chunkX, chunkZ);
				
				MapChunk chunk = chunks.get(index);
				
				if(chunk != null) {
					chunk.needsUpdate = true;
				}
			}
		}
	}
	
	public void updateAllTiles() {
		for(int i=0; i < renderChunksUsed.size(); i++) {
			renderChunksUsed.get(i).needsUpdate = true;
		}
	}

	@Override
	public void onUpdateBlock(int x, int y, int z) {
		updateBlocks(x-1, y-1, z-1, x+1, y+1, z+1);
	}

	@Override
	public void onUpdateBlocks(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		updateBlocks(minX-1, minY-1, minZ-1, maxX+1, maxY+1, maxZ+1);
	}

	@Override
	public void onUpdateAllChunks() {
		updateAllTiles();
	}
	
	public void onWorldChange(World world) {
		mapTileRenderer.onWorldChanged(world);
		
		this.world = world;
		
		while(renderChunksUsed.size() > 0) {
			setChunkNotInUse(renderChunksUsed.remove(0));
		}
	}

}
