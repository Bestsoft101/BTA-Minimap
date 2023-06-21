package b100.minimap.gui;

import java.util.ArrayList;
import java.util.List;

public class GuiOptionsContainer extends GuiContainerBox {
	
	public GuiScreen screen;

	private List<Line> lines = new ArrayList<>();
	
	public int buttonWidth = 50;
	public int buttonHeight = 10;
	
	public GuiOptionsContainer(GuiScreen screen) {
		this.screen = screen;
	}

	public void onResize() {
		resizeContainer();
		resizeNavigation();
	}
	
	private void resizeContainer() {
		final int paddingOuter = 2;
		final int paddingInner = 1;
		
		int maxStringWidth = 0;
		for(int i=0; i < lines.size(); i++) {
			maxStringWidth = Math.max(maxStringWidth, utils.getStringWidth(lines.get(i).text));
		}
		maxStringWidth += 8;
		
		int w = maxStringWidth + buttonWidth + 2 * paddingOuter;
		int h = lines.size() * buttonHeight + (lines.size() - 1) * paddingInner + 2 * paddingOuter;
		
		int x = (screen.width - w) / 2;
		int y = (screen.height - h) / 2;
		
		setPositionAndSize(x, y, w, h);
		
		for(int i=0; i < lines.size(); i++) {
			lines.get(i).setPosition(x + paddingOuter, y + paddingOuter + i * (buttonHeight + paddingInner));
			lines.get(i).setSize(w - 2 * paddingOuter, buttonHeight);
			lines.get(i).setElementPosition(buttonWidth);
		}
	}
	
	private void resizeNavigation() {
	}
	
	public void add(String text, GuiElement element) {
		Line line = new Line(text, element);
		add(line);
		lines.add(line);
	}
	
	private class Line extends GuiContainer {
		
		private String text;
		private GuiElement element;
		
		public Line(String text, GuiElement element) {
			this.text = text;
			this.element = element;
			
			add(element);
		}
		
		@Override
		public void draw(float partialTicks) {
			super.draw(partialTicks);
			GuiElement element = screen.getClickElementAt(screen.cursorX, screen.cursorY);
			boolean mouseOver = element == this || element == this.element;
			utils.drawString(text, posX, posY + height / 2 - 4, mouseOver ? 0xffffff : 0xcccccc);
		}
		
		public void setElementPosition(int elementWidth) {
			element.setPosition(this.posX + this.width - elementWidth, this.posY);
			element.setSize(elementWidth, this.height);
		}
		
	}

}
