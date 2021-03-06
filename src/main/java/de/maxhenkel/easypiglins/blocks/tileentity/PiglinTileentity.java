package de.maxhenkel.easypiglins.blocks.tileentity;

import de.maxhenkel.easypiglins.items.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class PiglinTileentity extends FakeWorldTileentity {

    private ItemStack piglin;
    private PiglinEntity piglinEntity;

    public PiglinTileentity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        piglin = ItemStack.EMPTY;
    }

    public ItemStack getPiglin() {
        if (piglinEntity != null) {
            ModItems.PIGLIN.setPiglin(piglin, piglinEntity);
        }
        return piglin;
    }

    public boolean hasPiglin() {
        return !piglin.isEmpty();
    }

    public PiglinEntity getPiglinEntity() {
        if (piglinEntity == null && !piglin.isEmpty()) {
            piglinEntity = ModItems.PIGLIN.getPiglin(world, piglin);
        }
        return piglinEntity;
    }

    public void setPiglin(ItemStack piglin) {
        this.piglin = piglin;

        if (piglin.isEmpty()) {
            piglinEntity = null;
        } else {
            piglinEntity = ModItems.PIGLIN.getPiglin(world, piglin);
            onAddPiglin(piglinEntity);
        }
        markDirty();
        sync();
    }

    protected void onAddPiglin(PiglinEntity piglin) {

    }

    public ItemStack removePiglin() {
        ItemStack v = getPiglin();
        setPiglin(ItemStack.EMPTY);
        return v;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (hasPiglin()) {
            CompoundNBT comp = new CompoundNBT();
            getPiglin().write(comp);
            compound.put("Piglin", comp);
        }
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        if (compound.contains("Piglin")) {
            CompoundNBT comp = compound.getCompound("Piglin");
            piglin = ItemStack.read(comp);
            piglinEntity = null;
        } else {
            removePiglin();
        }
        super.read(state, compound);
    }

}
