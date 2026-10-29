
package xlays.c00lgui.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.lwjgl.glfw.GLFW;

import xlays.c00lgui.CoolGui;
import xlays.c00lgui.modules.Module;
import xlays.c00lgui.modules.ModuleManager;
import xlays.c00lgui.modules.impl.OreOverlay;

@EventBusSubscriber(
    modid = CoolGui.MOD_ID,
    value = Dist.CLIENT
)
public final class ClientEvents {

    public static final KeyMapping OPEN_GUI = new KeyMapping(
        "key.c00lgui.open",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_RIGHT_SHIFT,
        KeyMapping.Category.MISC
    );

    private ClientEvents() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        // Register modules before the GUI accesses them.
        ModuleManager.registerDefaults();

        Minecraft mc = Minecraft.getInstance();

        while (OPEN_GUI.consumeClick()) {
            if (mc.screen == null) {
                mc.setScreen(new ClickGuiScreen());
            } else if (mc.screen instanceof ClickGuiScreen) {
                mc.setScreen(null);
            }
        }

        // This is what makes module onTick() methods run.
        ModuleManager.tickEnabled();
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event) {
        ModuleManager.registerDefaults();

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) {
            return;
        }

        Module hud = ModuleManager.get("hud");

        if (hud != null && hud.isEnabled()) {
            long usedMemory =
                Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();

            long maxMemory = Runtime.getRuntime().maxMemory();

            String info = "c00l gui | " + mc.getFps()
                + " FPS | RAM "
                + (usedMemory / (1024 * 1024))
                + "/"
                + (maxMemory / (1024 * 1024))
                + " MiB";

            event.getGuiGraphics().drawString(
                mc.font, info, 6, 6, 0xFF62E6FF, true
            );

            Module oreModule = ModuleManager.get("ore_overlay");

            if (oreModule != null && oreModule.isEnabled()
                    && oreModule instanceof OreOverlay oreOverlay) {
                int y = 19;

                for (String line : oreOverlay.nearest()) {
                    event.getGuiGraphics().drawString(
                        mc.font, line, 6, y, 0xFFFFD166, true
                    );
                    y += 12;
                }
            }
        }
    }
}
