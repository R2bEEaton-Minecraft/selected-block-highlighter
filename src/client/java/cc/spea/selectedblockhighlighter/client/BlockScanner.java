package cc.spea.selectedblockhighlighter.client;

import cc.spea.selectedblockhighlighter.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BlockScanner {

    private static List<BlockPos> matchingBlocks = new ArrayList<>();
    private static Block lastScannedBlock = null;
    private static BlockPos lastPlayerPos = null;

    public static List<BlockPos> getMatchingBlocks() {
        return matchingBlocks;
    }

    public static void scanForBlocks() {
        Minecraft client = Minecraft.getInstance();
        ModConfig config = ModConfig.getInstance();

        if (client.player == null || client.level == null || !config.isEnabled()) {
            matchingBlocks.clear();
            lastScannedBlock = null;
            return;
        }

        ItemStack heldItem = client.player.getMainHandItem();

        // Only scan if holding a block item
        if (!(heldItem.getItem() instanceof BlockItem blockItem)) {
            matchingBlocks.clear();
            lastScannedBlock = null;
            return;
        }

        Block targetBlock = blockItem.getBlock();
        BlockPos playerPos = client.player.blockPosition();

        // Only rescan if player moved or changed held item
        if (targetBlock.equals(lastScannedBlock) && playerPos.equals(lastPlayerPos)) {
            return;
        }

        lastScannedBlock = targetBlock;
        lastPlayerPos = playerPos;
        matchingBlocks.clear();

        Level level = client.level;
        int range = config.getScanRange();

        // Scan all blocks in range
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = playerPos.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (state.getBlock() == targetBlock) {
                        matchingBlocks.add(pos.immutable());
                    }
                }
            }
        }
    }

    public static void clear() {
        matchingBlocks.clear();
        lastScannedBlock = null;
        lastPlayerPos = null;
    }
}
