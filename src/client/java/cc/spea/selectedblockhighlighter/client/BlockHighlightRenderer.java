package cc.spea.selectedblockhighlighter.client;

import cc.spea.selectedblockhighlighter.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;

import java.util.List;

public class BlockHighlightRenderer {

    public static void render(PoseStack poseStack, Camera camera) {
        ModConfig config = ModConfig.getInstance();
        if (!config.isEnabled()) {
            return;
        }

        List<BlockPos> blocks = BlockScanner.getMatchingBlocks();
        if (blocks.isEmpty()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.level == null) {
            return;
        }

        Vec3 cameraPos = camera.getPosition();
        float[] color = config.getHighlightColor();

        poseStack.pushPose();

        // Setup rendering state
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.lineWidth(config.getLineWidth());

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        // Render filled boxes
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        for (BlockPos pos : blocks) {
            BlockState state = client.level.getBlockState(pos);
            VoxelShape shape = state.getShape(client.level, pos);

            // If shape is empty (like fluids), check if there's a fluid and use full block
            if (shape.isEmpty() && !client.level.getFluidState(pos).isEmpty()) {
                shape = Shapes.block(); // Full block shape (0,0,0 to 1,1,1)
            } else if (shape.isEmpty()) {
                continue;
            }

            double minX = pos.getX() + shape.min(net.minecraft.core.Direction.Axis.X) - cameraPos.x;
            double minY = pos.getY() + shape.min(net.minecraft.core.Direction.Axis.Y) - cameraPos.y;
            double minZ = pos.getZ() + shape.min(net.minecraft.core.Direction.Axis.Z) - cameraPos.z;
            double maxX = pos.getX() + shape.max(net.minecraft.core.Direction.Axis.X) - cameraPos.x;
            double maxY = pos.getY() + shape.max(net.minecraft.core.Direction.Axis.Y) - cameraPos.y;
            double maxZ = pos.getZ() + shape.max(net.minecraft.core.Direction.Axis.Z) - cameraPos.z;

            drawFilledBox(buffer, poseStack.last().pose(), minX, minY, minZ, maxX, maxY, maxZ, color[0], color[1], color[2], color[3]);
        }
        tesselator.end();

        // Render outlines
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        for (BlockPos pos : blocks) {
            BlockState state = client.level.getBlockState(pos);
            VoxelShape shape = state.getShape(client.level, pos);

            // If shape is empty (like fluids), check if there's a fluid and use full block
            if (shape.isEmpty() && !client.level.getFluidState(pos).isEmpty()) {
                shape = Shapes.block(); // Full block shape (0,0,0 to 1,1,1)
            } else if (shape.isEmpty()) {
                continue;
            }

            double minX = pos.getX() + shape.min(net.minecraft.core.Direction.Axis.X) - cameraPos.x;
            double minY = pos.getY() + shape.min(net.minecraft.core.Direction.Axis.Y) - cameraPos.y;
            double minZ = pos.getZ() + shape.min(net.minecraft.core.Direction.Axis.Z) - cameraPos.z;
            double maxX = pos.getX() + shape.max(net.minecraft.core.Direction.Axis.X) - cameraPos.x;
            double maxY = pos.getY() + shape.max(net.minecraft.core.Direction.Axis.Y) - cameraPos.y;
            double maxZ = pos.getZ() + shape.max(net.minecraft.core.Direction.Axis.Z) - cameraPos.z;

            drawOutline(buffer, poseStack.last().pose(), minX, minY, minZ, maxX, maxY, maxZ, color[0], color[1], color[2], 1.0f);
        }
        tesselator.end();

        // Restore rendering state
        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private static void drawFilledBox(BufferBuilder buffer, Matrix4f matrix, double minX, double minY, double minZ,
                                        double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        // Bottom face
        buffer.vertex(matrix, (float) minX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();

        // Top face
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();

        // North face
        buffer.vertex(matrix, (float) minX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();

        // South face
        buffer.vertex(matrix, (float) minX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();

        // West face
        buffer.vertex(matrix, (float) minX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();

        // East face
        buffer.vertex(matrix, (float) maxX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
    }

    private static void drawOutline(BufferBuilder buffer, Matrix4f matrix, double minX, double minY, double minZ,
                                      double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        // Bottom edges
        buffer.vertex(matrix, (float) minX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) maxX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) maxX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) minX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();

        // Top edges
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) minX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();

        // Vertical edges
        buffer.vertex(matrix, (float) minX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) maxX, (float) minY, (float) minZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) minZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) maxX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) maxX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, (float) minX, (float) minY, (float) maxZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) minX, (float) maxY, (float) maxZ).color(red, green, blue, alpha).endVertex();
    }
}
