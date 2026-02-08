package de.schlaumeier.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.schlaumeier.FishTracker;
import de.schlaumeier.ProbabilityColorHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@Mixin(EntityRenderer.class)
public class AutoFishNameTagsRenderMixin {
    @Inject(
        method = "getNameTag(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/chat/Component;",
        at = @At("RETURN"),
        cancellable = true
    )
    private void modifyDisplayName(Entity entity, CallbackInfoReturnable<Component> cir) {
        Component original = cir.getReturnValue();
        if (original == null || !(entity instanceof Player player)) {
            return;
        }

        int probability = FishTracker.getStats(player.getUUID()).getAutoFishingProbabilityPercent();
        Component modified = original.copy().append(Component.literal("[" + probability + "%]").withColor(ProbabilityColorHelper.getColor(probability)));

        cir.setReturnValue(modified);
    }
}
