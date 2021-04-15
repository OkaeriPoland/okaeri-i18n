package eu.okaeri.i18n.message;

import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.*;


@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    public static Message of(String raw) {
        return of(null, raw);
    }

    public static Message of(Placeholders placeholders, String raw) {
        return of(placeholders, CompiledMessage.of(raw));
    }

    public static Message of(Placeholders placeholders, CompiledMessage compiled) {

        PlaceholderContext context = (placeholders == null)
                ? PlaceholderContext.of(compiled)
                : placeholders.contextOf(compiled);

        return new Message(compiled, context);
    }

    private final CompiledMessage compiled;
    private final PlaceholderContext context;

    public Message with(String field, Object value) {
        if (this.context == null) throw new IllegalArgumentException("context cannot be null");
        this.context.with(field, value);
        return this;
    }

    public String apply() {
        if (this.context == null) throw new IllegalArgumentException("context cannot be null");
        return this.context.apply();
    }

    public String source() {
        if (this.context == null) throw new IllegalArgumentException("compiled cannot be null");
        return this.compiled.getRaw();
    }
}
