package eu.okaeri.i18n.configs.impl;

import eu.okaeri.i18n.SimpleI18n;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.i18n.message.Message;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@EqualsAndHashCode(callSuper = false)
public class MOCI18n extends SimpleI18n<String, Message> {

    private final Map<Locale, Map<String, CompiledMessage>> configs = new ConcurrentHashMap<>();
    private Placeholders placeholders;

    public MOCI18n() {
        this(Placeholders.create());
    }

    public MOCI18n(Placeholders placeholders) {
        this.placeholders = placeholders;
    }

    public MOCI18n registerConfig(Locale locale, LocaleConfig config) {

        Map<String, Object> configMap = config.asMap(config.getConfigurer(), true);
        Map<String, CompiledMessage> map = new LinkedHashMap<>();

        configMap.forEach((key, value) -> {
            String strValue = (String) value;
            if (strValue == null) {
                return;
            }
            map.put(key, CompiledMessage.of(strValue));
        });

        this.configs.put(locale, map);
        return this;
    }

    @Override
    public Message get(Object entity, String key) {

        Locale locale = super.getLocale(entity);
        Map<String, CompiledMessage> map = this.configs.get(locale);

        if (map == null) {
            map = this.configs.get(this.getDefaultLocale());
        }

        if (map == null) {
            throw new RuntimeException("cannot find config for " + locale);
        }

        CompiledMessage message = map.get(key);
        if (message == null) {
            return Message.of("<" + key + ">");
        }

        return Message.of(this.placeholders, message);
    }
}
