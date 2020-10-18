package com.gildedgames.the_aether.client.gui.dialogue.entity;

import com.gildedgames.the_aether.client.gui.dialogue.DialogueOption;
import com.gildedgames.the_aether.client.gui.dialogue.DialogueGui;
import com.gildedgames.the_aether.entities.bosses.valkyrie_queen.ValkyrieQueen;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.network.packets.InitiateValkyrieFightPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;

public class ValkyrieDialogueGui extends DialogueGui {

	private ValkyrieQueen valkyrieQueen;
	private EntityPlayer player;
	private String title;
	private boolean have_enough_medals = false;

	public ValkyrieDialogueGui(ValkyrieQueen valkyrieQueen, EntityPlayer player) {
		super("[\247e" + valkyrieQueen.getName() + ", the Valkyrie Queen\247r]", new DialogueOption[]{new DialogueOption("What can you tell me about this place?"), new DialogueOption("I wish to fight you!"), new DialogueOption("Nevermind")});

		this.title = getDialogue();
		this.valkyrieQueen = valkyrieQueen;
		this.player = player;
	}

	@Override
	public void dialogueClicked(DialogueOption dialogue) {
		if (this.getDialogueOptions().size() == 3) {
			if (dialogue.getDialogueId() == 0) {
				this.addDialogueMessage(this.title + ": This is a sanctuary for us Valkyries who seek rest.");
				this.dialogueTreeCompleted();
			} else if (dialogue.getDialogueId() == 1) {
				DialogueOption medalDialogue = new DialogueOption(this.getMedalDiaulogue());

				this.addDialogueWithOptions(this.title + ": Very well then. Bring me ten medals from my subordinates to prove your worth, then we'll see.", medalDialogue, new DialogueOption("On second thought, i'd rather not."));
			} else if (dialogue.getDialogueId() == 2) {
				this.addDialogueMessage(this.title + ": Goodbye adventurer.");
				this.dialogueTreeCompleted();
			}
		} else {
			if (dialogue.getDialogueId() == 0) {
				if (this.mc.theWorld.difficultySetting == EnumDifficulty.PEACEFUL) {
					this.addDialogueMessage(this.title + ": Sorry, I don't fight with weaklings.");
					this.dialogueTreeCompleted();

					return;
				}

				if (this.have_enough_medals) {
					AetherNetwork.sendToServer(new InitiateValkyrieFightPacket(this.valkyrieQueen.getEntityId()));

					this.valkyrieQueen.setBossReady(true);
					this.addDialogueMessage(this.title + ": Now then, let's begin!");
					this.dialogueTreeCompleted();
				} else {
					this.addDialogueMessage(this.title + ": Take your time.");
					this.dialogueTreeCompleted();
				}
			} else if (dialogue.getDialogueId() == 1) {
				this.addDialogueMessage(this.title + ": So be it then. Goodbye adventurer.");
				this.dialogueTreeCompleted();
			}
		}
	}

	private static int get_medal_count(EntityPlayer player) {
		int count = 0;

		for (ItemStack item : player.inventory.mainInventory) {
			if(item == null || item.getItem() != AetherItems.victory_medal) continue;
			count += item.stackSize;
		}

		return count;
	}

	private String getMedalDiaulogue() {
		int count = get_medal_count(player);
		if(count < 10) return String.format("I'll return when I have them. (%d/10)", count);
		have_enough_medals = true;
		return "I'm ready, I have the medals right here!";
	}

}
