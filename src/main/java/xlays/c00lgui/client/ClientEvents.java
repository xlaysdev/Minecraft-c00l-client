package xlays.c00lgui.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import xlays.c00lgui.CoolGui;
import xlays.c00lgui.modules.ModuleManager;
import xlays.c00lgui.modules.impl.HudModule;
import xlays.c00lgui.modules.impl.OreOverlay;

@EventBusSubscriber(modid = CoolGui.MOD_ID, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        while (ClientModEvents.OPEN_GUI.consumeClick()) {
            if (mc.screen == null) mc.setScreen(new ClickGuiScreen());
            else if (mc.screen instanceof ClickGuiScreen) mc.setScreen(null);
        }
        ModuleManager.tickEnabled();
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (ModuleManager.get("hud") instanceof HudModule hud && hud.isEnabled()) {
            long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            long max = Runtime.getRuntime().maxMemory();
            String line = "c00l gui | " + mc.getFps() + " FPS | RAM "
                    + (used / (1024 * 1024)) + "/" + (max / (1024 * 1024)) + " MiB";
            event.getGuiGraphics().drawString(mc.font, line, 6, 6, 0xFF62E6FF, true);
        }
        if (ModuleManager.get("ore_overlay") instanceof OreOverlay ore && ore.isEnabled()) {
            int y = 19;
            for (String line : ore.nearest()) {
                event.getGuiGraphics().drawString(mc.font, line, 6, y, 0xFFFFD36A, true);
                y += 11;
            }
        }
    }
}
