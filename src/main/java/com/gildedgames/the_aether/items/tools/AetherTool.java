package com.gildedgames.the_aether.items.tools;

import com.gildedgames.the_aether.items.util.AetherToolType;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import com.google.common.collect.Multimap;
import java.util.Random;
import java.util.Set;

public abstract class AetherTool extends ItemTool {

	private float attackDamage;

	private String tool_type_name;

	public Random random = new Random();

	public AetherToolType tool_type;

	public AetherTool(float damage, ToolMaterial toolMaterial, AetherToolType tool_type) {
		super(damage, toolMaterial, tool_type.getToolBlockSet());

		this.tool_type = tool_type;

		if (tool_type == AetherToolType.PICKAXE) {
			this.tool_type_name = "pickaxe";
		} else if (tool_type == AetherToolType.AXE) {
			this.tool_type_name = "axe";
		} else if (tool_type == AetherToolType.SHOVEL) {
			this.tool_type_name = "shovel";
		}

		this.setCreativeTab(AetherCreativeTabs.tools);
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String tool_type_name) {
		int level = super.getHarvestLevel(stack, tool_type_name);

		if (level == -1 && tool_type_name != null && tool_type_name.equals(this.tool_type_name)) {
			return this.toolMaterial.getHarvestLevel();
		}

		return level;
	}

	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack) {
		return this.tool_type.canHarvestBlock(this.toolMaterial, block);
	}

	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta) {
		for (String type : getToolClasses(stack)) {
			if (block.isToolEffective(type, meta))
				return this.efficiencyOnProperMaterial;
		}

		return this.tool_type.getStrVsBlock(stack, block) == 4.0F ? this.efficiencyOnProperMaterial : 1.0F;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return tool_type_name != null ? com.google.common.collect.ImmutableSet.of(tool_type_name) : super.getToolClasses(stack);
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Multimap getItemAttributeModifiers() {
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", (double) this.attackDamage, 0));
		return multimap;
	}

	public float getEffectiveSpeed() {
		return this.efficiencyOnProperMaterial;
	}

}
