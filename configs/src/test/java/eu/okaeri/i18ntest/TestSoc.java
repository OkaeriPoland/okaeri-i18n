package eu.okaeri.i18ntest;

import eu.okaeri.bin.Bin;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.binary.obdf.ObdfConfigurer;
import eu.okaeri.i18n.configs.LocaleConfigManager;
import eu.okaeri.i18n.configs.impl.OCI18n;
import eu.okaeri.i18ntest.helper.CommandSender;
import eu.okaeri.i18ntest.helper.TestLocaleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSoc {

    private OCI18n i18n;
    private TestLocaleConfig messages;

    @BeforeEach
    public void prepare() {
        this.i18n = new OCI18n();
        this.messages = LocaleConfigManager.createTemplate(TestLocaleConfig.class);
    }

    @Test
    public void test_soc_1() {

        this.i18n.registerConfig(Locale.ENGLISH, ConfigManager.create(TestLocaleConfig.class, it -> {
            it.withConfigurer(new ObdfConfigurer());
            it.load(Bin.of(Collections.singletonMap("example-message", "Example Message!")).write());
        }));

        this.i18n.registerConfig(Locale.forLanguageTag("es"), ConfigManager.create(TestLocaleConfig.class, it -> {
            it.withConfigurer(new ObdfConfigurer());
            it.load(Bin.of(Collections.singletonMap("example-message", "Mensaje de ejemplo!")).write());
        }));

        this.i18n.registerLocaleProvider(CommandSender.class, CommandSender::getLocale);
        this.i18n.setDefaultLocale(Locale.ENGLISH);

        String message1 = this.i18n.get(new CommandSender("Player1", Locale.ENGLISH), this.messages.getExampleMessage());
        assertEquals("Example Message!", message1);

        String message2 = this.i18n.get(new CommandSender("Player2", Locale.forLanguageTag("es")), this.messages.getExampleMessage());
        assertEquals("Mensaje de ejemplo!", message2);

        String message3 = this.i18n.get(new CommandSender("Player3", Locale.GERMAN), this.messages.getExampleMessage());
        assertEquals("Example Message!", message3);
    }
}
