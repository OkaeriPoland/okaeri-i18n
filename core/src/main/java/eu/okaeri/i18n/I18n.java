package eu.okaeri.i18n;

import eu.okaeri.i18n.provider.LocaleProvider;

import java.util.Locale;

public interface I18n<K, M, D> {
    <T> I18n registerLocaleProvider(LocaleProvider<T> localeProvider);
    <T> I18n registerLocaleProvider(Class<?> overrideType, LocaleProvider<T> localeProvider);
    Locale getDefaultLocale();
    LocaleProvider<?> getLocaleProvider(Class<?> entityType);
    Locale getLocale(Object entity);
    M get(Object entity, K key);
    D get(K key);
}
