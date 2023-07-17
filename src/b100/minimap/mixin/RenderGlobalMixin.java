package b100.minimap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.minimap.Minimap;
import net.minecraft.src.RenderGlobal;

@Mixin(value = RenderGlobal.class, remap = false)
public class RenderGlobalMixin {
	
	@Inject(method = "loadRenderers", at = @At("TAIL"))
	public void minimap_onLoadRenderers(CallbackInfo ci) {
		Minimap.instance.mapRender.onUpdateAllChunks();
	}

}
