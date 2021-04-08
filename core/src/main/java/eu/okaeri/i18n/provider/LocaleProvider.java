package eu.okaeri.i18n.provider;

import java.util.Locale;

public interface LocaleProvider<T> {
    boolean supports(Class<?> type);
    Locale getLocale(T entity);
}
