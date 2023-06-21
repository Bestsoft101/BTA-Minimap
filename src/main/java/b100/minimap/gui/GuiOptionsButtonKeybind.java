package b100.minimap.gui;

import org.lwjgl.input.Keyboard;

import b100.minimap.config.Keybind;

public class GuiOptionsButtonKeybind extends GuiOptionButton<Integer> {

	private boolean selected = false;
	
	public GuiOptionsButtonKeybind(GuiScreen screen, Keybind keybind) {
		super(screen, keybind);
	}
	
	@Override
	public void keyEvent(int key, char c, boolean pressed, boolean repeat, int mouseX, int mouseY) {
		if(selected && pressed) {
			selected = false;
			if(key != Keyboard.KEY_ESCAPE) {
				option.value = key;
				onOptionValueChanged();
			}
			updateText();
			throw new CancelEventException();
		}
		super.keyEvent(key, c, pressed, repeat, mouseX, mouseY);
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
			return Colors.buttonBackgroundHover;
		}else {
			return super.getColor();
		}
	}
	
	@Override
	public void updateText() {
		text = Keyboard.getKeyName(option.value);
	}

}
