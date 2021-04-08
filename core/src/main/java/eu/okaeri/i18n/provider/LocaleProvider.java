package eu.okaeri.i18n.provider;

import java.util.Locale;

public interface LocaleProvider<T> {
    Locale getLocale(T entity);
}
