package b100.minimap.gui;

public class GuiButtonNavigation extends GuiButton {

	private GuiOptionsContainer guiOptionsContainer;
	
	public GuiButtonNavigation(GuiScreen screen, String string, GuiOptionsContainer container) {
		super(screen, string);
		this.guiOptionsContainer = container;
		
		setSize(50, 12);
	}
	
	@Override
	public int getColor() {
		if(!mouseOver) {
			return guiOptionsContainer.bgColor;
		}else {
			return super.getColor();
		}
	}

}
