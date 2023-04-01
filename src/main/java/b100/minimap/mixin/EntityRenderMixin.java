package b100.minimap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.minimap.Minimap;
import net.minecraft.src.EntityRenderer;

@Mixin(value = EntityRenderer.class, remap = false)
public class EntityRenderMixin {
	
	@Inject(method = "updateCameraAndRender", at = @At("TAIL"))
	public void minimap_onRenderGui(float partialTicks, CallbackInfo ci) {
		Minimap.instance.onRenderGui(partialTicks);
	}

}
