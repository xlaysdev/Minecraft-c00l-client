package dev.c00lgui.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.c00lgui.CoolGui;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.ParticleStatus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = CoolGui.MOD_ID, value = Dist.CLIENT)
public final class ClientEvents {
    private static final KeyMapping OPEN_GUI = new KeyMapping(
            "key.c00lgui.open", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT, KeyMapping.Category.MISC);
    private static boolean appliedForThisWorld = false;

    private ClientEvents() {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_GUI);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        while (OPEN_GUI.consumeClick()) {
            if (mc.screen == null) mc.setScreen(new ClickGuiScreen());
            else if (mc.screen instanceof ClickGuiScreen) mc.setScreen(null);
        }

        if (mc.level == null || mc.options == null) {
            appliedForThisWorld = false;
            return;
        }
        if (ClientOptions.autoLowSettings && !appliedForThisWorld) {
            try {
                mc.options.renderDistance().set(2);
                mc.options.simulationDistance().set(2);
                mc.options.particles().set(ClientOptions.minimalParticles ? ParticleStatus.MINIMAL : ParticleStatus.ALL);
                mc.options.graphicsMode().set(GraphicsStatus.FAST);
                mc.options.cloudStatus().set(CloudStatus.OFF);
                mc.options.ambientOcclusion().set(false);
            } catch (RuntimeException ignored) {
                // Best effort only.
            }
            appliedForThisWorld = true;
        }
    }

    @SubscribeEvent
    public static void onLivingRender(RenderLivingEvent.Pre<?, ?> event) {
        if (!ClientOptions.entityCull) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.getEntity() == mc.player) return;
        if (mc.player.distanceToSqr(event.getEntity()) > 32.0 * 32.0) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event) {
        if (!ClientOptions.hud) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long max = Runtime.getRuntime().maxMemory();
        String line = "c00l gui | " + mc.getFps() + " FPS | RAM " +
                (used / (1024 * 1024)) + "/" + (max / (1024 * 1024)) + " MiB";
        event.getGuiGraphics().drawString(mc.font, line, 6, 6, 0xFF62E6FF, true);
    }
}
