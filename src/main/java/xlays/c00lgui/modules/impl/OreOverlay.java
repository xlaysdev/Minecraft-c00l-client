package xlays.c00lgui.modules.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import xlays.c00lgui.modules.Module;

/** Lists nearby loaded ore coordinates in the HUD; scans a small area periodically to limit work on low-end devices. */
public final class OreOverlay extends Module {
    private final List<String> nearest = new ArrayList<>();
    private int tickCounter;

    public OreOverlay() { super("ore_overlay", "Ore coordinates HUD", false); }

    @Override
    public void onTick() {
        if (++tickCounter < 30) return;
        tickCounter = 0;
        nearest.clear();
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        BlockPos origin = mc.player.blockPosition();
        List<BlockPos> found = new ArrayList<>();
        for (int x = origin.getX() - 8; x <= origin.getX() + 8; x++) {
            for (int y = origin.getY() - 6; y <= origin.getY() + 6; y++) {
                for (int z = origin.getZ() - 8; z <= origin.getZ() + 8; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = mc.level.getBlockState(pos);
                    if (isOre(state)) found.add(pos);
                }
            }
        }
        found.sort(Comparator.comparingDouble(pos -> pos.distToCenterSqr(mc.player.position())));
        for (int i = 0; i < Math.min(3, found.size()); i++) {
            BlockPos pos = found.get(i);
            nearest.add("Ore @ " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        }
    }

    public List<String> nearest() { return List.copyOf(nearest); }

    private boolean isOre(BlockState state) {
        return state.is(BlockTags.DIAMOND_ORES)
                || state.is(BlockTags.EMERALD_ORES)
                || state.is(BlockTags.GOLD_ORES)
                || state.is(BlockTags.IRON_ORES)
                || state.is(BlockTags.COPPER_ORES)
                || state.is(BlockTags.REDSTONE_ORES)
                || state.is(BlockTags.LAPIS_ORES)
                || state.is(BlockTags.COAL_ORES)
                || state.is(Blocks.ANCIENT_DEBRIS);
    }

    @Override
    public void onDisable() { nearest.clear(); tickCounter = 0; }
}
