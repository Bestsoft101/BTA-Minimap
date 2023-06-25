package b100.minimap.gui;

import b100.minimap.utils.Utils;

public class GuiTextComponentInteger extends GuiTextComponent {
	
	public Integer min;
	public Integer max;
	public int value;
	private boolean isValid = true;
	
	public GuiTextComponentInteger(int value) {
		super(String.valueOf(value));
		
		this.value = value;
	}
	
	public GuiTextComponentInteger(int value, Integer min, Integer max) {
		super(String.valueOf(value));
		
		this.min = min;
		this.max = max;
	}
	
	@Override
	public boolean isCharacterAllowed(char c) {
		return super.isCharacterAllowed(c) && "0123456789-".indexOf(c) != -1;
	}
	
	public void setValue(int value) {
		setText(String.valueOf(clamp(value)));
	}
	
	public int getValue() {
		return value;
	}
	
	public int clamp(int val) {
		if(min != null && val < min) return min;
		if(max != null && val > max) return max;
		return val;
	}
	
	@Override
	public void onUpdate() {
		try {
			int value = Integer.parseInt(text);
			this.isValid = true;
			int clampedValue = clamp(value);
			
			if(value != clampedValue) {
				setValue(clampedValue);
			}else {
				this.value = value;
			}
		}catch (NumberFormatException e) {
			this.isValid = false;
		}
		super.onUpdate();
	}
	
	@Override
	public void scrollEvent(int direction) {
		super.scrollEvent(direction);
		if(focused) {
			int newValue = clamp(this.value + Utils.clamp(direction, -1, 1));
			if(newValue != value) {
				setValue(newValue);
			}
			throw new CancelEventException();	
		}
	}
	
	@Override
	public int getTextColor() {
		return isValid ? super.getTextColor() : 0xFF0000;
	}

}
