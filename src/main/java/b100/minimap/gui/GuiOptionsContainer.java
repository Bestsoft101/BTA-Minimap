package b100.minimap.gui;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

public class GuiOptionsContainer extends GuiContainer {
	
	public GuiScreen screen;

	public int bgColor = 0xaa000000;
	
	private List<Line> lines = new ArrayList<>();
	
	private List<GuiElement> navigationElements = new ArrayList<>();
	
	public int buttonWidth = 50;
	public int buttonHeight = 10;
	
	public GuiOptionsContainer(GuiScreen screen) {
		this.screen = screen;
	}

	@Override
	public void draw(float partialTicks) {
		glDisable(GL_TEXTURE_2D);
		utils.drawRectangle(posX, posY, width, height, bgColor);
		
		super.draw(partialTicks);
	}
	
	public void onResize() {
		resizeContainer();
		resizeNavigation();
	}
	
	private void resizeContainer() {
		int p1 = 2;
		int p2 = 1;
		
		int maxStringWidth = 0;
		for(int i=0; i < lines.size(); i++) {
			maxStringWidth = Math.max(maxStringWidth, utils.getStringWidth(lines.get(i).text));
		}
		maxStringWidth += 8;
		
		int w = maxStringWidth + buttonWidth + 2 * p1;
		int h = lines.size() * buttonHeight + (lines.size() - 1) * p2 + 2 * p1;
		
		int x = (screen.width - w) / 2;
		int y = (screen.height - h) / 2;
		
		setPositionAndSize(x, y, w, h);
		
		for(int i=0; i < lines.size(); i++) {
			lines.get(i).setPosition(x + p1, y + p1 + i * (buttonHeight + p2));
			lines.get(i).setSize(w - 2 * p1, buttonHeight);
			lines.get(i).setElementPosition(buttonWidth);
		}
	}
	
	private void resizeNavigation() {
		int buttonWidth = 0;
		int buttonHeight = 0;
		
		int p1 = 8;
		
		for(int i=0; i < navigationElements.size(); i++) {
			buttonWidth = Math.max(buttonWidth, navigationElements.get(i).width);
			buttonHeight = Math.max(buttonHeight, navigationElements.get(i).height);
		}
		
		int width = navigationElements.size() * buttonWidth + (navigationElements.size() - 1) * p1;
		
		int x0 = (screen.width - width) / 2;
		int y0 = this.posY + height + 8;

		for(int i=0; i < navigationElements.size(); i++) {
			navigationElements.get(i).setPositionAndSize(x0 + (p1 + buttonWidth) * i, y0, buttonWidth, buttonHeight);
		}
	}
	
	public void add(String text, GuiElement element) {
		Line line = new Line(text, element);
		add(line);
		lines.add(line);
	}
	
	public void addNav(GuiElement element) {
		navigationElements.add(element);
		add(element);
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
