package xlays.c00lgui.modules.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ParticleStatus;
import xlays.c00lgui.modules.Module;

public final class MinimalParticles extends Module {
    private ParticleStatus previous;

    public MinimalParticles() { super("minimal_particles", "Minimal particles", false); }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null) return;
        previous = mc.options.particles().get();
        mc.options.particles().set(ParticleStatus.MINIMAL);
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options != null && previous != null) mc.options.particles().set(previous);
    }
}
