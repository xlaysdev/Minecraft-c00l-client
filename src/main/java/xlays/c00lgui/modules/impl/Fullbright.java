package xlays.c00lgui.modules.impl;

import net.minecraft.client.Minecraft;
import xlays.c00lgui.modules.Module;

/** Sets the vanilla brightness slider to its maximum and restores it when disabled. */
public final class Fullbright extends Module {
    private Double previous;

    public Fullbright() { super("fullbright", "Max brightness", false); }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null) return;
        previous = mc.options.gamma().get();
        mc.options.gamma().set(1.0D);
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options != null && previous != null) mc.options.gamma().set(previous);
    }
}
