package xlays.c00lgui.modules.impl;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsPreset;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ParticleStatus;
import xlays.c00lgui.modules.Module;

public final class AutoLowSettings extends Module {
    private Integer oldRenderDistance;
    private Integer oldSimulationDistance;
    private GraphicsPreset oldPreset;
    private CloudStatus oldCloudStatus;
    private Boolean oldAmbientOcclusion;

    public AutoLowSettings() { super("auto_low", "Auto low settings", false); }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null) return;
        oldRenderDistance = mc.options.renderDistance().get();
        oldSimulationDistance = mc.options.simulationDistance().get();
        oldPreset = mc.options.graphicsPreset().get();
        oldCloudStatus = mc.options.cloudStatus().get();
        oldAmbientOcclusion = mc.options.ambientOcclusion().get();
        mc.options.applyGraphicsPreset(GraphicsPreset.FAST);
        mc.options.renderDistance().set(Math.min(4, oldRenderDistance));
        mc.options.simulationDistance().set(Math.min(4, oldSimulationDistance));
        mc.options.cloudStatus().set(CloudStatus.OFF);
        mc.options.ambientOcclusion().set(false);
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null) return;
        if (oldPreset != null) mc.options.applyGraphicsPreset(oldPreset);
        if (oldRenderDistance != null) mc.options.renderDistance().set(oldRenderDistance);
        if (oldSimulationDistance != null) mc.options.simulationDistance().set(oldSimulationDistance);
        if (oldCloudStatus != null) mc.options.cloudStatus().set(oldCloudStatus);
        if (oldAmbientOcclusion != null) mc.options.ambientOcclusion().set(oldAmbientOcclusion);
    }
}
