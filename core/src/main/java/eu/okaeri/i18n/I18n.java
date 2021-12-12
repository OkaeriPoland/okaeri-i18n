package eu.okaeri.i18n;

import eu.okaeri.i18n.provider.LocaleProvider;
import lombok.NonNull;

import java.util.Locale;

public interface I18n<K, M, D> {

    <T> I18n registerLocaleProvider(@NonNull LocaleProvider<T> localeProvider);

    <T> I18n registerLocaleProvider(@NonNull Class<?> overrideType, @NonNull LocaleProvider<T> localeProvider);

    Locale getDefaultLocale();

    LocaleProvider<?> getLocaleProvider(@NonNull Class<?> entityType);

    Locale getLocale(@NonNull Object entity);

    M get(@NonNull Object entity, @NonNull K key);

    D get(@NonNull K key);
}
