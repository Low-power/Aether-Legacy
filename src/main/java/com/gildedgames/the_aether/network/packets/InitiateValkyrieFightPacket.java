package com.gildedgames.the_aether.network.packets;

import com.gildedgames.the_aether.entities.bosses.valkyrie_queen.ValkyrieQueen;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.items.AetherItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InitiateValkyrieFightPacket extends AetherPacket<InitiateValkyrieFightPacket> {

	public int entityId;

	public InitiateValkyrieFightPacket() {

	}

	public InitiateValkyrieFightPacket(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
	}

	@Override
	public void handleClient(InitiateValkyrieFightPacket message, EntityPlayer player) {

	}

	@Override
	public void handleServer(InitiateValkyrieFightPacket message, EntityPlayer player) {
		int medal_count = 0;
		int slot_i = player.inventory.mainInventory.length - 1;
		do {
			ItemStack item_stack = player.inventory.mainInventory[slot_i];
			if(item_stack == null) continue;
			if(item_stack.getItem() != AetherItems.victory_medal) continue;
			int stack_count = item_stack.stackSize;
			if(medal_count + stack_count > 10) {
				item_stack.stackSize = medal_count + stack_count - 10;
				medal_count = 10;
				break;
			}
			medal_count += stack_count;
			player.inventory.setInventorySlotContents(slot_i, null);
		} while(medal_count < 10 && --slot_i >= 0);
		if(medal_count < 10) {
			ItemStack item_stack = new ItemStack(AetherItems.victory_medal, medal_count);
			player.entityDropItem(item_stack, 1F);
			return;
		}

		Entity entity = player.worldObj.getEntityByID(message.entityId);

		if (entity instanceof ValkyrieQueen) {
			((ValkyrieQueen)entity).setBossReady(true);
			PlayerAether.get(player).setFocusedBoss((ValkyrieQueen)entity);
		}
	}

}
