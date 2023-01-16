package eu.okaeri.i18ntest;

import eu.okaeri.bin.Bin;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.binary.obdf.ObdfConfigurer;
import eu.okaeri.i18n.configs.LocaleConfigManager;
import eu.okaeri.i18n.configs.simple.StringOCI18n;
import eu.okaeri.i18n.locale.SimpleLocaleProvider;
import eu.okaeri.i18ntest.helper.CommandSender;
import eu.okaeri.i18ntest.helper.TestLocaleConfig;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSoc {

    @Test
    public void test_soc_1() {

        StringOCI18n i18n = new StringOCI18n();
        TestLocaleConfig messages = LocaleConfigManager.createTemplate(TestLocaleConfig.class);

        i18n.registerConfig(Locale.ENGLISH, ConfigManager.create(TestLocaleConfig.class, it -> {
            it.withConfigurer(new ObdfConfigurer());
            it.load(Bin.of(Collections.singletonMap("example-message", "Example Message!")).write());
        }));

        i18n.registerConfig(Locale.forLanguageTag("es"), ConfigManager.create(TestLocaleConfig.class, it -> {
            it.withConfigurer(new ObdfConfigurer());
            it.load(Bin.of(Collections.singletonMap("example-message", "Mensaje de ejemplo!")).write());
        }));

        i18n.registerLocaleProvider(SimpleLocaleProvider.of(CommandSender.class, CommandSender::getLocale));
        i18n.setDefaultLocale(Locale.ENGLISH);

        String message1 = i18n.get(new CommandSender("Player1", Locale.ENGLISH), messages.getExampleMessage());
        assertEquals("Example Message!", message1);

        String message2 = i18n.get(new CommandSender("Player2", Locale.forLanguageTag("es")), messages.getExampleMessage());
        assertEquals("Mensaje de ejemplo!", message2);

        String message3 = i18n.get(new CommandSender("Player3", Locale.GERMAN), messages.getExampleMessage());
        assertEquals("Example Message!", message3);
    }
}
