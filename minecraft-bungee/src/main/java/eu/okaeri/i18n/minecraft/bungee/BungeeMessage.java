package eu.okaeri.i18n.minecraft.bungee;

import eu.okaeri.i18n.message.SimpleMessage;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BungeeMessage extends SimpleMessage {

    protected BungeeMessage(CompiledMessage compiled, PlaceholderContext context) {
        super(compiled, context);
    }

    public BaseComponent[] component() {
        return TextComponent.fromLegacyText(this.apply());
    }

    @Override
    public BungeeMessage with(@NonNull String field, @Nullable Object value) {
        return (BungeeMessage) super.with(field, value);
    }

    @Override
    public BungeeMessage with(@NonNull Map<String, Object> fields) {
        return (BungeeMessage) super.with(fields);
    }

    @Override
    public BungeeMessage with(@NonNull PlaceholderContext context) {
        return (BungeeMessage) super.with(context);
    }
}
