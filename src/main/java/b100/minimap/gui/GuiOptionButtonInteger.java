package b100.minimap.gui;

import org.lwjgl.input.Keyboard;

import b100.minimap.config.IntegerOption;

public class GuiOptionButtonInteger extends GuiOptionButton<Integer> {

	public IntegerOption integerOption;
	
	public boolean enableScrolling = true;
	
	public GuiOptionButtonInteger(GuiScreen screen, IntegerOption option) {
		super(screen, option);
		
		this.integerOption = option;
	}
	
	@Override
	public void onClick(int button) {
		if(button == 0) update(1, true);
		if(button == 1) update(-1, true);
	}
	
	@Override
	public void onScroll(int dir, int mouseX, int mouseY) {
		if(enableScrolling && screen.getClickElementAt(mouseX, mouseY) == this) {
			int scrollAmount = 0;
			if(dir > 0) scrollAmount = 1;
			if(dir < 0) scrollAmount = -1;
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) scrollAmount *= 10;
			update(scrollAmount, false);
		}
	}
	
	public void update(int delta, boolean loop) {
		option.value += delta;
		
		if(integerOption.minValue == null || integerOption.maxValue == null) {
			loop = false;
		}
		
		if(integerOption.maxValue != null && option.value > integerOption.maxValue) {
			if(loop) {
				option.value = integerOption.minValue;
			}else {
				option.value = integerOption.maxValue;
			}
			
		}
		if(integerOption.minValue != null && option.value < integerOption.minValue) {
			if(loop) {
				option.value = integerOption.maxValue;
			}else {
				option.value = integerOption.minValue;
			}
			
		}
		
		onOptionValueChanged();
	}
	
	public GuiOptionButtonInteger setScrollingEnabled(boolean enableScrolling) {
		this.enableScrolling = enableScrolling;
		return this;
	}

}
