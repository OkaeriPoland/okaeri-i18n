package eu.okaeri.i18n.core.minecraft.adventure;

import eu.okaeri.i18n.message.Message;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.awt.*;

@RequiredArgsConstructor
public class BungeeAdventureMessage implements Message {

    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.gson();

    @Delegate(excludes = Overrides.class) private final AdventureMessage adventure;

    public static BungeeAdventureMessage of(Placeholders placeholders, @NonNull CompiledMessage compiled) {
        return new BungeeAdventureMessage(AdventureMessage.of(placeholders, compiled));
    }

    interface Overrides {
        Component component();
    }

    public BaseComponent[] component() {
        String json = GSON_SERIALIZER.serialize(this.adventure.component());
        return ComponentSerializer.parse(json);
    }
}
