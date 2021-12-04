package forestry.block;

import forestry.block.entity.CarpenterBlockEntity;
import forestry.block.entity.ForestryBlockEntities;

public class CarpenterBlock extends MachineBlock<CarpenterBlockEntity> {

    public CarpenterBlock(Settings settings) {
        super(settings, ForestryBlockEntities.CARPENTER);
    }
}
