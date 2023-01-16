package eu.okaeri.i18n.core.minecraft.adventure;

import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import lombok.experimental.Delegate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;


public class AdventureBungeeMessage extends BungeeMessage {

    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.gson();

    @Delegate(excludes = Overrides.class) private final AdventureMessage adventure;

    protected AdventureBungeeMessage(@NonNull AdventureMessage message) {
        super(message.compiled(), message.context());
        this.adventure = message;
    }

    public static AdventureBungeeMessage of(Placeholders placeholders, @NonNull CompiledMessage compiled) {
        return new AdventureBungeeMessage(AdventureMessage.of(placeholders, compiled));
    }

    interface Overrides {
        Component component();
    }

    @Override
    public BaseComponent[] component() {
        String json = GSON_SERIALIZER.serialize(this.adventure.component());
        return ComponentSerializer.parse(json);
    }

    /**
     * @deprecated Unstable hack API.
     */
    @Deprecated
    public Component adventure() {
        return this.adventure.component();
    }
}
