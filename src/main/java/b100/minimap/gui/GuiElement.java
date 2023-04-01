package b100.minimap.gui;

import b100.minimap.Minimap;
import b100.minimap.mc.IMinecraftHelper;

public abstract class GuiElement {

	public Minimap minimap = Minimap.instance;
	public IGuiUtils utils = minimap.guiUtils;
	public IMinecraftHelper minecraftHelper = minimap.minecraftHelper;
	
	public int posX;
	public int posY;
	public int width;
	public int height;
	
	public GuiElement() {
		
	}
	
	public abstract void draw(float partialTicks);
	
	public void keyEvent(int key, boolean pressed, boolean repeat, int mouseX, int mouseY) {
		
	}
	
	public void mouseEvent(int button, boolean pressed, int mouseX, int mouseY) {
		
	}
	
	public void onScroll(int dir, int mouseX, int mouseY) {
		
	}
	
	public boolean isSolid() {
		return true;
	}
	
	public GuiElement setPositionAndSize(int x, int y, int w, int h) {
		this.posX = x;
		this.posY = y;
		this.width = w;
		this.height = h;
		return this;
	}
	
	public GuiElement setPosition(int x, int y) {
		this.posX = x;
		this.posY = y;
		return this;
	}
	
	public GuiElement setSize(int w, int h) {
		this.width = w;
		this.height = h;
		return this;
	}
	
	public boolean isInside(int x, int y) {
		return x >= posX && y >= posY && x < posX + width && y < posY + height;
	}
	
}
