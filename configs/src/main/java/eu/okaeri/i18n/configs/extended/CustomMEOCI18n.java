package eu.okaeri.i18n.configs.extended;

import eu.okaeri.i18n.message.Message;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import eu.okaeri.placeholders.message.part.MessageElement;
import eu.okaeri.placeholders.message.part.MessageStatic;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class CustomMEOCI18n<T extends Message> extends EOCI18n<T> {

    @Override
    public CompiledMessage storeConfigValue(@NonNull Locale locale, @NonNull Object value) {
        return CompiledMessage.of(locale, String.valueOf(value));
    }

    @Override
    public T createMessageFromStored(CompiledMessage compiled, @NonNull String key) {
        return this.assembleMessage(this.placeholders, (compiled == null)
            ? CompiledMessage.of(Locale.ENGLISH, "<" + key + ">")
            : compiled
        );
    }

    @Override
    public T get(@NonNull Object entity, @NonNull String key) {

        T message = super.get(entity, key);
        if (this.getPrefixProvider() == null) {
            return message;
        }

        List<MessageElement> messageElements = message.compiled().getParts();
        if (messageElements.isEmpty()) {
            return message;
        }

        MessageElement firstElement = messageElements.get(0);
        if (!(firstElement instanceof MessageStatic)) {
            return message;
        }

        MessageStatic staticElement = (MessageStatic) firstElement;
        String elementValue = staticElement.getValue();

        if ((this.getPrefixMarker() == null) || !elementValue.startsWith(this.getPrefixMarker())) {
            return message;
        }

        String prefix = this.getPrefixProvider().getPrefix(entity, key);
        String base = elementValue.substring(this.getPrefixMarker().length());

        List<MessageElement> elementsCopy = new ArrayList<>(messageElements);
        elementsCopy.set(0, MessageStatic.of(prefix + base));

        String raw = prefix + message.raw().substring(this.getPrefixMarker().length()); // wat
        CompiledMessage compiled = CompiledMessage.of(raw, elementsCopy);

        return this.assembleMessage(this.getPlaceholders(), compiled);
    }

    public abstract T assembleMessage(Placeholders placeholders, @NonNull CompiledMessage compiled);
}
