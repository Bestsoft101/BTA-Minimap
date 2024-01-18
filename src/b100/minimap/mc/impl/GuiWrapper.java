package b100.minimap.mc.impl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import b100.minimap.gui.CancelEventException;
import b100.minimap.gui.GuiScreen;

public class GuiWrapper extends net.minecraft.client.gui.GuiScreen {
	
	public GuiScreen minimapGui;
	public int mouseX;
	public int mouseY;
	
	/**
	 * Fixes a bug where a character is typed into the focused textbox immediately when opening the create waypoint GUI using the hotkey
	 */
	public boolean skipInput = true;
	
	public GuiWrapper(GuiScreen minimapGui) {
		this.minimapGui = minimapGui;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		
		if(minimapGui.isInitialized()) {
			minimapGui.cursorX = mouseX;
			minimapGui.cursorY = mouseY;

			while(Keyboard.next()) {
				int key = Keyboard.getEventKey();
				boolean repeat = Keyboard.isRepeatEvent();
				boolean pressed = Keyboard.getEventKeyState();
				char c = Keyboard.getEventCharacter();
				handleKeyEvent(key, c, pressed, repeat);
			}
			while(Mouse.next()) {
				int button = Mouse.getEventButton();
				boolean pressed = Mouse.getEventButtonState();
				handleMouseEvent(button, pressed);
			}
			
			handleScrolling(Mouse.getDWheel());
			
			if(skipInput) {
				skipInput = false;
			}
		}
	}
	
	public void handleKeyEvent(int key, char c, boolean pressed, boolean repeat) {
		if(skipInput) {
			return;
		}
		if(key == Keyboard.KEY_F11) {
			if(pressed) {
				mc.gameWindow.toggleFullscreen();
			}
		}else {
			try{
				minimapGui.keyEvent(key, c, pressed, repeat, mouseX, mouseY);
			}catch (CancelEventException e) {}
		}
	}
	
	public void handleMouseEvent(int button, boolean pressed) {
		if(skipInput) {
			return;
		}
		if(button >= 0) {
			try{
				minimapGui.mouseEvent(button, pressed, mouseX, mouseY);
			}catch (CancelEventException e) {}
		}
	}
	
	public void handleScrolling(int scrollAmount) {
		if(skipInput) {
			return;
		}
		if(scrollAmount != 0) {
			try{
				minimapGui.scrollEvent(scrollAmount, mouseX, mouseY);
			}catch (CancelEventException e) {}
		}
	}
	
	@Override
	public void handleInput() {
	}
	
	@Override
	public void onClosed() {
		Keyboard.enableRepeatEvents(false);
		
		minimapGui.onGuiClosed();
	}

	public void onOpened() {
		Keyboard.enableRepeatEvents(true);
		
		minimapGui.onGuiOpened();
	}
	
}
