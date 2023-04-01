package b100.minimap.gui;

import b100.minimap.config.BooleanOption;

public class GuiOptionButtonBoolean extends GuiOptionButton<Boolean> {

	public GuiOptionButtonBoolean(GuiScreen screen, BooleanOption booleanOption) {
		super(screen, booleanOption);
	}
	
	@Override
	public void onClick(int button) {
		option.value = !option.value;
		onOptionValueChanged();
	}

}
