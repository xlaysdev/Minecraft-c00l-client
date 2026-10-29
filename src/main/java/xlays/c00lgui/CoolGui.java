
package xlays.c00lgui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CoolGui.MOD_ID)
public final class CoolGui {

    public static final String MOD_ID = "c00lgui";
    public static final Logger LOGGER =
            LoggerFactory.getLogger(MOD_ID);

    public CoolGui(IEventBus modEventBus) {
        LOGGER.info("c00l gui initialized");
    }
}
