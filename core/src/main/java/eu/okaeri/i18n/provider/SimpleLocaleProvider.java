package eu.okaeri.i18n.provider;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleLocaleProvider<T> implements LocaleProvider<T> {

    public static <A> SimpleLocaleProvider<A> of(@NonNull Class<A> type, @NonNull Function<A, Locale> localeFunction) {
        return new SimpleLocaleProvider<A>(type, localeFunction);
    }

    private final Class<?> type;
    private final Function<T, Locale> localeFunction;

    @Override
    public boolean supports(Class<?> type) {
        return this.type.isAssignableFrom(type);
    }

    @Override
    public Locale getLocale(T entity) {
        return this.localeFunction.apply(entity);
    }
}
