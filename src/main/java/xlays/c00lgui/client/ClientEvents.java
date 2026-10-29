
package xlays.c00lgui.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsPreset;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ParticleStatus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.lwjgl.glfw.GLFW;

import xlays.c00lgui.CoolGui;

@EventBusSubscriber(
        modid = CoolGui.MOD_ID,
        value = Dist.CLIENT
)
public final class ClientEvents {

    private static final KeyMapping OPEN_GUI = new KeyMapping(
            "key.c00lgui.open",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyMapping.Category.MISC
    );

    private static int settingsTimer = 0;

    private ClientEvents() {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_GUI);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        // Open or close the Click GUI.
        while (OPEN_GUI.consumeClick()) {
            if (mc.screen == null) {
                mc.setScreen(new ClickGuiScreen());
            } else if (mc.screen instanceof ClickGuiScreen) {
                mc.setScreen(null);
            }
        }

        if (mc.level == null || mc.options == null) {
            settingsTimer = 0;
            return;
        }

        // Reapply minimal settings once per second while enabled.
        if (ClientOptions.autoLowSettings) {
            settingsTimer++;

            if (settingsTimer >= 20) {
                settingsTimer = 0;
                applyMinimalSettings(mc);
            }
        } else {
            settingsTimer = 0;
        }
    }

    private static void applyMinimalSettings(Minecraft mc) {
        try {
            // Minimum practical view distance.
            mc.options.renderDistance().set(2);
            mc.options.simulationDistance().set(2);

            // Lowest graphics preset.
            mc.options.graphicsPreset().set(GraphicsPreset.FAST);

            // Reduce visual effects.
            mc.options.particles().set(ParticleStatus.MINIMAL);
            mc.options.cloudStatus().set(CloudStatus.OFF);
            mc.options.ambientOcclusion().set(false);

        } catch (RuntimeException exception) {
            CoolGui.LOGGER.warn(
                    "Could not apply every minimal graphics setting",
                    exception
            );
        }
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event) {
        if (!ClientOptions.hud) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) {
            return;
        }

        long usedMemory =
                Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();

        long maxMemory = Runtime.getRuntime().maxMemory();

        String text = "c00l gui | "
                + mc.getFps()
                + " FPS | RAM "
                + (usedMemory / (1024 * 1024))
                + "/"
                + (maxMemory / (1024 * 1024))
                + " MiB";

        event.getGuiGraphics().drawString(
                mc.font,
                text,
                6,
                6,
                0xFF62E6FF,
                true
        );
    }
}
