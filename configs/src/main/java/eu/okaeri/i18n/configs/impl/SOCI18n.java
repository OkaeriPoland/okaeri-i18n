package eu.okaeri.i18n.configs.impl;

import eu.okaeri.i18n.configs.OCI18n;
import eu.okaeri.i18n.message.MessageDispatcher;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = false)
public class SOCI18n extends OCI18n<String, String, MessageDispatcher<String>> {

    @Override
    public String storeConfigValue(@NonNull Locale locale, @NonNull Object value) {
        return String.valueOf(value);
    }

    @Override
    public String createMessageFromStored(String object, @NonNull String key) {
        if (object == null) {
            return "<" + key + ">";
        }
        return object;
    }
}