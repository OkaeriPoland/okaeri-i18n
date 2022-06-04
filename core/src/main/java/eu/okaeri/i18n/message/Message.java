package eu.okaeri.i18n.message;

import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    protected final CompiledMessage compiled;
    protected PlaceholderContext context;

    public static Message of(@NonNull String raw) {
        return of(null, raw);
    }

    public static Message of(Placeholders placeholders, @NonNull String raw) {
        return of(placeholders, CompiledMessage.of(raw));
    }

    public static Message of(Placeholders placeholders, @NonNull CompiledMessage compiled) {

        PlaceholderContext context = (placeholders == null)
            ? PlaceholderContext.of(compiled)
            : placeholders.contextOf(compiled);

        return new Message(compiled, context);
    }

    public Message with(@NonNull String field, @Nullable Object value) {
        if (this.context == null) throw new IllegalArgumentException("context cannot be null");
        this.context.with(field, value);
        return this;
    }

    public Message with(@NonNull Map<String, Object> fields) {
        if (this.context == null) throw new IllegalArgumentException("context cannot be null");
        this.context.with(fields);
        return this;
    }

    public Message with(@NonNull PlaceholderContext context) {
        this.context = context;
        return this;
    }

    public String apply() {
        if (this.context == null) throw new IllegalArgumentException("context cannot be null");
        return this.context.apply();
    }

    public String raw() {
        return this.compiled().getRaw();
    }

    public CompiledMessage compiled() {
        return this.compiled;
    }

    public PlaceholderContext context() {
        return this.context;
    }
}
