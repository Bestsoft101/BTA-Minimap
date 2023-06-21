package b100.minimap.gui;

import java.util.ArrayList;
import java.util.List;

import b100.minimap.utils.Utils;

public class GuiScrollableContainer extends GuiContainerBox {
	
	public GuiScreen screen;
	
	public int elementsPerPage;
	
	public int scrollLevel;
	
	public int elementWidth = 120;
	public int elementHeight = 10;
	
	public int scrollButtonSize = 10;

	public List<GuiElement> nonScrollableElements = new ArrayList<>();
	public List<GuiElement> scrollableElements = new ArrayList<>();
	
	public GuiButton buttonScrollUp;
	public GuiButton buttonScrollDown;
	
	private boolean needsUpdate = false;
	
	public GuiScrollableContainer(GuiScreen screen, int elementCount) {
		this.screen = screen;
		this.elementsPerPage = elementCount;
		
		this.buttonScrollUp = new GuiButton(screen, 0).addActionListener((e) -> scroll(-1));
		this.buttonScrollDown = new GuiButton(screen, 1).addActionListener((e) -> scroll(1));
		
		add(buttonScrollUp);
		add(buttonScrollDown);
	}
	
	@Override
	public void draw(float partialTicks) {
		if(needsUpdate) {
			update();
		}
		
		super.draw(partialTicks);
	}
	
	public void update() {
		this.needsUpdate = false;

		int maxScrollLevel = scrollableElements.size() - elementsPerPage;
		this.scrollLevel = Utils.clamp(scrollLevel, 0, maxScrollLevel);
		
		this.buttonScrollUp.clickable = scrollLevel > 0;
		this.buttonScrollDown.clickable = scrollLevel < maxScrollLevel;
		
		elements.clear();
		elements.addAll(nonScrollableElements);
		
		final int paddingOuter = 2;
		final int paddingInner = 1;
		
		int w = elementWidth + paddingInner + scrollButtonSize + 2 * paddingOuter;
		int h = elementsPerPage * elementHeight + (elementsPerPage - 1) * paddingInner + 2 * paddingOuter;
		
		int x = (screen.width - w) / 2;
		int y = (screen.height - h) / 2;
		
		setPositionAndSize(x, y, w, h);
		
		for(int i=0; i < scrollableElements.size(); i++) {
			GuiElement element = scrollableElements.get(i);
			
			int listPosition = i - scrollLevel;
			if(listPosition >= 0 && listPosition < elementsPerPage) {
				int x0 = x + paddingOuter;
				int y0 = y + paddingOuter + (elementHeight + paddingInner) * listPosition;
				
				element.setPosition(x0, y0);
				elements.add(element);
			}else {
				element.setPosition(0, 0);
			}
			element.setSize(elementWidth, elementHeight);
		}

		buttonScrollUp.setSize(scrollButtonSize, scrollButtonSize);
		buttonScrollDown.setSize(scrollButtonSize, scrollButtonSize);
		
		buttonScrollUp.setPosition(x + paddingOuter + elementWidth + paddingInner, y + paddingOuter);
		buttonScrollDown.setPosition(x + paddingOuter + elementWidth + paddingInner, y + h - paddingOuter - scrollButtonSize);
	}
	
	public void onResize() {
		update();
	}
	
	@Override
	public void onScroll(int dir, int mouseX, int mouseY) {
		dir = Utils.clamp(dir, -1, 1);
		
		if(dir < 0) {
			scroll(1);
			throw new CancelEventException();
		}
		if(dir > 0 && scrollLevel > 0) {
			scroll(-1);
			throw new CancelEventException();
		}
		super.onScroll(dir, mouseX, mouseY);
	}
	
	public void scroll(int dir) {
		this.scrollLevel += dir;
		needsUpdate = true;
	}
	
	public void setScrollLevel(int dir) {
		this.scrollLevel = dir;
		needsUpdate = true;
	}
	
	@Override
	public <E extends GuiElement> E add(E element) {
		nonScrollableElements.add(element);
		needsUpdate = true;
		return element;
	}
	
	public <E extends GuiElement> E addScrollable(E element) {
		scrollableElements.add(element);
		needsUpdate = true;
		return element;
	}
	
}
