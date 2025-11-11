package cc.spea.selectedblockhighlighter.client;

import cc.spea.selectedblockhighlighter.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public class BlockScanner {

    private static List<BlockPos> matchingBlocks = new ArrayList<>();
    private static Block lastScannedBlock = null;
    private static Fluid lastScannedFluid = null;
    private static ItemStack lastScannedItem = ItemStack.EMPTY;
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
            lastScannedFluid = null;
            lastScannedItem = ItemStack.EMPTY;
            return;
        }

        ItemStack heldItem = client.player.getMainHandItem();
        BlockPos playerPos = client.player.blockPosition();

        // Determine what to scan for
        Block targetBlock = null;
        Fluid targetFluid = null;

        if (heldItem.getItem() instanceof BlockItem blockItem) {
            // Holding a block item - scan for matching blocks
            targetBlock = blockItem.getBlock();
        } else if (heldItem.is(Items.WATER_BUCKET)) {
            // Holding a water bucket - scan for water source blocks
            targetFluid = Fluids.WATER;
        } else if (heldItem.is(Items.LAVA_BUCKET)) {
            // Holding a lava bucket - scan for lava source blocks
            targetFluid = Fluids.LAVA;
        } else {
            // Not holding a valid item
            matchingBlocks.clear();
            lastScannedBlock = null;
            lastScannedFluid = null;
            lastScannedItem = ItemStack.EMPTY;
            return;
        }

        // Only rescan if player moved or changed held item
        boolean sameTarget = (targetBlock != null && targetBlock.equals(lastScannedBlock)) ||
                            (targetFluid != null && targetFluid.equals(lastScannedFluid));
        if (sameTarget &&
            ItemStack.isSameItemSameTags(heldItem, lastScannedItem) &&
            playerPos.equals(lastPlayerPos)) {
            return;
        }

        lastScannedBlock = targetBlock;
        lastScannedFluid = targetFluid;
        lastScannedItem = heldItem.copy();
        lastPlayerPos = playerPos;
        matchingBlocks.clear();

        Level level = client.level;
        int range = config.getScanRange();

        // Scan all blocks in range
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = playerPos.offset(x, y, z);

                    if (targetFluid != null) {
                        // Scanning for fluid sources
                        FluidState fluidState = level.getFluidState(pos);
                        if (fluidState.getType() == targetFluid && fluidState.isSource()) {
                            matchingBlocks.add(pos.immutable());
                        }
                    } else if (targetBlock != null) {
                        // Scanning for blocks
                        BlockState state = level.getBlockState(pos);
                        if (state.getBlock() == targetBlock) {
                            matchingBlocks.add(pos.immutable());
                        }
                    }
                }
            }
        }
    }

    public static void clear() {
        matchingBlocks.clear();
        lastScannedBlock = null;
        lastScannedFluid = null;
        lastScannedItem = ItemStack.EMPTY;
        lastPlayerPos = null;
    }
}
