package eu.okaeri.i18ntest.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Locale;

@Data
@AllArgsConstructor
public class CommandSender {
    private final String name;
    private final Locale locale;
}
