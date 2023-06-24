package b100.minimap.gui;

import b100.minimap.config.IntegerOption;

public class GuiOptionButtonRequireItem extends GuiOptionButtonInteger {

	public GuiOptionButtonRequireItem(GuiScreen screen, IntegerOption option) {
		super(screen, option);
	}
	
	@Override
	public String getOptionValueString() {
		if(option.value == 0) return "None";
		if(option.value == 1) return "Compass";
		return super.getOptionValueString();
	}

}
