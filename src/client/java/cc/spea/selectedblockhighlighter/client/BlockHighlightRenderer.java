package cc.spea.selectedblockhighlighter.client;

import cc.spea.selectedblockhighlighter.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
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
        //? if >=1.21.1 {
        BufferBuilder buffer;
        //?} else {
        /*BufferBuilder buffer = tesselator.getBuilder();
        *///?}

        // Render filled boxes
        //? if >=1.21.1 {
        buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        //?} else {
        /*buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        *///?}
        for (BlockPos pos : blocks) {
            VoxelShape shape = client.level.getBlockState(pos).getShape(client.level, pos);
            if (shape.isEmpty()) {
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
        //? if >=1.21.1 {
        com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(buffer.buildOrThrow());
        //?} else {
        /*tesselator.end();
        *///?}

        // Render outlines
        //? if >=1.21.1 {
        buffer = tesselator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        //?} else {
        /*buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        *///?}
        for (BlockPos pos : blocks) {
            VoxelShape shape = client.level.getBlockState(pos).getShape(client.level, pos);
            if (shape.isEmpty()) {
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
        //? if >=1.21.1 {
        com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(buffer.buildOrThrow());
        //?} else {
        /*tesselator.end();
        *///?}

        // Restore rendering state
        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private static void addVertex(BufferBuilder buffer, Matrix4f matrix, float x, float y, float z, float red, float green, float blue, float alpha) {
        //? if >=1.21.1 {
        buffer.addVertex(matrix, x, y, z).setColor(red, green, blue, alpha);
        //?} else {
        /*buffer.vertex(matrix, x, y, z).color(red, green, blue, alpha).endVertex();
        *///?}
    }

    private static void drawFilledBox(BufferBuilder buffer, Matrix4f matrix, double minX, double minY, double minZ,
                                        double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        // Bottom face
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) maxZ, red, green, blue, alpha);

        // Top face
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) minZ, red, green, blue, alpha);

        // North face
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) minZ, red, green, blue, alpha);

        // South face
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) maxZ, red, green, blue, alpha);

        // West face
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) minZ, red, green, blue, alpha);

        // East face
        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) maxZ, red, green, blue, alpha);
    }

    private static void drawOutline(BufferBuilder buffer, Matrix4f matrix, double minX, double minY, double minZ,
                                      double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        // Bottom edges
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) minZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) maxZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) maxZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) minX, (float) minY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) minZ, red, green, blue, alpha);

        // Top edges
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) minZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) maxZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) maxZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) minZ, red, green, blue, alpha);

        // Vertical edges
        addVertex(buffer, matrix, (float) minX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) minZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) minZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) minZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) maxX, (float) minY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) maxX, (float) maxY, (float) maxZ, red, green, blue, alpha);

        addVertex(buffer, matrix, (float) minX, (float) minY, (float) maxZ, red, green, blue, alpha);
        addVertex(buffer, matrix, (float) minX, (float) maxY, (float) maxZ, red, green, blue, alpha);
    }
}
