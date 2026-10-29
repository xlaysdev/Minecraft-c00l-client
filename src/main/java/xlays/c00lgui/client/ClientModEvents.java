
package xlays.c00lgui.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.bus.api.SubscribeEvent;

import xlays.c00lgui.CoolGui;

@EventBusSubscriber(
    modid = CoolGui.MOD_ID,
    value = Dist.CLIENT
)
public final class ClientModEvents {

    private ClientModEvents() {
        // Utility class
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ClientEvents.OPEN_GUI);
    }
}
