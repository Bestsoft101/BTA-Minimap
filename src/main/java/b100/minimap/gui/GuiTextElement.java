package b100.minimap.gui;

public class GuiTextElement extends GuiElement {

	public String text;
	
	public Align alignHorizontal = Align.CENTER;
	public Align alignVertical = Align.CENTER;
	
	public int textColor = 0xFFFFFF;
	
	public GuiTextElement(String text) {
		setText(text);
	}
	
	@Override
	public void draw(float partialTicks) {
		int stringWidth = utils.getStringWidth(text);
		int stringHeight = 8;
		
		int x = (int) (this.posX + alignHorizontal.val * (this.width - stringWidth));
		int y = (int) (this.posY + alignVertical.val * (this.height - stringHeight));
		
		utils.drawString(text, x, y, getTextColor());
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setAlignHorizontal(Align align) {
		this.alignHorizontal = align;
	}
	
	public void setAlignVertical(Align align) {
		this.alignVertical = align;
	}
	
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	public int getTextColor() {
		return textColor;
	}
	
	public static enum Align {
		
		START(0.0f), CENTER(0.5f), END(1.0f);
		
		private float val;
		
		private Align(float val) {
			this.val = val;
		}
		
	}

}
