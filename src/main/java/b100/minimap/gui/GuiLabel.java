package b100.minimap.gui;

public class GuiLabel extends Gui {
	
	public String text;
	public int posX;
	public int posY;
	public int textColor = 0xFFFFFF;
	
	public GuiLabel(String string) {
		setText(string);
	}
	
	public GuiLabel(String string, int posX, int posY) {
		setText(string);
		setPosition(posX, posY);
	}
	
	public void draw(float partialTicks) {
		utils.drawString(text, posX, posY, getTextColor());
	}
	
	public int getTextColor() {
		return textColor;
	}
	
	public void setText(String string) {
		this.text = string;
	}
	
	public String getText() {
		return text;
	}
	
	public void setPosition(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
}
