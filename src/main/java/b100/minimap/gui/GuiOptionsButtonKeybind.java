package b100.minimap.gui;

import org.lwjgl.input.Keyboard;

import b100.minimap.config.Keybind;

public class GuiOptionsButtonKeybind extends GuiOptionButton<Integer> {

	private boolean selected = false;
	
	public GuiOptionsButtonKeybind(GuiScreen screen, Keybind keybind) {
		super(screen, keybind);
	}
	
	@Override
	public void keyEvent(int key, boolean pressed, boolean repeat, int mouseX, int mouseY) {
		if(selected && pressed) {
			selected = false;
			if(key != Keyboard.KEY_ESCAPE) {
				option.value = key;
				onOptionValueChanged();
			}
			updateText();
			throw new CancelEventException();
		}
		super.keyEvent(key, pressed, repeat, mouseX, mouseY);
	}
	
	@Override
	public void onClick(int button) {
		selected = true;
		
		updateText();
		
		super.onClick(button);
	}
	
	@Override
	public int getColor() {
		if(selected) {
			return hoverColor;
		}else {
			return super.getColor();
		}
	}
	
	@Override
	public void updateText() {
		text = Keyboard.getKeyName(option.value);
	}

}
