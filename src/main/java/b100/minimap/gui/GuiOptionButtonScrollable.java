package b100.minimap.gui;

import org.lwjgl.input.Keyboard;

import b100.minimap.config.IntegerOption;

public class GuiOptionButtonScrollable extends GuiOptionButton<Integer> {

	public IntegerOption integerOption;
	
	public GuiOptionButtonScrollable(GuiScreen screen, IntegerOption option) {
		super(screen, option);
		
		this.integerOption = option;
	}
	
	@Override
	public void onScroll(int dir, int mouseX, int mouseY) {
		if(screen.getClickElementAt(mouseX, mouseY) == this) {
			int scrollAmount = 0;
			if(dir > 0) scrollAmount = 1;
			if(dir < 0) scrollAmount = -1;
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) scrollAmount *= 10;
			
			option.value += scrollAmount;

			if(integerOption.maxValue != null && option.value > integerOption.maxValue) option.value = integerOption.maxValue;
			if(integerOption.minValue != null && option.value < integerOption.minValue) option.value = integerOption.minValue;
			
			onOptionValueChanged();
		}
	}

}
