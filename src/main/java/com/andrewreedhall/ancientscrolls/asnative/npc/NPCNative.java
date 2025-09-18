package com.andrewreedhall.ancientscrolls.asnative.npc;

import com.andrewreedhall.ancientscrolls.npc.AncientScrollsNPC;
import com.andrewreedhall.ancientscrolls.npc.NPCInstance;

public abstract class NPCNative extends AncientScrollsNPC {
    public NPCNative(final String id, final String name, final NPCInstance.Skin skin) {
        super(fromAncientScrollsNamespace(id), name, skin);
    }
}
