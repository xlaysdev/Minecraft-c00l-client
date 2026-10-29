
package xlays.c00lgui.modules.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import xlays.c00lgui.modules.Module;

public final class OreOverlay extends Module {

    private static final int SCAN_INTERVAL = 30;
    private static final int SCAN_RADIUS_XZ = 8;
    private static final int SCAN_RADIUS_Y = 6;
    private static final int MAX_TARGETS = 3;

    private final List<String> nearest = new ArrayList<>();
    private final List<OreTarget> targets = new ArrayList<>();

    private int tickCounter;

    public record OreTarget(
        BlockPos pos,
        String name,
        int color,
        double distanceSquared
    ) {}

    public OreOverlay() {
        super("ore_overlay", "Ore direction tracers", false);
    }

    @Override
    public void onTick() {
        if (++tickCounter < SCAN_INTERVAL) return;
        tickCounter = 0;

        nearest.clear();
        targets.clear();

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        BlockPos origin = mc.player.blockPosition();
        Vec3 playerPos = mc.player.position();

        List<OreTarget> found = new ArrayList<>();

        for (int x = origin.getX() - SCAN_RADIUS_XZ;
             x <= origin.getX() + SCAN_RADIUS_XZ; x++) {

            for (int y = origin.getY() - SCAN_RADIUS_Y;
                 y <= origin.getY() + SCAN_RADIUS_Y; y++) {

                for (int z = origin.getZ() - SCAN_RADIUS_XZ;
                     z <= origin.getZ() + SCAN_RADIUS_XZ; z++) {

                    BlockPos pos = new BlockPos(x, y, z);

                    if (!mc.level.hasChunkAt(pos)) continue;

                    BlockState state = mc.level.getBlockState(pos);
                    OreInfo info = getOreInfo(state);

                    if (info == null) continue;

                    double distance = pos.distToCenterSqr(playerPos);

                    found.add(new OreTarget(
                        pos.immutable(),
                        info.name(),
                        info.color(),
                        distance
                    ));
                }
            }
        }

        found.sort(Comparator.comparingDouble(
            OreTarget::distanceSquared
        ));

        for (int i = 0; i < Math.min(MAX_TARGETS, found.size()); i++) {
            OreTarget target = found.get(i);
            targets.add(target);

            BlockPos pos = target.pos();
            nearest.add(
                target.name() + " @ "
                + pos.getX() + " "
                + pos.getY() + " "
                + pos.getZ()
            );
        }
    }

    public List<String> nearest() {
        return List.copyOf(nearest);
    }

    public List<OreTarget> targets() {
        return List.copyOf(targets);
    }

    /**
     * Draws 2D directional tracer lines from screen center
     * toward the selected ore targets.
     */
    public void renderTracers(GuiGraphics gui, Minecraft mc) {
        if (mc.player == null || mc.level == null) return;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double radius = Math.min(width, height) * 0.42;

        double yaw = Math.toRadians(mc.player.getYRot());
        double pitch = Math.toRadians(mc.player.getXRot());

        Vec3 eye = mc.player.getEyePosition();

        for (OreTarget target : targets) {
            Vec3 delta = Vec3.atCenterOf(target.pos()).subtract(eye);

            // Camera-relative horizontal direction.
            double right = delta.x * Math.cos(yaw)
                         + delta.z * Math.sin(yaw);

            // Camera-relative vertical direction.
            double up = delta.x * Math.sin(yaw) * Math.sin(pitch)
                      + delta.y * Math.cos(pitch)
                      - delta.z * Math.cos(yaw) * Math.sin(pitch);

            double forward = -delta.x * Math.sin(yaw) * Math.cos(pitch)
                           - delta.y * Math.sin(pitch)
                           + delta.z * Math.cos(yaw) * Math.cos(pitch);

            // Point behind the player: show its direction at the edge.
            if (forward < 0) {
                right = -right;
                up = -up;
            }

            double length = Math.sqrt(right * right + up * up);

            if (length < 0.001) {
                right = 0;
                up = 1;
                length = 1;
            }

            double endX = centerX + (right / length) * radius;
            double endY = centerY - (up / length) * radius;

            drawLine(
                gui,
                (int) centerX,
                (int) centerY,
                (int) endX,
                (int) endY,
                target.color()
            );
        }
    }

    private static void drawLine(
        GuiGraphics gui,
        int x1, int y1,
        int x2, int y2,
        int color
    ) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(
            1,
            Math.min(100, Math.max(Math.abs(dx), Math.abs(dy)))
        );

        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;

            int x = (int) Math.round(x1 + dx * t);
            int y = (int) Math.round(y1 + dy * t);

            gui.fill(x - 1, y - 1, x + 2, y + 2, color);
        }
    }

    private record OreInfo(String name, int color) {}

    private OreInfo getOreInfo(BlockState state) {
        if (state.is(BlockTags.DIAMOND_ORES))
            return new OreInfo("Diamond", 0xFF40E8FF);

        if (state.is(BlockTags.EMERALD_ORES))
            return new OreInfo("Emerald", 0xFF35FF70);

        if (state.is(BlockTags.GOLD_ORES))
            return new OreInfo("Gold", 0xFFFFD740);

        if (state.is(BlockTags.IRON_ORES))
            return new OreInfo("Iron", 0xFFEAEAEA);

        if (state.is(BlockTags.COPPER_ORES))
            return new OreInfo("Copper", 0xFFFF9255);

        if (state.is(BlockTags.REDSTONE_ORES))
            return new OreInfo("Redstone", 0xFFFF4040);

        if (state.is(BlockTags.LAPIS_ORES))
            return new OreInfo("Lapis", 0xFF477CFF);

        if (state.is(BlockTags.COAL_ORES))
            return new OreInfo("Coal", 0xFFAAAAAA);

        if (state.is(Blocks.ANCIENT_DEBRIS))
            return new OreInfo("Ancient Debris", 0xFFBD79FF);

        return null;
    }

    @Override
    public void onDisable() {
        nearest.clear();
        targets.clear();
        tickCounter = 0;
    }
}
