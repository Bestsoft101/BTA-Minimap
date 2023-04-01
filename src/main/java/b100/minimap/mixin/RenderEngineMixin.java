package b100.minimap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.minimap.Minimap;
import net.minecraft.src.RenderEngine;

@Mixin(value = RenderEngine.class, remap = false)
public class RenderEngineMixin {
	
	@Inject(method = "refreshTextures", at = @At("TAIL"))
	public void minimap_onRefreshTextures(CallbackInfo ci) {
		Minimap.instance.onReload();
	}
	
}
