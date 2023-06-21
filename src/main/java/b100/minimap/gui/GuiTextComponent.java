package b100.minimap.gui;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import b100.minimap.utils.Utils;

public class GuiTextComponent extends GuiLabel {
	
	public int cursorPosition;
	public int textSelection = -1;
	
	public boolean editable = true;
	public boolean focused = false;
	
	private long blinkTime = 0;
	private long blinkDuration = 600;
	
	public GuiTextComponent(String string) {
		super(string);
	}
	
	public GuiTextComponent(String string, int posX, int posy) {
		super(string, posX, posy);
	}
	
	@Override
	public void draw(float partialTicks) {
		this.cursorPosition = Utils.clamp(cursorPosition, 0, string.length());
		this.textSelection = Utils.clamp(textSelection, -1, string.length());
		
		glEnable(GL_TEXTURE_2D);
		
		int textColor = getTextColor();
		
		if(!focused || !editable) {
			textSelection = -1;
		}
		
		if(isTextSelected()) {
			int selectionStart = getSelectionStart();
			int selectionEnd = getSelectionEnd();
			
			String str0 = string.substring(0, selectionStart);
			String str1 = string.substring(selectionStart, selectionEnd);
			String str2 = string.substring(selectionEnd);
			
			int offset0 = utils.getStringWidth(str0);
			int offset1 = utils.getStringWidth(str1);
			
			utils.drawString(str0, posX, posY, textColor);
			utils.drawString(str1, posX + offset0, posY, 0xFFFF00);
			utils.drawString(str2, posX + offset0 + offset1, posY, textColor);
			
			glDisable(GL_TEXTURE_2D);
			glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR);
			
			utils.drawRectangle(posX + offset0, posY - 1, offset1, 10, 0xFFFFFFFF);
			
			glEnable(GL_TEXTURE_2D);
			glBlendFunc(770, 771);
		}else {
			utils.drawString(string, posX, posY, textColor);
		}
		
		if(focused && editable) {
			boolean blink = (System.currentTimeMillis() - blinkTime) % blinkDuration < (blinkDuration / 2);
			if(blink) {
				if(cursorPosition == string.length()) {
					int offset = utils.getStringWidth(string);
					
					utils.drawString("_", posX + offset, posY, 0xFFFFFFFF);
				}else {
					int offset = utils.getStringWidth(string.substring(0, cursorPosition));
					
					glDisable(GL_TEXTURE_2D);
					utils.drawRectangle(this.posX + offset, posY - 1, 1, 10, 0xFFFFFFFF);
				}
			}
		}
	}
	
	public void keyEvent(int key, char c, boolean pressed) {
		if(!editable || !focused || !pressed) {
			return;
		}

		boolean control = Keyboard.isKeyDown(KEY_LCONTROL) || Keyboard.isKeyDown(KEY_RCONTROL);
		boolean shift = Keyboard.isKeyDown(KEY_LSHIFT) || Keyboard.isKeyDown(KEY_RSHIFT);

		if(!isTextSelected() && shift) textSelection = cursorPosition;
		
		if(key == KEY_BACK) {
			if(isTextSelected()) {
				int i = getSelectionStart();
				string = string.substring(0, getSelectionStart()) + string.substring(getSelectionEnd());
				cursorPosition = i;
				textSelection = -1;
			}else if(cursorPosition > 0) {
				if(control) {
					int i = getNextWordStartPosition(cursorPosition, -1);
					string = string.substring(0, i) + string.substring(cursorPosition);
					cursorPosition = i;
				}else {
					string = string.substring(0, cursorPosition - 1) + string.substring(cursorPosition);	
				}
				cursorPosition--;
				onUpdate();
			}
			throw new CancelEventException();
		}
		if(key == KEY_DELETE) {
			if(isTextSelected()) {
				int i = getSelectionStart();
				string = string.substring(0, getSelectionStart()) + string.substring(getSelectionEnd());
				cursorPosition = i;
				textSelection = -1;
			}else if(cursorPosition < string.length()) {
				if(control) {
					int i = getNextWordStartPosition(cursorPosition, 1);
					string = string.substring(0, cursorPosition) + string.substring(i);	
				}else {
					string = string.substring(0, cursorPosition) + string.substring(cursorPosition + 1);
					onUpdate();	
				}
			}
			throw new CancelEventException();
		}
		if(key == KEY_HOME) {
			cursorPosition = 0;
			onUpdate();
			if(!shift) textSelection = -1;
			throw new CancelEventException();
		}
		if(key == KEY_END) {
			cursorPosition = string.length();
			onUpdate();
			if(!shift) textSelection = -1;
			throw new CancelEventException();
		}
		if(key == KEY_LEFT) {
			if(control) {
				cursorPosition = getNextWordStartPosition(cursorPosition, -1);
			}else {
				cursorPosition--;
			}
			if(!shift) textSelection = -1;
			onUpdate();
			throw new CancelEventException();
		}
		if(key == KEY_RIGHT) {
			if(control) {
				cursorPosition = getNextWordStartPosition(cursorPosition, 1);
			}else {
				cursorPosition++;	
			}
			if(!shift) textSelection = -1;
			onUpdate();
			throw new CancelEventException();
		}
		if(isCharacterAllowed(c)) {
			if(isTextSelected() && textSelection != cursorPosition) {
				int i = getSelectionStart();
				string = string.substring(0, getSelectionStart()) + c + string.substring(getSelectionEnd());
				cursorPosition = i;
				textSelection = -1;
			}else {
				string = string.substring(0, cursorPosition) + c + string.substring(cursorPosition);
				cursorPosition++;
				textSelection = -1;
			}
			onUpdate();
			throw new CancelEventException();
		}
	}
	
	public int getNextWordStartPosition(int start, int dir) {
		dir = Utils.clamp(dir, -1, 1);
		if(dir == 0) {
			throw new IllegalArgumentException(String.valueOf(dir));
		}
		for(int i = start + dir; i >= 0 && i <= string.length(); i += dir) {
			if(i == 0 || i >= string.length()) {
				return i;
			}
			if(string.charAt(i) != ' ' && string.charAt(i - 1) == ' ') {
				return i;
			}
		}
		return dir > 0 ? string.length() : 0;
	}
	
	public void onUpdate() {
		this.cursorPosition = Utils.clamp(cursorPosition, 0, string.length());
		this.textSelection = Utils.clamp(textSelection, -1, string.length());
		
		if(cursorPosition == textSelection) {
			textSelection = -1;
		}
	}
	
	@Override
	public void setString(String string) {
		this.string = string;
		this.cursorPosition = string.length();
		this.textSelection = -1;
	}
	
	public boolean isCharacterAllowed(char c) {
		return minecraftHelper.isCharacterAllowed(c);
	}
	
	public void setFocused(boolean focused) {
		if(focused) {
			if(!this.focused) {
				this.blinkTime = System.currentTimeMillis();
				this.cursorPosition = string.length();
			}
			this.focused = true;
		}else {
			this.focused = false;
		}
	}
	
	public boolean isTextSelected() {
		return textSelection != -1;
	}
	
	public int getSelectionStart() {
		if(!isTextSelected()) {
			return -1;
		}
		return Math.min(cursorPosition, textSelection);
	}
	
	public int getSelectionEnd() {
		if(!isTextSelected()) {
			return -1;
		}
		return Math.max(cursorPosition, textSelection);
	}
}
