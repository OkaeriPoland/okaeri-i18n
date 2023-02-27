package eu.okaeri.i18n.message;

import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;


@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleMessage implements Message {

    protected final CompiledMessage compiled;
    protected PlaceholderContext context;

    @Deprecated
    public static SimpleMessage of(@NonNull String raw) {
        return of(null, CompiledMessage.of(raw));
    }

    public static SimpleMessage of(@NonNull Locale locale, @NonNull String raw) {
        return of(null, locale, raw);
    }

    @Deprecated
    public static SimpleMessage of(Placeholders placeholders, @NonNull String raw) {
        return of(placeholders, CompiledMessage.of(raw));
    }

    public static SimpleMessage of(Placeholders placeholders, @NonNull Locale locale, @NonNull String raw) {
        return of(placeholders, CompiledMessage.of(locale, raw));
    }

    public static SimpleMessage of(Placeholders placeholders, @NonNull CompiledMessage compiled) {

        PlaceholderContext context = (placeholders == null)
            ? PlaceholderContext.of(compiled)
            : placeholders.contextOf(compiled);

        return new SimpleMessage(compiled, context);
    }

    public boolean isSimple() {
        return true;
    }

    @Override
    public SimpleMessage with(@NonNull String field, @Nullable Object value) {
        if (this.context == null) throw new IllegalArgumentException("context cannot be null");
        this.context.with(field, value);
        return this;
    }

    @Override
    public SimpleMessage with(@NonNull Map<String, Object> fields) {
        if (this.context == null) throw new IllegalArgumentException("context cannot be null");
        this.context.with(fields);
        return this;
    }

    @Override
    public SimpleMessage with(@NonNull PlaceholderContext context) {
        this.context = context;
        return this;
    }

    @Override
    public String apply() {
        if (this.context == null) throw new IllegalArgumentException("context cannot be null");
        return this.context.apply();
    }

    @Override
    public String raw() {
        return this.compiled().getRaw();
    }

    @Override
    public CompiledMessage compiled() {
        return this.compiled;
    }

    @Override
    public PlaceholderContext context() {
        return this.context;
    }
}
