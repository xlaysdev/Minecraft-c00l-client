package xlays.c00lgui.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import xlays.c00lgui.modules.Module;
import xlays.c00lgui.modules.ModuleManager;

public final class ClickGuiScreen extends Screen {
    private int panelX, panelY, panelW, panelH;
    private static final int ROW_HEIGHT = 25;
    private static final int ROW_GAP = 2;
    private static final int ROW_TOP = 34;

    public ClickGuiScreen() { super(Component.literal("c00l gui")); }

    @Override
    protected void init() {
        panelW = Math.min(320, Math.max(230, this.width - 16));
        int needed = ROW_TOP + ModuleManager.all().size() * (ROW_HEIGHT + ROW_GAP) + 24;
        panelH = Math.min(250, Math.max(220, Math.min(needed, this.height - 16)));
        panelX = (this.width - panelW) / 2;
        panelY = (this.height - panelH) / 2;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, width, height, 0x99050A13);
        g.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xFF101725);
        g.fill(panelX, panelY, panelX + panelW, panelY + 3, 0xFF52E6FF);
        g.fill(panelX, panelY + 3, panelX + 5, panelY + panelH, 0xFF0B111D);
        g.drawString(font, "c00l gui  /  MODULES", panelX + 12, panelY + 12, 0xFF62E6FF, false);

        int row = 0;
        for (Module module : ModuleManager.all()) {
            int y = panelY + ROW_TOP + row * (ROW_HEIGHT + ROW_GAP);
            drawModule(g, module, panelX + 12, y, panelW - 24, mouseX, mouseY);
            row++;
        }
        g.drawString(font, "Right Shift closes", panelX + 12, panelY + panelH - 15, 0xFF71819B, false);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void drawModule(GuiGraphics g, Module module, int x, int y, int w, int mx, int my) {
        boolean hover = mx >= x && mx <= x + w && my >= y && my <= y + ROW_HEIGHT;
        int bg = module.isEnabled() ? (hover ? 0xFF1C555F : 0xFF163D46)
                : (hover ? 0xFF26354A : 0xFF1A2637);
        g.fill(x, y, x + w, y + ROW_HEIGHT, bg);
        g.drawString(font, module.name(), x + 7, y + 8, 0xFFE6EEF8, false);
        String state = module.isEnabled() ? "ON" : "OFF";
        int stateColor = module.isEnabled() ? 0xFF8BFFFF : 0xFFC5CEDA;
        g.drawString(font, state, x + w - 27, y + 8, stateColor, false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != 0) return super.mouseClicked(event, doubleClick);
        double mx = event.x(), my = event.y();
        int row = 0;
        for (Module module : ModuleManager.all()) {
            int y = panelY + ROW_TOP + row * (ROW_HEIGHT + ROW_GAP);
            if (mx >= panelX + 12 && mx <= panelX + panelW - 12 && my >= y && my <= y + ROW_HEIGHT) {
                module.toggle();
                return true;
            }
            row++;
        }
        if (mx < panelX || mx > panelX + panelW || my < panelY || my > panelY + panelH) onClose();
        return true;
    }

    @Override public boolean isPauseScreen() { return false; }
}
