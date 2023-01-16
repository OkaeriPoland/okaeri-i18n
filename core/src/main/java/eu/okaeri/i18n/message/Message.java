package eu.okaeri.i18n.message;

import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public interface Message {

    /**
     * @deprecated Use {@link SimpleMessage} factory methods.
     */
    @Deprecated
    public static Message of(@NonNull String raw) {
        return SimpleMessage.of(raw);
    }

    /**
     * @deprecated Use {@link SimpleMessage} factory methods.
     */
    @Deprecated
    public static Message of(@NonNull Locale locale, @NonNull String raw) {
        return SimpleMessage.of(locale, raw);
    }

    /**
     * @deprecated Use {@link SimpleMessage} factory methods.
     */
    @Deprecated
    public static Message of(Placeholders placeholders, @NonNull String raw) {
        return SimpleMessage.of(placeholders, raw);
    }

    /**
     * @deprecated Use {@link SimpleMessage} factory methods.
     */
    @Deprecated
    public static Message of(Placeholders placeholders, @NonNull Locale locale, @NonNull String raw) {
        return SimpleMessage.of(placeholders, locale, raw);
    }

    /**
     * @deprecated Use {@link SimpleMessage} factory methods.
     */
    @Deprecated
    public static Message of(Placeholders placeholders, @NonNull CompiledMessage compiled) {
        return SimpleMessage.of(placeholders, compiled);
    }

    Message with(@NonNull String field, @Nullable Object value);

    Message with(@NonNull Map<String, Object> fields);

    Message with(@NonNull PlaceholderContext context);

    String apply();

    String raw();

    CompiledMessage compiled();

    PlaceholderContext context();
}
