package forestry.block;

import forestry.block.entity.CarpenterBlockEntity;
import forestry.block.entity.ForestryBlockEntities;
import net.minecraft.block.entity.BlockEntityType;

public class CarpenterBlock extends MachineBlock<CarpenterBlockEntity> {

    public CarpenterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntityType<CarpenterBlockEntity> getType() {
        return ForestryBlockEntities.CARPENTER;
    }
}
