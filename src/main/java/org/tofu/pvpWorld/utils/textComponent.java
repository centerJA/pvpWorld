package org.tofu.pvpWorld.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class textComponent {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    private static final LegacyComponentSerializer legacy = LegacyComponentSerializer.builder().character('§').hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    private static final PlainTextComponentSerializer plain = PlainTextComponentSerializer.plainText();


    //textComponent.purse("<red>test</red>")
    public static Component parse(String message) {
        return (message == null) ? Component.empty() : mm.deserialize(message);
    }
}
