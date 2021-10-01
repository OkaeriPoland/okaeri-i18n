package eu.okaeri.i18n.provider;

import lombok.NonNull;

import java.util.Locale;

public interface LocaleProvider<T> {
    boolean supports(@NonNull Class<?> type);
    Locale getLocale(@NonNull T entity);
}
