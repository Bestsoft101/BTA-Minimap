package b100.minimap.gui;

import b100.minimap.utils.Utils;

public class GuiTextComponentColor extends GuiTextComponent {
	
	private int value;
	private boolean isValid = true;
	private boolean includeAlpha;
	
	public GuiTextComponentColor(int color, boolean includeAlpha) {
		super(Utils.toColorString(color, includeAlpha));
		
		this.value = color;
		this.includeAlpha = includeAlpha;
	}
	
	public void setColor(int color) {
		setText(Utils.toColorString(color, includeAlpha));
	}
	
	public int getColor() {
		return value;
	}
	
	@Override
	public void onUpdate() {
		try {
			this.value = Utils.parseColor(text);
			this.isValid = true;
		}catch (NumberFormatException e) {
			this.isValid = false;
		}
		super.onUpdate();
	}
	
	@Override
	public int getTextColor() {
		return isValid ? super.getTextColor() : Colors.textInvalid;
	}

}
