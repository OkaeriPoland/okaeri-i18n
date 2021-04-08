package eu.okaeri.i18n;

import eu.okaeri.i18n.provider.LocaleProvider;
import lombok.Data;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public abstract class SimpleI18n<K, M> implements I18n<K, M> {

    private final Map<Class<?>, LocaleProvider<?>> localeProviders = new ConcurrentHashMap<>();
    private Locale defaultLocale = Locale.getDefault();

    @Override
    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    @Override
    public <T> I18n registerLocaleProvider(Class<T> entityType, LocaleProvider<T> localeProvider) {
        this.localeProviders.put(entityType, localeProvider);
        return this;
    }

    @Override
    public LocaleProvider getLocaleProvider(Class<?> entityType) {
        return this.localeProviders.get(entityType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Locale getLocale(Object entity) {

        if (entity == null) throw new IllegalArgumentException("entity cannot be null");
        LocaleProvider localeProvider = this.localeProviders.get(entity.getClass());

        if (localeProvider == null) {
            return this.getDefaultLocale();
        }

        Locale locale = localeProvider.getLocale(entity);
        if (locale == null) {
            throw new RuntimeException("locale provider " + localeProvider.getClass().getSimpleName() + " returned null for: " + entity);
        }

        return locale;
    }
}
