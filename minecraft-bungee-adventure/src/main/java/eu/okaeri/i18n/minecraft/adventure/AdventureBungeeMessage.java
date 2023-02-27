package eu.okaeri.i18n.minecraft.adventure;

import eu.okaeri.i18n.minecraft.bungee.BungeeMessage;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public class AdventureBungeeMessage extends BungeeMessage {

    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.gson();

    private final AdventureMessage adventure;

    protected AdventureBungeeMessage(@NonNull AdventureMessage message) {
        super(message.compiled(), message.context());
        this.adventure = message;
    }

    public static AdventureBungeeMessage of(Placeholders placeholders, @NonNull CompiledMessage compiled) {
        return new AdventureBungeeMessage(AdventureMessage.of(placeholders, compiled));
    }

    @Override
    public boolean isSimple() {
        return this.adventure.isSimple();
    }

    @Override
    public BaseComponent[] component() {
        String json = GSON_SERIALIZER.serialize(this.adventure.component());
        return ComponentSerializer.parse(json);
    }

    @Override
    public AdventureBungeeMessage with(@NonNull String field, @Nullable Object value) {
        this.adventure.with(field, value);
        return this;
    }

    @Override
    public AdventureBungeeMessage with(@NonNull Map<String, Object> fields) {
        this.adventure.with(fields);
        return this;
    }

    @Override
    public AdventureBungeeMessage with(@NonNull PlaceholderContext context) {
        this.adventure.with(context);
        return this;
    }

    @Override
    public PlaceholderContext context() {
        return this.adventure.context();
    }

    @Override
    public String apply() {
        return this.adventure.apply();
    }

    /**
     * @deprecated Unstable hack API.
     */
    @Deprecated
    public Component adventure() {
        return this.adventure.component();
    }
}
