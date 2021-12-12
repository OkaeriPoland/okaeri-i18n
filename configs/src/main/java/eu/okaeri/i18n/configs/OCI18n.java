package eu.okaeri.i18n.configs;

import eu.okaeri.i18n.SimpleI18n;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class OCI18n<S, R, D> extends SimpleI18n<String, R, D> {

    private final Map<Locale, Map<String, S>> configs = new ConcurrentHashMap<>();

    public OCI18n<S, R, D> registerConfig(@NonNull Locale locale, @NonNull LocaleConfig config) {

        Map<String, Object> configMap = config.asMap(config.getConfigurer(), true);
        Map<String, S> map = new LinkedHashMap<>();

        configMap.forEach((key, value) -> {
            String strValue = (String) value;
            if (strValue == null) return;
            map.put(key, this.storeConfigValue(locale, value));
        });

        this.configs.put(locale, map);
        return this;
    }

    @Override
    public R get(@NonNull Object entity, @NonNull String key) {

        Locale locale = super.getLocale(entity);
        Map<String, S> map = this.configs.get(locale);

        if (map == null) {
            map = this.configs.get(Locale.forLanguageTag(locale.getLanguage()));
        }

        if (map == null) {
            map = this.configs.get(this.getDefaultLocale());
        }

        if (map == null) {
            throw new RuntimeException("cannot find config for " + locale + " [available: " + this.configs.keySet().stream()
                .map(Locale::toString)
                .collect(Collectors.joining(", ")) + "]");
        }

        S message = map.get(key);
        if (message != null) {
            return this.createMessageFromStored(message, key);
        }

        Map<String, S> defaultLocaleMap = this.configs.get(this.getDefaultLocale());
        if (defaultLocaleMap != null) {
            message = defaultLocaleMap.get(key);
        }

        return this.createMessageFromStored(message, key);
    }

    @Override
    public D get(@NonNull String key) {
        throw new RuntimeException("feature not implemented yet");
    }

    public abstract S storeConfigValue(@NonNull Locale locale, @NonNull Object value);

    public abstract R createMessageFromStored(@Nullable S object, @NonNull String key);
}
