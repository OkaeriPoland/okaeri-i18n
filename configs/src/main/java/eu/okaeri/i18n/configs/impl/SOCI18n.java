package eu.okaeri.i18n.configs.impl;

import eu.okaeri.i18n.configs.OCI18n;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = false)
public class SOCI18n extends OCI18n<String, String> {

    @Override
    public String storeConfigValue(Locale locale, Object value) {
        return String.valueOf(value);
    }

    @Override
    public String createMessageFromStored(String object, String key) {
        if (object == null) {
            return "<" + key + ">";
        }
        return object;
    }
}