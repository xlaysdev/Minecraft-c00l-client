package xlays.c00lgui.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import xlays.c00lgui.modules.impl.AutoLowSettings;
import xlays.c00lgui.modules.impl.EntityCulling;
import xlays.c00lgui.modules.impl.EntityGlow;
import xlays.c00lgui.modules.impl.Fullbright;
import xlays.c00lgui.modules.impl.HudModule;
import xlays.c00lgui.modules.impl.MinimalParticles;
import xlays.c00lgui.modules.impl.OreOverlay;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();
    private static boolean registered;

    private ModuleManager() {}

    public static void registerDefaults() {
        if (registered) return;
        registered = true;
        MODULES.add(new AutoLowSettings());
        MODULES.add(new MinimalParticles());
        MODULES.add(new EntityCulling());
        MODULES.add(new HudModule());
        MODULES.add(new Fullbright());
        MODULES.add(new EntityGlow());
        MODULES.add(new OreOverlay());
    }

    public static List<Module> all() {
        return Collections.unmodifiableList(MODULES);
    }

    public static Module get(String id) {
        for (Module module : MODULES) {
            if (module.id().equals(id)) return module;
        }
        return null;
    }

    public static void tickEnabled() {
        for (Module module : MODULES) {
            if (module.isEnabled()) module.onTick();
        }
    }
}
