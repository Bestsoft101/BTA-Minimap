package b100.minimap.gui;

import static org.lwjgl.opengl.GL11.*;

public class GuiTextField extends GuiElement {
	
	public GuiScreen screen;
	public GuiTextComponent textComponent;
	
	public GuiTextField(GuiScreen screen, GuiTextComponent textComponent) {
		this.screen = screen;
		this.textComponent = textComponent;
		
		setSize(100, 10);
	}
	
	public GuiTextField(GuiScreen screen, String string) {
		this.screen = screen;
		this.textComponent = new GuiTextComponent(string);
		
		setSize(100, 10);
	}
	
	@Override
	public void draw(float partialTicks) {
		glDisable(GL_TEXTURE_2D);
		utils.drawRectangle(posX, posY, width, height, getBackgroundColor());
		
		textComponent.posX = this.posX + 2;
		textComponent.posY = this.posY + 1;
		textComponent.draw(partialTicks);
	}
	
	@Override
	public void keyEvent(int key, char c, boolean pressed, boolean repeat, int mouseX, int mouseY) {
		textComponent.keyEvent(key, c, pressed);
		
		super.keyEvent(key, c, pressed, repeat, mouseX, mouseY);
	}
	
	@Override
	public void mouseEvent(int button, boolean pressed, int mouseX, int mouseY) {
		if(pressed) {
			if(screen.getClickElementAt(mouseX, mouseY) == this) {
				if(button == 1) {
					textComponent.setText("");
				}
				textComponent.setFocused(true);	
			}else {
				textComponent.setFocused(false);
			}
		}
	}
	
	@Override
	public void scrollEvent(int dir, int mouseX, int mouseY) {
		textComponent.scrollEvent(dir);
		
		super.scrollEvent(dir, mouseX, mouseY);
	}
	
	public int getBackgroundColor() {
		return textComponent.focused ? Colors.buttonBackgroundHover : Colors.buttonBackground;
	}
	
	public String getText() {
		return textComponent.text;
	}

}
