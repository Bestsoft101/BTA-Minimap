package b100.minimap.gui;

import java.util.ArrayList;
import java.util.List;

public class GuiNavigationContainer extends GuiContainer {
	
	public GuiScreen screen;
	public GuiContainer parent;
	public Position position;
	
	public List<GuiElement> navigationElements = new ArrayList<>();
	
	public int spacing = 8;
	
	public GuiNavigationContainer(GuiScreen screen, GuiContainer parent, Position position) {
		this.screen = screen;
		this.parent = parent;
		this.position = position;
	}
	
	public void onResize() {
		int buttonWidth = 50;
		int buttonHeight = 0;
		
		for(int i=0; i < navigationElements.size(); i++) {
			buttonWidth = Math.max(buttonWidth, navigationElements.get(i).width);
			buttonHeight = Math.max(buttonHeight, navigationElements.get(i).height);
		}
		
		int width = navigationElements.size() * buttonWidth + (navigationElements.size() - 1) * spacing;
		
		int x0 = (screen.width - width) / 2;
		int y0 = spacing;
		if(position == Position.TOP) y0 = this.parent.posY - spacing - buttonHeight;	
		if(position == Position.BOTTOM) y0 = this.parent.posY + this.parent.height + spacing;	
		
		for(int i=0; i < navigationElements.size(); i++) {
			navigationElements.get(i).setPositionAndSize(x0 + (spacing + buttonWidth) * i, y0, buttonWidth, buttonHeight);
		}
	}
	
	@Override
	public <E extends GuiElement> E add(E element) {
		navigationElements.add(element);
		return super.add(element);
	}
	
	public enum Position {
		
		TOP, BOTTOM;
		
	}

}
