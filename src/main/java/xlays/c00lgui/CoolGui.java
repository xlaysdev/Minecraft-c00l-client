package xlays.c00lgui;

import net.neoforged.fml.common.Mod;
import xlays.c00lgui.modules.ModuleManager;

@Mod(CoolGui.MOD_ID)
public final class CoolGui {
    public static final String MOD_ID = "c00lgui";

    public CoolGui() {
        ModuleManager.registerDefaults();
    }
}
