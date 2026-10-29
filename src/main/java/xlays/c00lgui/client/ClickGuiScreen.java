
package xlays.c00lgui.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public final class ClickGuiScreen extends Screen {
    private int panelX, panelY, panelW, panelH;

    public ClickGuiScreen() {
        super(Component.literal("c00l gui"));
    }

    @Override
    protected void init() {
        panelW = Math.min(330, Math.max(230, this.width - 16));
        panelH = Math.min(280, Math.max(220, this.height - 16));
        panelX = (this.width - panelW) / 2;
        panelY = (this.height - panelH) / 2;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, width, height, 0x99050A13);
        g.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xFF101725);
        g.fill(panelX, panelY, panelX + panelW, panelY + 3, 0xFF52E6FF);
        g.fill(panelX, panelY + 3, panelX + 72, panelY + panelH, 0xFF0B111D);

        g.drawString(font, "c00l gui", panelX + 12, panelY + 13, 0xFF62E6FF, false);
        g.drawString(font, "MODULES", panelX + 9, panelY + 38, 0xFFB5C4D9, false);
        g.drawString(font, "PERFORMANCE", panelX + 9, panelY + 53, 0xFF71819B, false);
        g.drawString(font, "RENDER", panelX + 9, panelY + 68, 0xFF71819B, false);
        g.drawString(font, "HUD", panelX + 9, panelY + 83, 0xFF71819B, false);

        int x = panelX + 82;
        int w = panelW - 94;

        toggle(g, x, panelY + 34, w, "Auto low settings", ClientOptions.autoLowSettings, mouseX, mouseY);
        toggle(g, x, panelY + 63, w, "Minimal particles", ClientOptions.minimalParticles, mouseX, mouseY);
        toggle(g, x, panelY + 92, w, "Entity culling (32 blocks)", ClientOptions.entityCull, mouseX, mouseY);
        toggle(g, x, panelY + 121, w, "FPS + RAM HUD", ClientOptions.hud, mouseX, mouseY);
        toggle(g, x, panelY + 150, w, "Fullbright (prototype)", ClientOptions.fullbright, mouseX, mouseY);
        toggle(g, x, panelY + 179, w, "Entity Glow (prototype)", ClientOptions.entityGlow, mouseX, mouseY);
        toggle(g, x, panelY + 208, w, "Ore ESP (prototype)", ClientOptions.oreOverlay, mouseX, mouseY);

        g.drawString(font, "Right Shift closes menu", panelX + 10, panelY + panelH - 17, 0xFF71819B, false);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void toggle(GuiGraphics g, int x, int y, int w,
                        String label, boolean enabled, int mx, int my) {
        boolean hover = mx >= x && mx <= x + w && my >= y && my <= y + 24;

        g.fill(x, y, x + w, y + 24, hover ? 0xFF26354A : 0xFF1A2637);
        g.drawString(font, label, x + 6, y + 8, 0xFFE6EEF8, false);

        int px = x + w - 37;
        g.fill(px, y + 4, px + 32, y + 20, enabled ? 0xFF155C64 : 0xFF3A4658);
        g.drawString(font, enabled ? "ON" : "OFF", px + 4, y + 8,
                enabled ? 0xFF8BFFFF : 0xFFC5CEDA, false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != 0) {
            return super.mouseClicked(event, doubleClick);
        }

        double mx = event.x();
        double my = event.y();

        int x = panelX + 82;
        int w = panelW - 94;

        if (hit(mx, my, x, panelY + 34, w)) {
            ClientOptions.autoLowSettings = !ClientOptions.autoLowSettings;
        } else if (hit(mx, my, x, panelY + 63, w)) {
            ClientOptions.minimalParticles = !ClientOptions.minimalParticles;
        } else if (hit(mx, my, x, panelY + 92, w)) {
            ClientOptions.entityCull = !ClientOptions.entityCull;
        } else if (hit(mx, my, x, panelY + 121, w)) {
            ClientOptions.hud = !ClientOptions.hud;
        } else if (hit(mx, my, x, panelY + 150, w)) {
            ClientOptions.fullbright = !ClientOptions.fullbright;
        } else if (hit(mx, my, x, panelY + 179, w)) {
            ClientOptions.entityGlow = !ClientOptions.entityGlow;
        } else if (hit(mx, my, x, panelY + 208, w)) {
            ClientOptions.oreOverlay = !ClientOptions.oreOverlay;
        } else if (mx < panelX || mx > panelX + panelW
                || my < panelY || my > panelY + panelH) {
            onClose();
        }

        return true;
    }

    private boolean hit(double mx, double my, int x, int y, int w) {
        return mx >= x && mx <= x + w && my >= y && my <= y + 24;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
