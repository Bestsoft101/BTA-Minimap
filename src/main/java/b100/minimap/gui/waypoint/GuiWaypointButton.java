package b100.minimap.gui.waypoint;

import static org.lwjgl.opengl.GL11.*;

import b100.minimap.gui.Colors;
import b100.minimap.gui.GuiButton;
import b100.minimap.gui.GuiScreen;
import b100.minimap.waypoint.Waypoint;

public class GuiWaypointButton extends GuiButton {

	public Waypoint waypoint;
	
	public GuiWaypointButton(GuiScreen screen, Waypoint waypoint) {
		super(screen);
		
		this.waypoint = waypoint;
	}
	
	@Override
	public void draw(float partialTicks) {
		super.draw(partialTicks);
		
		int colorSquareSize = height;
		
		glDisable(GL_TEXTURE_2D);
		utils.drawRectangle(posX + width - colorSquareSize, posY, colorSquareSize, colorSquareSize, waypoint.color | 0xFF000000);
		
		glEnable(GL_TEXTURE_2D);
		utils.drawString(waypoint.name, posX + 2, posY + height / 2 - 4, waypoint.visible ? Colors.buttonText : Colors.buttonTextDeactivated);
	}
	
	@Override
	public int getColor() {
		return mouseOver ? Colors.buttonBackground : 0x0;
	}
	
	@Override
	public void onClick(int button) {
		utils.displayGui(new GuiEditWaypoint(screen, waypoint));
		
		super.onClick(button);
	}

}
