package eu.okaeri.i18n.configs.impl;

import eu.okaeri.i18n.configs.OCI18n;
import eu.okaeri.i18n.message.Message;
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
public class MOCI18n extends OCI18n<CompiledMessage, Message, MessageDispatcher<Message>> {

    private Placeholders placeholders;

    public MOCI18n() {
        this(Placeholders.create());
    }

    public MOCI18n(Placeholders placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public CompiledMessage storeConfigValue(@NonNull Locale locale, @NonNull Object value) {
        return CompiledMessage.of(locale, String.valueOf(value));
    }

    @Override
    public Message createMessageFromStored(@Nullable CompiledMessage object, @NonNull String key) {
        if (object == null) {
            return Message.of("<" + key + ">");
        }
        return Message.of(this.placeholders, object);
    }
}
