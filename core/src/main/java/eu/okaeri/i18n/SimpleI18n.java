package eu.okaeri.i18n;

import eu.okaeri.i18n.locale.LocaleProvider;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
public abstract class SimpleI18n<K, M, D> implements I18n<K, M, D> {

    private final List<LocaleProvider<?>> localeProviders = new ArrayList<>();
    private Locale defaultLocale = Locale.getDefault();

    @Override
    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    @Override
    public <T> I18n registerLocaleProvider(@NonNull LocaleProvider<T> localeProvider) {
        this.localeProviders.add(localeProvider);
        return this;
    }

    @Override
    public <T> I18n registerLocaleProvider(@NonNull Class<?> overrideType, @NonNull LocaleProvider<T> localeProvider) {
        List<LocaleProvider<?>> providers = this.localeProviders.stream()
            .filter(provider -> !provider.supports(overrideType))
            .collect(Collectors.toList());
        providers.add(localeProvider);
        this.localeProviders.clear();
        this.localeProviders.addAll(providers);
        return this;
    }

    @Override
    public LocaleProvider getLocaleProvider(@NonNull Class<?> entityType) {
        return this.localeProviders.stream()
            .filter(provider -> provider.supports(entityType))
            .findAny()
            .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Locale getLocale(@NonNull Object entity) {

        if (entity instanceof Locale) {
            return (Locale) entity;
        }

        LocaleProvider localeProvider = this.getLocaleProvider(entity.getClass());
        if (localeProvider == null) {
            return this.getDefaultLocale();
        }

        Locale locale = localeProvider.getLocale(entity);
        if (locale == null) {
            return this.getDefaultLocale();
        }

        return locale;
    }
}
