package b100.minimap.gui;

import static org.lwjgl.opengl.GL11.*;

public class GuiContainerBox extends GuiContainer {

	public int bgColor = Colors.boxBackground;
	
	@Override
	public void draw(float partialTicks) {
		glDisable(GL_TEXTURE_2D);
		utils.drawRectangle(posX, posY, width, height, bgColor);
		
		super.draw(partialTicks);
	}

}
