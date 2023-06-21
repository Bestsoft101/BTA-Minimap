package b100.minimap.gui;

public class GuiButtonNavigation extends GuiButton {

	private GuiContainerBox container;
	
	public GuiButtonNavigation(GuiScreen screen, String text, GuiContainerBox container) {
		super(screen, text);
		this.container = container;
	}
	
	@Override
	public int getColor() {
		if(!mouseOver) {
			return container.bgColor;
		}else {
			return super.getColor();
		}
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		
		setSize(utils.getStringWidth(text) + 4, 12);
	}
	
	

}
