package blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

public class RubyOre extends OreBlock{
	
	public RubyOre() {
		super(Block.Properties.create(Material.ROCK)
			.hardnessAndResistance(3.0f, 3.0f)
			.sound(SoundType.STONE)
			.harvestLevel(2)
			.harvestTool(ToolType.PICKAXE)
			.setRequiresTool()
		);
	}
	
	public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return 1;
	}

}
