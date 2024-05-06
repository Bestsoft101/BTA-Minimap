package b100.minimap.mc.impl;

import static org.lwjgl.opengl.GL11.*;

import b100.minimap.gui.GuiScreen;
import b100.minimap.gui.IGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.sound.SoundCategory;

public class GuiUtilsImpl implements IGuiUtils {

	public Minecraft mc;
	
	public GuiUtilsImpl(Minecraft mc) {
		this.mc = mc;
	}
	
	@Override
	public void drawString(String string, int x, int y, int color) {
		mc.fontRenderer.drawStringWithShadow(string, x, y, color);
	}

	@Override
	public void drawCenteredString(String string, int x, int y, int color) {
		mc.fontRenderer.drawCenteredString(string, x, y, color);
	}

	@Override
	public int getStringWidth(String string) {
		return mc.fontRenderer.getStringWidth(string);
	}

	@Override
	public void drawRectangle(int x, int y, int w, int h, int color) {
		Tessellator tessellator = Tessellator.instance;
		int a = (color >> 24) & 0xFF;
		int r = (color >> 16) & 0xFF;
		int g = (color >>  8) & 0xFF;
		int b = (color >>  0) & 0xFF;
		int x0 = x;
		int y0 = y;
		int x1 = x + w;
		int y1 = y + h;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(r, g, b, a);
		tessellator.addVertex(x0, y0, 0);
		tessellator.addVertex(x0, y1, 0);
		tessellator.addVertex(x1, y1, 0);
		tessellator.addVertex(x1, y0, 0);
		tessellator.draw();
	}

	@Override
	public void playButtonSound() {
		mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0f, 1.0f);
	}

	@Override
	public boolean isGuiOpened() {
		return mc.currentScreen != null;
	}

	@Override
	public boolean isMinimapGuiOpened() {
		return mc.currentScreen instanceof GuiWrapper;
	}

	@Override
	public void displayGui(GuiScreen screen) {
		if(screen != null) {
			GuiWrapper wrapper = new GuiWrapper(screen);
			mc.displayGuiScreen(wrapper);
			wrapper.onOpened();
		}else {
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public GuiScreen getCurrentScreen() {
		if(mc.currentScreen instanceof GuiWrapper) {
			GuiWrapper guiWrapper = (GuiWrapper) mc.currentScreen;
			return guiWrapper.minimapGui;
		}
		return null;
	}

	@Override
	public void drawIcon(int icon, int x, int y, int color) {
		glBindTexture(GL_TEXTURE_2D, mc.renderEngine.getTexture("/minimap/gui.png"));
		
		int a = color >> 24 & 0xFF;
		int r = color >> 16 & 0xFF;
		int g = color >>  8 & 0xFF;
		int b = color >>  0 & 0xFF;
		
		if(a == 0) {
			a = 255;
		}
		
		int iconX = icon & 3;
		int iconY = icon >> 2;
		
		float u0 = iconX / 4.0f;
		float v0 = iconY / 4.0f;
		
		float u1 = (iconX + 1) / 4.0f;
		float v1 = (iconY + 1) / 4.0f;
		
		int x1 = x + 8;
		int y1 = y + 8;
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(r, g, b, a);
		tessellator.addVertexWithUV(x, y, 0, u0, v0);
		tessellator.addVertexWithUV(x, y1, 0, u0, v1);
		tessellator.addVertexWithUV(x1, y1, 0, u1, v1);
		tessellator.addVertexWithUV(x1, y, 0, u1, v0);
		tessellator.draw();
	}

	@Override
	public void drawIconWithShadow(int icon, int x, int y, int color) {
		int shadowColor = color;
        int alphaChannelOnly = shadowColor & 0xFF000000;
        shadowColor = (shadowColor & 0xFCFCFC) >> 2;
        shadowColor += alphaChannelOnly;
        
        drawIcon(icon, x + 1, y + 1, shadowColor);
        drawIcon(icon, x, y, color);
	}

	@Override
	public void drawTexturedRectangle(int x, int y, int w, int h, float u0, float v0, float u1, float v1, int color) {
		Tessellator tessellator = Tessellator.instance;
		int a = (color >> 24) & 0xFF;
		int r = (color >> 16) & 0xFF;
		int g = (color >>  8) & 0xFF;
		int b = (color >>  0) & 0xFF;
		int x0 = x;
		int y0 = y;
		int x1 = x + w;
		int y1 = y + h;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(r, g, b, a);
		tessellator.addVertexWithUV(x0, y0, 0, u0, v0);
		tessellator.addVertexWithUV(x0, y1, 0, u0, v1);
		tessellator.addVertexWithUV(x1, y1, 0, u1, v1);
		tessellator.addVertexWithUV(x1, y0, 0, u1, v0);
		tessellator.draw();
	}

}
