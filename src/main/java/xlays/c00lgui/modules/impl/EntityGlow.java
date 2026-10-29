package xlays.c00lgui.modules.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import xlays.c00lgui.modules.Module;

/** Applies the client-side glowing flag to currently loaded entities while enabled. */
public final class EntityGlow extends Module {
    public EntityGlow() { super("entity_glow", "Entity glow", false); }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity != mc.player) entity.setGlowingTag(true);
        }
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity != mc.player) entity.setGlowingTag(false);
        }
    }
}
