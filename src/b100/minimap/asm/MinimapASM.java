package b100.minimap.asm;

import b100.minimap.Minimap;
import b100.minimap.mc.impl.TileColorsBTA;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.Option;

public class MinimapASM {
	
	public static void onTick() {
		Minimap.instance.onTick();
	}
	
	public static void onRender(float partialTicks) {
		Minimap.instance.onRenderGui(partialTicks);
	}
	
	public static void onLoadRenderers() {
		Minimap.instance.mapRender.onUpdateAllChunks();
	}
	
	public static void onRefreshTextures() {
		Minimap.instance.onReload();
	}
	
	public static void onOptionChanged(GameSettings gameSettings, Option<?> option) {
		System.out.println("option value changed: " + gameSettings + " " + option);
		TileColorsBTA.instance.onOptionValueChanged(gameSettings, option);
	}

}
