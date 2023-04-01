package b100.minimap.gui;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiContainer extends GuiElement {
	
	public List<GuiElement> elements = new ArrayList<>();
	
	public GuiContainer() {
		
	}
	
	public <E extends GuiElement> E add(E element) {
		elements.add(element);
		return element;
	}
	
	@Override
	public void draw(float partialTicks) {
		for(int i=0; i < elements.size(); i++) {
			elements.get(i).draw(partialTicks);
		}
	}
	
	@Override
	public void keyEvent(int key, boolean pressed, boolean repeat, int mouseX, int mouseY) {
		for(int i=0; i < elements.size(); i++) {
			elements.get(i).keyEvent(key, pressed, repeat, mouseX, mouseY);
		}
	}
	
	@Override
	public void mouseEvent(int button, boolean pressed, int mouseX, int mouseY) {
		for(int i=0; i < elements.size(); i++) {
			elements.get(i).mouseEvent(button, pressed, mouseX, mouseY);
		}
	}
	
	@Override
	public void onScroll(int dir, int mouseX, int mouseY) {
		for(int i=0; i < elements.size(); i++) {
			elements.get(i).onScroll(dir, mouseX, mouseY);
		}
	}
	
	public GuiElement getClickElementAt(int x, int y) {
		for(int i = elements.size() - 1; i >= 0; i--) {
			GuiElement element = elements.get(i);
			if(element instanceof GuiContainer) {
				GuiContainer container = (GuiContainer) element;
				GuiElement element1 = container.getClickElementAt(x, y);
				if(element1 != null) {
					return element1;
				}
			}
			if(element.isSolid() && element.isInside(x, y)) {
				return element;
			}
		}
		return null;
	}
	
}
