package b100.minimap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.minimap.mc.impl.TileColorsBTA;
import net.minecraft.src.GameSettings;
import net.minecraft.src.option.Option;

@Mixin(value = GameSettings.class, remap = false)
public class GameSettingsMixin {
	
	@Inject(method = "setOptionValue", at = @At("TAIL"))
	public void minimap_biomeWaterChangeListener(Option<?> option, int delta, CallbackInfo ci) {
		TileColorsBTA.instance.onOptionValueChanged((GameSettings)(Object)this, option);
	}

}
