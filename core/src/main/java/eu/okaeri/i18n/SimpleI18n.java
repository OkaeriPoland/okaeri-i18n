package eu.okaeri.i18n;

import eu.okaeri.i18n.provider.LocaleProvider;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
public abstract class SimpleI18n<K, M> implements I18n<K, M> {

    private final List<LocaleProvider<?>> localeProviders = new ArrayList<>();
    private Locale defaultLocale = Locale.getDefault();

    @Override
    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    @Override
    public <T> I18n registerLocaleProvider(LocaleProvider<T> localeProvider) {
        this.localeProviders.add(localeProvider);
        return this;
    }

    @Override
    public <T> I18n registerLocaleProvider(Class<?> overrideType, LocaleProvider<T> localeProvider) {
        List<LocaleProvider<?>> providers = this.localeProviders.stream()
                .filter(provider -> !provider.supports(overrideType))
                .collect(Collectors.toList());
        providers.add(localeProvider);
        this.localeProviders.clear();
        this.localeProviders.addAll(providers);
        return this;
    }

    @Override
    public LocaleProvider getLocaleProvider(Class<?> entityType) {
        return this.localeProviders.stream()
                .filter(provider -> provider.supports(entityType))
                .findAny()
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Locale getLocale(Object entity) {

        if (entity == null) throw new IllegalArgumentException("entity cannot be null");
        LocaleProvider localeProvider = this.getLocaleProvider(entity.getClass());

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
