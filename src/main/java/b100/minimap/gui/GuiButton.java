package b100.minimap.gui;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

public class GuiButton extends GuiElement {

	public GuiScreen screen;
	public String text;
	public int icon = -1;

	public boolean clickable = true;
	
	public boolean mouseOver = false;
	
	public List<ActionListener> actionListeners = new ArrayList<>();
	
	public GuiButton(GuiScreen screen, String text) {
		this(screen);
		setText(text);
	}
	
	public GuiButton(GuiScreen screen, int icon) {
		this(screen);
		this.icon = icon;
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
		
		if(text != null) {
			glEnable(GL_TEXTURE_2D);
			utils.drawCenteredString(text, posX + width / 2, posY + height / 2 - 4, getTextColor());	
		}
		if(icon >= 0) {
			glEnable(GL_TEXTURE_2D);
			utils.drawIconWithShadow(icon, posX + width / 2 - 4, posY + height / 2 - 4, getTextColor());
		}
	}
	
	public int getColor() {
		return clickable && mouseOver ? Colors.buttonBackgroundHover : Colors.buttonBackground;
	}
	
	public int getTextColor() {
		return clickable ? Colors.buttonText : Colors.buttonTextDeactivated;
	}

	@Override
	public void mouseEvent(int button, boolean pressed, int mouseX, int mouseY) {
		if(screen.getClickElementAt(mouseX, mouseY) == this && pressed) {
			if(clickable) {
				utils.playButtonSound();
				onClick(button);	
			}
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
	
	public void setText(String text) {
		this.text = text;
	}

}
