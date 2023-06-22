package b100.minimap.gui;

public class GuiTextComponentInteger extends GuiTextComponent {

	public GuiTextComponentInteger(int value) {
		super(String.valueOf(value));
	}
	
	@Override
	public boolean isCharacterAllowed(char c) {
		return super.isCharacterAllowed(c) && "0123456789-".indexOf(c) != -1;
	}
	
	public void setValue(int value) {
		setText(String.valueOf(value));
	}
	
	public int getValue() {
		try{
			return Integer.parseInt(text);
		}catch (Exception e) {
			return 0;
		}
	}

}
