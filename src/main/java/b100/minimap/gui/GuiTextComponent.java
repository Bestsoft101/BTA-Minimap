package b100.minimap.gui;

import static org.lwjgl.input.Keyboard.KEY_A;
import static org.lwjgl.input.Keyboard.KEY_BACK;
import static org.lwjgl.input.Keyboard.KEY_C;
import static org.lwjgl.input.Keyboard.KEY_DELETE;
import static org.lwjgl.input.Keyboard.KEY_END;
import static org.lwjgl.input.Keyboard.KEY_HOME;
import static org.lwjgl.input.Keyboard.KEY_LCONTROL;
import static org.lwjgl.input.Keyboard.KEY_LEFT;
import static org.lwjgl.input.Keyboard.KEY_LSHIFT;
import static org.lwjgl.input.Keyboard.KEY_RCONTROL;
import static org.lwjgl.input.Keyboard.KEY_RIGHT;
import static org.lwjgl.input.Keyboard.KEY_RSHIFT;
import static org.lwjgl.input.Keyboard.KEY_V;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import b100.minimap.utils.Utils;

public class GuiTextComponent extends GuiLabel {
	
	public int cursorPosition;
	public int textSelection = -1;
	
	public boolean editable = true;
	public boolean focused = false;
	
	private long blinkTime = 0;
	private long blinkDuration = 600;
	
	private List<TextComponentListener> textComponentListeners = new ArrayList<>();
	
	public GuiTextComponent(String string) {
		super(string);
	}
	
	public GuiTextComponent(String string, int posX, int posy) {
		super(string, posX, posy);
	}
	
	@Override
	public void draw(float partialTicks) {
		this.cursorPosition = Utils.clamp(cursorPosition, 0, text.length());
		this.textSelection = Utils.clamp(textSelection, -1, text.length());
		
		glEnable(GL_TEXTURE_2D);
		
		int textColor = getTextColor() | 0xFF000000;
		
		if(!focused || !editable) {
			textSelection = -1;
		}
		
		if(isTextSelected()) {
			int selectionStart = getSelectionStart();
			int selectionEnd = getSelectionEnd();
			
			String str0 = text.substring(0, selectionStart);
			String str1 = text.substring(selectionStart, selectionEnd);
			String str2 = text.substring(selectionEnd);
			
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
			utils.drawString(text, posX, posY, textColor);
		}
		
		if(focused && editable) {
			boolean blink = (System.currentTimeMillis() - blinkTime) % blinkDuration < (blinkDuration / 2);
			if(blink) {
				if(cursorPosition == text.length()) {
					int offset = utils.getStringWidth(text);
					
					utils.drawString("_", posX + offset, posY, textColor);
				}else {
					int offset = utils.getStringWidth(text.substring(0, cursorPosition));
					
					glDisable(GL_TEXTURE_2D);
					utils.drawRectangle(this.posX + offset, posY - 1, 1, 10, textColor);
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
		
		if(control) {
			if(key == KEY_A) {
				textSelection = 0;
				cursorPosition = text.length();
				onUpdate();
				throw new CancelEventException();
			}
			if(key == KEY_C) {
				String stringToCopy = text;
				if(isTextSelected()) {
					stringToCopy = text.substring(getSelectionStart(), getSelectionEnd());
				}
				Utils.copyStringToClipboard(stringToCopy);
				throw new CancelEventException();
			}
			if(key == KEY_V) {
				String clipboardString = Utils.getClipboardString();
				if(clipboardString != null && clipboardString.length() > 0) {
					StringBuilder stringBuilder = new StringBuilder();
					for(int i=0; i < clipboardString.length(); i++) {
						char c1 = clipboardString.charAt(i);
						if(isCharacterAllowed(c1)) {
							stringBuilder.append(c1);
						}
					}
					clipboardString = stringBuilder.toString();
					if(clipboardString.length() > 0) {
						if(isTextSelected()) {
							text = text.substring(0, getSelectionStart()) + clipboardString + text.substring(getSelectionEnd());
						}else {
							text = text.substring(0, cursorPosition) + clipboardString + text.substring(cursorPosition);
						}
						onUpdate();
					}
				}
				throw new CancelEventException();
			}
		}
		if(key == KEY_BACK) {
			if(isTextSelected()) {
				int i = getSelectionStart();
				text = text.substring(0, getSelectionStart()) + text.substring(getSelectionEnd());
				cursorPosition = i;
				textSelection = -1;
			}else if(cursorPosition > 0) {
				if(control) {
					int i = getNextWordStartPosition(cursorPosition, -1);
					text = text.substring(0, i) + text.substring(cursorPosition);
					cursorPosition = i;
				}else {
					text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);	
				}
				cursorPosition--;
				onUpdate();
			}
			throw new CancelEventException();
		}
		if(key == KEY_DELETE) {
			if(isTextSelected()) {
				int i = getSelectionStart();
				text = text.substring(0, getSelectionStart()) + text.substring(getSelectionEnd());
				cursorPosition = i;
				textSelection = -1;
			}else if(cursorPosition < text.length()) {
				if(control) {
					int i = getNextWordStartPosition(cursorPosition, 1);
					text = text.substring(0, cursorPosition) + text.substring(i);	
				}else {
					text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
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
			cursorPosition = text.length();
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
				text = text.substring(0, getSelectionStart()) + c + text.substring(getSelectionEnd());
				cursorPosition = i + 1;
				textSelection = -1;
			}else {
				text = text.substring(0, cursorPosition) + c + text.substring(cursorPosition);
				cursorPosition++;
				textSelection = -1;
			}
			onUpdate();
			throw new CancelEventException();
		}
	}
	
	public void scrollEvent(int direction) {
		
	}
	
	public int getNextWordStartPosition(int start, int dir) {
		dir = Utils.clamp(dir, -1, 1);
		if(dir == 0) {
			throw new IllegalArgumentException(String.valueOf(dir));
		}
		for(int i = start + dir; i >= 0 && i <= text.length(); i += dir) {
			if(i == 0 || i >= text.length()) {
				return i;
			}
			if(text.charAt(i) != ' ' && text.charAt(i - 1) == ' ') {
				return i;
			}
		}
		return dir > 0 ? text.length() : 0;
	}
	
	public void onUpdate() {
		this.cursorPosition = Utils.clamp(cursorPosition, 0, text.length());
		this.textSelection = Utils.clamp(textSelection, -1, text.length());
		
		if(cursorPosition == textSelection) {
			textSelection = -1;
		}
		
		try {
			for(int i=0; i < textComponentListeners.size(); i++) {
				textComponentListeners.get(i).onTextComponentChanged(this);
			}
		}catch (CancelEventException e) {}	
	}
	
	@Override
	public void setText(String string) {
		this.text = string;
		this.cursorPosition = string.length();
		this.textSelection = -1;
		onUpdate();
	}
	
	public boolean isCharacterAllowed(char c) {
		return minecraftHelper.isCharacterAllowed(c);
	}
	
	public void setFocused(boolean focused) {
		if(focused) {
			if(!this.focused) {
				this.blinkTime = System.currentTimeMillis();
				this.cursorPosition = text.length();
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
	
	public GuiTextComponent addTextComponentListener(TextComponentListener textComponentListener) {
		this.textComponentListeners.add(textComponentListener);
		return this;
	}
	
	public GuiTextComponent removeTextComponentListener(TextComponentListener textComponentListener) {
		this.textComponentListeners.remove(textComponentListener);
		return this;
	}
}