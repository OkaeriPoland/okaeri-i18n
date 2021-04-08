package eu.okaeri.i18n;

import eu.okaeri.i18n.provider.LocaleProvider;

import java.util.Locale;

public interface I18n<K, M> {
    <T> I18n registerLocaleProvider(Class<T> entityType, LocaleProvider<T> localeProvider);
    Locale getDefaultLocale();
    LocaleProvider<?> getLocaleProvider(Class<?> entityType);
    Locale getLocale(Object entity);
    M get(Object entity, K key);
}
