package b100.minimap.mc;

import b100.minimap.gui.GuiScreen;
import b100.minimap.gui.IGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Tessellator;

public class GuiUtilsImpl implements IGuiUtils {

	public Minecraft mc;
	
	public GuiUtilsImpl(Minecraft mc) {
		this.mc = mc;
	}
	
	@Override
	public void drawString(String string, int x, int y, int color) {
		mc.fontRenderer.drawString(string, x, y, color);
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
		mc.sndManager.playSoundFX("random.click", 1.0f, 1.0f);
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
			mc.displayGuiScreen(new GuiWrapper(screen));
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

}
