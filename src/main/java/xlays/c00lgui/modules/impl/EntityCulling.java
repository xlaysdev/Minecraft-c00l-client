package xlays.c00lgui.modules.impl;

import net.minecraft.client.Minecraft;
import xlays.c00lgui.modules.Module;

/** Uses Minecraft's entity-distance scaling option as a low-cost client-side alternative to a custom culling renderer. */
public final class EntityCulling extends Module {
    private Double previous;

    public EntityCulling() { super("entity_culling", "Reduced entity range", false); }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null) return;
        previous = mc.options.entityDistanceScaling().get();
        mc.options.entityDistanceScaling().set(Math.min(previous, 0.5D));
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options != null && previous != null) mc.options.entityDistanceScaling().set(previous);
    }
}
