package b100.minimap.gui;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

public class GuiButton extends GuiElement {

	public GuiScreen screen;
	public String text;

	public int bgColor = 0xbb505050;
	public int hoverColor = 0xcc808080;
	public int textColor = 0xffffff;
	
	public boolean mouseOver = false;
	
	public List<ActionListener> actionListeners = new ArrayList<>();
	
	public GuiButton(GuiScreen screen, String text) {
		this(screen);
		this.text = text;
	}
	
	public GuiButton(GuiScreen screen) {
		this.screen = screen;
		
		setSize(50, 10);
	}
	
	@Override
	public void draw(float partialTicks) {
		mouseOver = screen.getClickElementAt(screen.cursorX, screen.cursorY) == this;
		
		glDisable(GL_TEXTURE_2D);
		utils.drawRectangle(posX, posY, width, height, getColor());
		
		glEnable(GL_TEXTURE_2D);
		utils.drawCenteredString(text, posX + width / 2, posY + height / 2 - 4, textColor);
	}
	
	public int getColor() {
		return mouseOver ? hoverColor : bgColor;
	}

	@Override
	public void mouseEvent(int button, boolean pressed, int mouseX, int mouseY) {
		if(screen.getClickElementAt(mouseX, mouseY) == this && pressed) {
			utils.playButtonSound();
			onClick(button);
			throw new CancelEventException();
		}
	}
	
	public void onClick(int button) {
		try {
			for(int i=0; i < actionListeners.size(); i++) {
				actionListeners.get(i).actionPerformed(this);
			}	
		}catch (CancelEventException e) {}
	}
	
	public GuiButton addActionListener(ActionListener actionListener) {
		this.actionListeners.add(actionListener);
		return this;
	}
	
	public GuiButton removeActionListener(ActionListener actionListener) {
		this.actionListeners.remove(actionListener);
		return this;
	}

}
