package b100.minimap.gui;

import b100.minimap.Minimap;
import b100.minimap.mc.IMinecraftHelper;

public abstract class Gui {

	public Minimap minimap = Minimap.instance;
	public IGuiUtils utils = minimap.guiUtils;
	public IMinecraftHelper minecraftHelper = minimap.minecraftHelper;
	
}
