package b100.minimap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.minimap.Minimap;
import net.minecraft.client.Minecraft;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
	
	@Inject(method = "runTick", at = @At("TAIL"))
	public void minimap_onRunTick(CallbackInfo ci) {
		Minimap.instance.onTick();
	}
	
}
