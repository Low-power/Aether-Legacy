package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.tools.AetherTool;
import com.gildedgames.the_aether.items.tools.SkyrootTool;
import com.gildedgames.the_aether.items.util.AetherToolType;
import com.gildedgames.the_aether.items.util.DoubleDropHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.Random;

public class AetherOre extends Block {

	public AetherOre(int level) {
		super(Material.rock);

		this.setHardness(3F);
		this.setResistance(5F);
		this.setStepSound(soundTypeStone);
		this.setHarvestLevel("pickaxe", level);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		DoubleDropHelper.drop_block(player, x, y, z, this, meta, AetherToolType.PICKAXE);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return this == BlocksAether.zanite_ore ? AetherItems.zanite_gemstone : AetherItems.ambrosium_shard;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(0, random, fortune)) {
			int j = random.nextInt(fortune + 2) - 1;
			if (j < 0) j = 0;
			return this.quantityDropped(random) * (j + 1);
		} else {
			return this.quantityDropped(random);
		}
	}

	@Override
	public int getExpDrop(IBlockAccess p_149690_1_, int p_149690_5_, int p_149690_7_) {
		Random random = new Random();

		if (this.getItemDropped(p_149690_5_, random, p_149690_7_) != Item.getItemFromBlock(this)) {
			int amount = 0;

			if (this == BlocksAether.ambrosium_ore) {
				amount = MathHelper.getRandomIntegerInRange(random, 0, 2);
			} else if (this == BlocksAether.zanite_ore) {
				amount = MathHelper.getRandomIntegerInRange(random, 2, 5);
			}

			return amount;
		}

		return 0;
	}

}
