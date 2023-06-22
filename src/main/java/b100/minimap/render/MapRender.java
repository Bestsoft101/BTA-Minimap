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
import b100.minimap.render.style.MapStyle;
import b100.minimap.waypoint.Waypoint;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class MapRender implements IWorldListener {
	
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
	
	public MapConfig mapConfig;
	
	public int viewRadius = 16;
	
	public double playerPosX;
	public double playerPosY;
	public double playerPosZ;
	
	public int playerBlockX;
	public int playerBlockZ;
	
	public double mapRotation;
	
	public int playerChunkX;
	public int playerChunkZ;
	
	public int zoom;
	
	public int mapX;
	public int mapY;
	public int mapWidth;
	public int mapHeight;
	
	public MapRender(Minimap minimap) {
		this.minimap = minimap;
		
		mapTileManager = new MapTileManager(minimap, 64);
		mapTileRenderer = new MapTileRenderer(minimap);
		
		maskTexture = minimap.minecraftHelper.generateTexture();
		mapTexture = minimap.minecraftHelper.generateTexture();
		
		glBindTexture(GL_TEXTURE_2D, maskTexture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		int sphereTextureSize = 1024;
		ByteBuffer buffer = ByteBuffer.allocateDirect(sphereTextureSize * sphereTextureSize * 4);
		generateSphereTexture(buffer, sphereTextureSize, sphereTextureSize, 0x0, 0xFFFFFFFF);
		buffer.position(0);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sphereTextureSize, sphereTextureSize, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}
	
	public void setStyle(MapStyle style) {
		setTextureImage(mapTexture, style.getMapTexture(), true, false);
		setTextureImage(maskTexture, style.getMaskTexture(), true, false);
	}
	
	public void renderMap(float partialTicks) {
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_FOG);
		
		glColor3d(1.0, 1.0, 1.0);
		
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, minimap.minecraftHelper.getDisplayWidth(), minimap.minecraftHelper.getDisplayHeight(), 0, -1000, 1000);
		
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		
		EntityPlayer player = minimap.minecraftHelper.getThePlayer();
		
		playerPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		playerPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		playerPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		
		playerChunkX = MathHelper.floor_double(playerPosX) >> 4;
		playerChunkZ = MathHelper.floor_double(playerPosZ) >> 4;
		
		mapRotation = player.rotationYaw;
		
		mapConfig = minimap.config.mapConfig;
		
		updateChunks();
		
		drawMapOnScreen();
		
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
	}
	
	private void drawMapOnScreen() {
		Tessellator tessellator = Tessellator.instance;

		int displayWidth = minimap.minecraftHelper.getDisplayWidth();
		int displayHeight = minimap.minecraftHelper.getDisplayHeight();

		mapWidth = mapConfig.width.value * 16;
		mapHeight = mapConfig.width.value * 16;
		
		final int pad = (int) (displayHeight * minimap.minecraftHelper.getScreenPaddingPercentage());
		
		if(mapConfig.fullscreenMap.value) {
			mapWidth = mapHeight = Math.min(displayWidth - pad * 2, displayHeight - pad * 2);
			zoom = (int) Math.pow(2, mapConfig.fullscreenZoomLevel.value);
			mapX = (displayWidth - mapWidth) / 2;
			mapY = (displayHeight - mapHeight) / 2;
		}else {
			zoom = (int) Math.pow(2, mapConfig.zoomLevel.value);
			
			if(mapConfig.position.value == 1 || mapConfig.position.value == 3) {
				mapX = displayWidth - mapWidth - pad;
			}else {
				mapX = pad;
			}
			if(mapConfig.position.value == 2 || mapConfig.position.value == 3) {
				mapY = displayHeight - mapHeight - pad;
			}else {
				mapY = pad;
			}
		}
		
		int mapCenterX = mapX + mapWidth / 2;
		int mapCenterY = mapY + mapHeight / 2;
		
		int tileSize = 16 * zoom;
		
		/////////////////////////////
		
		glEnable(GL_BLEND);
		glBlendFunc(770, 771);
		
		if(minimap.config.mask.value) {
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_ALWAYS);
			glColorMask(false, false, false, false);
			glDisable(GL_TEXTURE_2D);
			glColor3d(1.0, 1.0, 1.0);
			
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
			
			renderHelper.drawRectangle(tessellator, 0, 0, displayWidth, displayHeight, 0, 0, 0, 0, 32);
			
			tessellator.setColorOpaque_F(0.0f, 0.0f, 1.0f);
			renderHelper.drawRectangle(tessellator, mapX, mapY, mapWidth, mapHeight, 0, 0, 1, 1, -32);
			tessellator.draw();
			
			glDepthFunc(GL_LEQUAL);
			glEnable(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, maskTexture);

			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_F(0.0f, 1.0f, 0.0f);
			renderHelper.drawRectangle(tessellator, mapX, mapY, mapWidth, mapHeight, 0, 0, 1, 1, 64);
			tessellator.draw();
			
			glColorMask(true, true, true, true);
		}
		
		glDisable(GL_TEXTURE_2D);
		glColor4d(0.1, 0.1, 0.1, 0.9);
		tessellator.startDrawingQuads();
		renderHelper.drawRectangle(tessellator, mapX, mapY, mapWidth, mapHeight, 0, 0, 0, 0, 0);
		tessellator.draw();
		
		
		glPushMatrix();
		glTranslated(mapCenterX, mapCenterY, 0);
		
		if(minimap.config.mapConfig.rotateMap.value) {
			glPushMatrix();
			glRotated(-mapRotation + 180.0f, 0, 0, 1);
		}
		
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
			
			if(x0 < wh && z0 < hh && x0 + tileSize > -wh && z0 + tileSize > -hh) {
				int x1 = (int) x0;
				int y1 = (int) z0;
				
				renderTile(tessellator, tile, x1, y1, tileSize, tileSize, 0);
			}
		}
		
		if(startedDrawing) {
			tessellator.draw();
		}
		
		if(minimap.config.mapConfig.rotateMap.value) {
			glPopMatrix();
		}
		
		int tex = minimap.minecraftHelper.getTexture("%blur%/player_arrow.png");
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, tex);
		
		glColor3d(1.0, 0.0, 0.0);

		if(!minimap.config.mapConfig.rotateMap.value) {
			glRotated(mapRotation + 180.0f, 0, 0, 1);
		}
		
		tessellator.startDrawingQuads();
		renderHelper.drawRectangle(tessellator, -8, -8, 16, 16, 0, 0, 1, 1, 80);
		tessellator.draw();
		
		glPopMatrix();

		float frameOpacity = minimap.config.mapConfig.frameOpacity.value / 100.0f;
		if(frameOpacity > 0.0f) {
			glDisable(GL_ALPHA_TEST);
			glBindTexture(GL_TEXTURE_2D, mapTexture);
			glColor4f(1.0f, 1.0f, 1.0f, frameOpacity);
			tessellator.startDrawingQuads();
			renderHelper.drawRectangle(tessellator, mapX, mapY, mapWidth, mapHeight, 0.0f, 0.0f, 1.0f, 1.0f, 64);
			tessellator.draw();
			glEnable(GL_ALPHA_TEST);
		}

		glDisable(GL_DEPTH_TEST);
		
		renderWaypoints(mapCenterX, mapCenterY);
		
		
//		glBindTexture(GL_TEXTURE_2D, mapTileManager.texture);
//		glDisable(GL_TEXTURE_2D);
//		glColor3d(0.0, 0.0, 0.0);
//		tessellator.startDrawingQuads();
//		renderHelper.drawRectangle(tessellator, 1, 1, 514, 514, 0.0f, 0.0f, 1.0f, 1.0f, 0);
//		tessellator.draw();
//		glEnable(GL_TEXTURE_2D);
//		glColor3d(1.0, 1.0, 1.0);
//		tessellator.startDrawingQuads();
//		renderHelper.drawRectangle(tessellator, 2, 2, 512, 512, 0.0f, 0.0f, 1.0f, 1.0f, 0);
//		tessellator.draw();
	}
	
	public void renderWaypoints(int mapCenterX, int mapCenterY) {
		List<Waypoint> waypoints = Minimap.instance.worldData.waypoints;
		if(waypoints.size() == 0) {
			return;
		}
		
		int waypointTex = minimap.minecraftHelper.getTexture("%blur%/waypoint.png");
		int waypointArrowTex = minimap.minecraftHelper.getTexture("%blur%/waypoint_arrow.png");
		
		glBindTexture(GL_TEXTURE_2D, waypointTex);
		int currentTex = waypointTex;
		
		Tessellator tessellator = Tessellator.instance;
		
		for(int i=0; i < waypoints.size(); i++) {
			Waypoint waypoint = waypoints.get(i);
			if(!waypoint.visible) {
				continue;
			}
			
			double offsetX = waypoint.x * zoom - (int) (playerPosX * zoom);
			double offsetZ = waypoint.z * zoom - (int) (playerPosZ * zoom);
			
			Double angle = null;
			
			if(mapConfig.rotateMap.value) {
				double rot = Math.toRadians(mapRotation - 90);
				
				double sin = Math.sin(rot);
				double cos = Math.cos(rot);
				
				double p0xNew = offsetX * sin - offsetZ * cos;
				double p0yNew = offsetX * cos + offsetZ * sin;
				
				offsetX = p0xNew;
				offsetZ = p0yNew;
			}
			
			double x = mapCenterX + offsetX;
			double y = mapCenterY + offsetZ;
			
			boolean isOnMap = true;
			int border = (int) (mapWidth / 24.0f);
			
			if(mapConfig.roundMap.value) {
				double dx = x - mapCenterX;
				double dy = y - mapCenterY;
				double rad = (mapWidth - border) / 2;
				double rad2 = (mapWidth + border) / 2;
				
				double distance = Math.sqrt(dx * dx + dy * dy); 
				isOnMap = distance < rad;
				
				if(!isOnMap) {
					double x1 = x - mapCenterX;
					double y1 = y - mapCenterY;
					
					x1 = (x1 / distance) * rad2;
					y1 = (y1 / distance) * rad2;
					
					x = (int) x1 + mapCenterX;
					y = (int) y1 + mapCenterY;
				}
			}else {
				isOnMap = x >= mapX + border && y >= mapY + border && x < mapX + mapWidth - border && y < mapY + mapHeight - border;
				
				
			}
			
			if(isOnMap) {
				if(currentTex != waypointTex) {
					glBindTexture(GL_TEXTURE_2D, waypointTex);
					currentTex = waypointTex;
				}
			}else {
				angle = Math.atan2(playerBlockZ - waypoint.z, waypoint.x - playerBlockX);
				if(mapConfig.rotateMap.value) {
					angle += Math.toRadians(mapRotation + 180);
				}
				if(currentTex != waypointArrowTex) {
					glBindTexture(GL_TEXTURE_2D, waypointArrowTex);
					currentTex = waypointArrowTex;
				}
			}
			
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(waypoint.color);
			
			if(angle != null) {
				renderHelper.drawRotatedRectangle(tessellator, x - 8, y - 8, 16, 16, 0.0f, 0.0f, 1.0f, 1.0f, 0, angle);
			}else {
				renderHelper.drawRectangle(tessellator, x - 8, y - 8, 16, 16, 0.0f, 0.0f, 1.0f, 1.0f, 64);
			}
			
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
				
				if(!world.checkChunksExist(x0 - 8, 0, z0 - 8, x0 + 24, 0, z0 + 24)) {
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
	
	public void generateSphereTexture(ByteBuffer buffer, int width, int height, int insideColor, int outsideColor) {
		int centerX = width / 2;
		int centerY = height / 2;
		
		for(int i=0; i < height; i++) {
			for(int j=0; j < width; j++) {
				float distance = distance(j, i, centerX, centerY);
				
				int color = insideColor;
				
				if(distance > width / 2 - 1) {
					color = outsideColor;
				}
				
				byte a = (byte) (color >> 24);
				byte r = (byte) (color >> 16);
				byte g = (byte) (color >>  8);
				byte b = (byte) (color >>  0);
				
				buffer.put(b);
				buffer.put(g);
				buffer.put(r);
				buffer.put(a);
			}
		}
	}

}
