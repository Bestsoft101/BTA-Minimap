package b100.minimap.gui;

public class GuiContextMenu extends GuiContainer {
	
	public GuiScreen screen;
	
	public GuiContainerBox contextMenuContainer = new GuiContainerBox();
	
	public GuiContextMenu(GuiScreen screen) {
		this.screen = screen;
		
		this.setPosition(0, 0);
		this.setSize(screen.width, screen.height);
		
		add(contextMenuContainer);
	}
	
	@Override
	public void onAddToContainer(GuiContainer container) {
		final int paddingOuter = 2;
		final int paddingInner = 1;
		
		int elementWidth = 0;
		int elementHeight = 0;
		int elementCount = contextMenuContainer.elements.size();
		
		for(int i=0; i < elementCount; i++) {
			GuiElement element = contextMenuContainer.elements.get(i);
			elementWidth = Math.max(elementWidth, element.width);
			elementHeight = Math.max(elementHeight, element.height);
		}
		
		int width = 2 * paddingOuter + elementWidth;
		int height = 2 * paddingOuter + elementCount * elementHeight + (elementCount - 1) * paddingInner;
		
		int x = 0;
		int y = 0;
		
		if(screen.cursorX + width < screen.width) {
			x = screen.cursorX;
		}else if(screen.cursorX - width > 0) {
			x = screen.cursorX;
		}
		if(screen.cursorY + height < screen.height) {
			y = screen.cursorY;
		}else if(screen.cursorY - height > 0) {
			y = screen.cursorY;
		}
		
		contextMenuContainer.setPosition(x, y);
		contextMenuContainer.setSize(width, height);
		
		int innerPosX = x + paddingOuter;
		int innerPosY = y + paddingOuter;
		
		for(int i=0; i < elementCount; i++) {
			contextMenuContainer.elements.get(i).setPosition(innerPosX, innerPosY + i * (elementHeight + paddingInner)).setSize(elementWidth, elementHeight);
		}
	}
	
	public GuiContextMenu addContextMenuElement(GuiElement element) {
		contextMenuContainer.add(element);
		return this;
	}
	
	@Override
	public void mouseEvent(int button, boolean pressed, int mouseX, int mouseY) {
		try{
			super.mouseEvent(button, pressed, mouseX, mouseY);
		}catch (CancelEventException e) {
			screen.elements.remove(this);
		}
		if(pressed) {
			GuiElement clickElement = screen.getClickElementAt(mouseX, mouseY);
			if(clickElement == this) {
				screen.elements.remove(this);
			}
			if(clickElement == contextMenuContainer) {
				throw new CancelEventException();
			}
		}
	}

}
