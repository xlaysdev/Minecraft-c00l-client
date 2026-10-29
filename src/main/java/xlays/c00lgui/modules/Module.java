package xlays.c00lgui.modules;

/** Base class for a client-side toggleable module. */
public abstract class Module {
    private final String id;
    private final String name;
    private boolean enabled;

    protected Module(String id, String name, boolean defaultEnabled) {
        this.id = id;
        this.name = name;
        this.enabled = false;
        if (defaultEnabled) setEnabled(true);
    }

    public final String id() { return id; }
    public final String name() { return name; }
    public final boolean isEnabled() { return enabled; }

    public final void toggle() { setEnabled(!enabled); }

    public final void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if (enabled) onEnable();
        else onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}
