package eu.okaeri.i18n.configs.impl;

import eu.okaeri.i18n.SimpleI18n;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.placeholders.Placeholders;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@EqualsAndHashCode(callSuper = false)
public class OCI18n extends SimpleI18n<String, String> {

    private Map<Locale, Map<String, String>> configs = new ConcurrentHashMap<>();
    private Placeholders placeholders;

    public OCI18n registerConfig(Locale locale, LocaleConfig config) {
        Map<String, Object> configMap = config.asMap(config.getConfigurer(), true);
        Map<String, String> map = new LinkedHashMap<>();
        configMap.forEach((key, value) -> map.put(key, (String) value));
        this.configs.put(locale, map);
        return this;
    }

    @Override
    public String get(Object entity, String key) {

        Locale locale = super.getLocale(entity);
        Map<String, String> map = this.configs.get(locale);

        if (map == null) {
            map = this.configs.get(this.getDefaultLocale());
        }

        if (map == null) {
            throw new RuntimeException("cannot find config for " + locale);
        }

        String message = map.get(key);
        if (message == null) {
            return "<" + key + ">";
        }

        return message;
    }
}