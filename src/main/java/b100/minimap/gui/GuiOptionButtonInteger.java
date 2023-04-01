package b100.minimap.gui;

import b100.minimap.config.RangedIntegerOption;
import net.minecraft.src.dynamictexture.DynamicTexture;

public class GuiOptionButtonInteger extends GuiOptionButton<Integer> {

	private RangedIntegerOption integerOption;
	
	public GuiOptionButtonInteger(GuiScreen screen, RangedIntegerOption option) {
		super(screen, option);
		
		if(option.values == null) {
			throw new NullPointerException();
		}
		
		this.integerOption = option;
	}
	
	@Override
	public void onClick(int button) {
		int dir = button == 1 ? -1 : 1;
		integerOption.value = integerOption.values[DynamicTexture.pmod(getValueIndex(integerOption.value) + dir, integerOption.values.length)];
		onOptionValueChanged();
	}
	
	public int getValueIndex(int value) {
		for(int i=0; i < integerOption.values.length; i++) {
			if(integerOption.values[i] == value) return i;
		}
		return 0;
	}

}
