package eu.okaeri.i18n.configs.simple;

import eu.okaeri.i18n.configs.OCI18n;
import eu.okaeri.i18n.message.Message;
import eu.okaeri.i18n.message.SimpleMessage;
import eu.okaeri.i18n.message.MessageDispatcher;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = false)
public class MessageOCI18n extends OCI18n<CompiledMessage, Message, MessageDispatcher<Message>> {

    private Placeholders placeholders;

    public MessageOCI18n() {
        this(Placeholders.create());
    }

    public MessageOCI18n(Placeholders placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public CompiledMessage storeConfigValue(@NonNull Locale locale, @NonNull Object value) {
        return CompiledMessage.of(locale, String.valueOf(value));
    }

    @Override
    public Message createMessageFromStored(@Nullable CompiledMessage compiled, @NonNull String key) {
        return this.assembleMessage(this.placeholders, (compiled == null)
            ? CompiledMessage.of(Locale.ENGLISH, "<" + key + ">")
            : compiled
        );
    }

    public Message assembleMessage(Placeholders placeholders, @NonNull CompiledMessage compiled) {
        return SimpleMessage.of(placeholders, compiled);
    }
}
