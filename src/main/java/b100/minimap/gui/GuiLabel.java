package b100.minimap.gui;

public class GuiLabel extends Gui {
	
	public String string;
	public int posX;
	public int posY;
	public int textColor = 0xFFFFFF;
	
	public GuiLabel(String string) {
		setString(string);
	}
	
	public GuiLabel(String string, int posX, int posY) {
		setString(string);
		setPosition(posX, posY);
	}
	
	public void draw(float partialTicks) {
		utils.drawString(string, posX, posY, getTextColor());
	}
	
	public int getTextColor() {
		return textColor;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public void setPosition(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
}
