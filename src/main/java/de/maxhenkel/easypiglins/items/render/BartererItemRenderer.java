package de.maxhenkel.easypiglins.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.easypiglins.blocks.ModBlocks;
import de.maxhenkel.easypiglins.blocks.tileentity.BartererTileentity;
import de.maxhenkel.easypiglins.blocks.tileentity.render.BartererRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.model.data.EmptyModelData;

public class BartererItemRenderer extends ItemStackTileEntityRenderer {

    private BartererRenderer renderer;
    private Minecraft minecraft;

    private CachedMap<ItemStack, BartererTileentity> cachedMap;

    public BartererItemRenderer() {
        cachedMap = new CachedMap<>(10_000L);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void func_239207_a_(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = new BartererRenderer(TileEntityRendererDispatcher.instance);
        }

        BlockState traderBlock = ModBlocks.BARTERER.getDefaultState();
        BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
        dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getCutoutMipped()), traderBlock, dispatcher.getModelForState(traderBlock), 0, 0, 0, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

        CompoundNBT blockEntityTag = itemStack.getChildTag("BlockEntityTag");
        if (blockEntityTag == null) {
            return;
        }

        BartererTileentity trader = cachedMap.get(itemStack, () -> {
            BartererTileentity bartererTileentity = new BartererTileentity();
            bartererTileentity.setFakeWorld(minecraft.world);
            bartererTileentity.read(null, blockEntityTag);
            return bartererTileentity;
        });
        renderer.render(trader, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
