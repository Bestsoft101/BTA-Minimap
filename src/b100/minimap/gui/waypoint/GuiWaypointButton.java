package b100.minimap.gui.waypoint;

import static org.lwjgl.opengl.GL11.*;

import b100.minimap.Minimap;
import b100.minimap.gui.Colors;
import b100.minimap.gui.GuiButton;
import b100.minimap.gui.GuiContextMenu;
import b100.minimap.waypoint.Waypoint;

public class GuiWaypointButton extends GuiButton {

	public GuiWaypoints guiWaypoints;
	public Waypoint waypoint;
	
	public GuiWaypointButton(GuiWaypoints screen, Waypoint waypoint) {
		super(screen);
		
		this.guiWaypoints = screen;
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
		if(button == 1) {
			GuiContextMenu contextMenu = new GuiContextMenu(screen);
			
			contextMenu.addContextMenuElement(new GuiButton(screen, "Edit").addActionListener((e) -> edit()));
			contextMenu.addContextMenuElement(new GuiButton(screen, "Teleport").addActionListener((e) -> teleport()).setClickable(minecraftHelper.getEnableCheats()));
//			contextMenu.addContextMenuElement(new GuiButton(screen, "Share").addActionListener((e) -> share()).setClickable(false));
			contextMenu.addContextMenuElement(new GuiButton(screen, "Delete").addActionListener((e) -> delete()));
			
			screen.add(contextMenu);
		}else {
			waypoint.visible = !waypoint.visible;
		}
		
		
		super.onClick(button);
	}
	
	public void edit() {
		utils.displayGui(new GuiEditWaypoint(screen, waypoint));
	}
	
	public void teleport() {
		if(minimap.minecraftHelper.getEnableCheats()) {
			minimap.minecraftHelper.getThePlayer().teleportTo(waypoint.x, waypoint.y, waypoint.z);
		}
		utils.displayGui(null);
	}
	
	public void share() {
		// TODO
	}
	
	public void delete() {
		if(minimap.worldData.remove(waypoint)) {
			Minimap.log("Waypoint deleted: "+waypoint.name);
			
			guiWaypoints.init();
		}else {
			Minimap.log("Could not remove waypoint!");
		}
	}

}
