package b100.minimap.gui;

import org.lwjgl.input.Keyboard;

public abstract class GuiScreen extends GuiContainer {
	
	private GuiScreen parentScreen;
	
	public int width;
	public int height;
	
	public int cursorX;
	public int cursorY;
	
	private boolean initialized = false;
	
	public GuiScreen(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}
	
	public final void init() {
		onInit();
		
		initialized = true;
	}
	
	@Override
	public void keyEvent(int key, char c, boolean pressed, boolean repeat, int mouseX, int mouseY) {
		super.keyEvent(key, c, pressed, repeat, mouseX, mouseY);
		if(pressed) {
			if(key == Keyboard.KEY_ESCAPE) {
				close();
				throw new CancelEventException();
			}
			if(key == Keyboard.KEY_BACK) {
				back();
				throw new CancelEventException();
			}	
		}
	}
	
	public abstract void onInit();
	
	public void onResize() {
		
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void back() {
		utils.displayGui(parentScreen);
	}
	
	public void close() {
		utils.displayGui(null);
	}

}
