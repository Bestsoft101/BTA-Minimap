package b100.minimap.data;

import java.io.File;

import b100.minimap.Minimap;
import b100.minimap.utils.Utils;
import net.minecraft.src.World;

public class WorldDataManager {
	
	public Minimap minimap;
	
	public WorldDataManager(Minimap minimap) {
		this.minimap = minimap;
	}
	
	public WorldData getWorldData(World world) {
		boolean isMultiplayer = minimap.minecraftHelper.isMultiplayer(world);
		
		File folder = minimap.getConfigFolder();
		folder = new File(folder, "data");
		
		if(isMultiplayer) {
			folder = new File(folder, "multiplayer");
			folder = new File(folder, Utils.getValidFileName(minimap.minecraftHelper.getServerName(world)));
		}else {
			folder = new File(folder, "singleplayer");
			folder = new File(folder, minimap.minecraftHelper.getWorldDirectoryName(world));
		}
		
		return new WorldData(folder, world);
	}
	
}
