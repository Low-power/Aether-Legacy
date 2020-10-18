package com.gildedgames.the_aether.client.gui.dialogue.server;

import com.gildedgames.the_aether.client.gui.dialogue.DialogueOption;
import com.gildedgames.the_aether.client.gui.dialogue.DialogueGui;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.network.packets.PacketDialogueClicked;
import com.google.common.collect.Lists;
import java.util.ArrayList;

public class ServerDialogueGui extends DialogueGui {

    private String dialogueName;

    public ServerDialogueGui(String dialogueName, String dialogue, ArrayList<String> dialogueText) {
        super(dialogue);

        this.dialogueName = dialogueName;

        ArrayList<DialogueOption> dialogueOptions = Lists.newArrayList();

        for (String dialogueForOption : dialogueText) {
            dialogueOptions.add(new DialogueOption(dialogueForOption));
        }

        this.addDialogueOptions(dialogueOptions.toArray(new DialogueOption[]{}));
    }

    @Override
    public void dialogueClicked(DialogueOption dialogue) {
        AetherNetwork.sendToServer(new PacketDialogueClicked(this.dialogueName, dialogue.getDialogueId()));
        this.dialogueTreeCompleted();
    }

}
