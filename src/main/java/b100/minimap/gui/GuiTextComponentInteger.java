package b100.minimap.gui;

public class GuiTextComponentInteger extends GuiTextComponent {
	
	public Integer min;
	public Integer max;
	
	public GuiTextComponentInteger(int value) {
		super(String.valueOf(value));
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
	
	@Override
	public void setText(String string) {
		if(string.length() > 0) {
			int value = Integer.parseInt(string);
			int clampedValue = clamp(value);
			if(value != clampedValue) {
				setText(String.valueOf(clampedValue));
				return;
			}	
		}
		super.setText(string);
	}
	
	public void setValue(int value) {
		setText(String.valueOf(clamp(value)));
	}
	
	public int getValue() {
		try{
			return clamp(Integer.parseInt(text));
		}catch (NumberFormatException e) {
			return clamp(0);
		}
	}
	
	public int clamp(int val) {
		if(min != null && val < min) return min;
		if(max != null && val > max) return max;
		return val;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(text.length() > 0) {
			final int val = Integer.parseInt(text);
			int newVal = clamp(val);
			if(val != newVal) {
				setValue(newVal);
			}
		}
	}

}
