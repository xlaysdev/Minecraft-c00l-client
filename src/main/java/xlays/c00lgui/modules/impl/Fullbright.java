
package xlays.c00lgui.modules.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import xlays.c00lgui.modules.Module;

public final class Fullbright extends Module {

    private static final int DURATION = 220;
    private boolean appliedByModule = false;

    public Fullbright() {
        super("fullbright", "Brightens dark areas using Night Vision", false);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) {
            return;
        }

        if (!isEnabled()) {
            removeOwnedEffect(mc);
            return;
        }

        MobEffectInstance current =
            mc.player.getEffect(MobEffects.NIGHT_VISION);

        // Do not overwrite a Night Vision effect already present.
        if (current != null && !appliedByModule) {
            return;
        }

        mc.player.addEffect(new MobEffectInstance(
            MobEffects.NIGHT_VISION,
            DURATION,
            0,
            true,  // ambient
            false, // particles
            false  // icon
        ));

        appliedByModule = true;
    }

    @Override
    public void onDisable() {
        removeOwnedEffect(Minecraft.getInstance());
    }

    private void removeOwnedEffect(Minecraft mc) {
        if (!appliedByModule) {
            return;
        }

        if (mc.player != null) {
            MobEffectInstance current =
                mc.player.getEffect(MobEffects.NIGHT_VISION);

            if (current != null) {
                mc.player.removeEffect(MobEffects.NIGHT_VISION);
            }
        }

        appliedByModule = false;
    }
}
