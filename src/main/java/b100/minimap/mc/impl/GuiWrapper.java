package b100.minimap.mc.impl;

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
		if(minimapGui.isInitialized()) {
			minimapGui.cursorX = mouseX;
			minimapGui.cursorY = mouseY;

			while(Keyboard.next()) {
				int key = Keyboard.getEventKey();
				boolean repeat = Keyboard.isRepeatEvent();
				boolean pressed = Keyboard.getEventKeyState();
				char c = Keyboard.getEventCharacter();
				
				if(key == Keyboard.KEY_F11) {
					if(pressed) {
						mc.toggleFullscreen();
					}
				}else {
					try{
						minimapGui.keyEvent(key, c, pressed, repeat, mouseX, mouseY);
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
				try{
					minimapGui.scrollEvent(scroll, mouseX, mouseY);
				}catch (CancelEventException e) {}
			}
		}
	}
	
	@Override
	public void handleInput() {
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		
		minimapGui.onGuiClosed();
	}
	
	public void onGuiOpened() {
		Keyboard.enableRepeatEvents(true);
		
		minimapGui.onGuiOpened();
	}
	
}
