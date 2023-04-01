package b100.minimap.mc;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import b100.minimap.gui.CancelEventException;
import b100.minimap.gui.GuiScreen;

public class GuiWrapper extends net.minecraft.src.GuiScreen {
	
	public GuiScreen minimapGui;
	
	public GuiWrapper(GuiScreen minimapGui) {
		this.minimapGui = minimapGui;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		if(!minimapGui.isInitialized()) {
			minimapGui.init();
		}
		
		minimapGui.cursorX = mouseX;
		minimapGui.cursorY = mouseY;

		while(Keyboard.next()) {
			int key = Keyboard.getEventKey();
			boolean repeat = Keyboard.isRepeatEvent();
			boolean pressed = Keyboard.getEventKeyState();
			
			if(key == Keyboard.KEY_F11) {
				if(pressed) {
					mc.toggleFullscreen();
				}
			}else {
				try{
					minimapGui.keyEvent(key, pressed, repeat, mouseX, mouseY);
				}catch (CancelEventException e) {}
			}
		}
		while(Mouse.next()) {
			int button = Mouse.getEventButton();
			boolean pressed = Mouse.getEventButtonState();
			
			if(button >= 0) {
				try{
					minimapGui.mouseEvent(button, pressed, mouseX, mouseY);
				}catch (CancelEventException e) {}
			}
		}
		
		int scroll = Mouse.getDWheel();
		if(scroll != 0) {
			minimapGui.onScroll(scroll, mouseX, mouseY);
		}
		
		int w = mc.resolution.scaledWidth;
		int h = mc.resolution.scaledHeight;
		
		if(w != minimapGui.width || h != minimapGui.height) {
			minimapGui.width = w;
			minimapGui.height = h;
			minimapGui.onResize();
		}
	}
	
	@Override
	public void handleInput() {
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
}
