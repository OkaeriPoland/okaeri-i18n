package eu.okaeri.i18n.minecraft.bungee;

import eu.okaeri.i18n.message.SimpleMessage;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class BungeeMessage extends SimpleMessage {

    protected BungeeMessage(CompiledMessage compiled, PlaceholderContext context) {
        super(compiled, context);
    }

    public BaseComponent[] component() {
        return TextComponent.fromLegacyText(this.apply());
    }
}
