package eu.okaeri.i18ntest;

import eu.okaeri.bin.Bin;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.binary.obdf.ObdfConfigurer;
import eu.okaeri.i18n.configs.LocaleConfigManager;
import eu.okaeri.i18n.configs.impl.MOCI18n;
import eu.okaeri.i18n.message.Message;
import eu.okaeri.i18ntest.helper.CommandSender;
import eu.okaeri.i18ntest.helper.TestLocaleConfig;
import eu.okaeri.placeholders.Placeholders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMoc {

    private MOCI18n i18n;
    private TestLocaleConfig messages;

    @BeforeEach
    public void prepare() {
        this.i18n = new MOCI18n();
        this.messages = LocaleConfigManager.createTemplate(TestLocaleConfig.class);
    }

    @Test
    public void test_moc_1() {

        this.i18n.registerConfig(Locale.ENGLISH, ConfigManager.create(TestLocaleConfig.class, it -> {
            it.withConfigurer(new ObdfConfigurer());
            it.load(Bin.of(Collections.singletonMap("example-message", "Hello {who|unknown}!")).write());
        }));

        this.i18n.registerConfig(Locale.forLanguageTag("es"), ConfigManager.create(TestLocaleConfig.class, it -> {
            it.withConfigurer(new ObdfConfigurer());
            it.load(Bin.of(Collections.singletonMap("example-message", "Hola {who|desconocido}!")).write());
        }));

        this.i18n.registerLocaleProvider(CommandSender.class, CommandSender::getLocale);
        this.i18n.setDefaultLocale(Locale.ENGLISH);

        Message message1 = this.i18n.get(new CommandSender("Player1", Locale.ENGLISH), this.messages.getExampleMessage());
        assertEquals("Hello World!", message1.with("who", "World").apply());

        Message message2 = this.i18n.get(new CommandSender("Player2", Locale.forLanguageTag("es")), this.messages.getExampleMessage());
        assertEquals("Hola Mundo!", message2.with("who", "Mundo").apply());

        Message message3 = this.i18n.get(new CommandSender("Player3", Locale.GERMAN), this.messages.getExampleMessage());
        assertEquals("Hello World!", message3.with("who", "World").apply());

        Message message4 = this.i18n.get(new CommandSender("Player2", Locale.forLanguageTag("es")), this.messages.getExampleMessage());
        assertEquals("Hola desconocido!", message4.apply());
    }

    @Test
    public void test_moc_2() {

        this.i18n.setPlaceholders(Placeholders.create()
                .registerPlaceholder(CommandSender.class, "name", CommandSender::getName));

        this.i18n.registerConfig(Locale.ENGLISH, ConfigManager.create(TestLocaleConfig.class, it -> {
            it.withConfigurer(new ObdfConfigurer());
            it.load(Bin.of(Collections.singletonMap("example-message", "Hello {who.name|unknown}!")).write());
        }));

        this.i18n.registerConfig(Locale.forLanguageTag("es"), ConfigManager.create(TestLocaleConfig.class, it -> {
            it.withConfigurer(new ObdfConfigurer());
            it.load(Bin.of(Collections.singletonMap("example-message", "Hola {who.name|desconocido}!")).write());
        }));

        this.i18n.registerLocaleProvider(CommandSender.class, CommandSender::getLocale);
        this.i18n.setDefaultLocale(Locale.ENGLISH);

        CommandSender englishSender = new CommandSender("Player1", Locale.ENGLISH);
        Message message1 = this.i18n.get(englishSender, this.messages.getExampleMessage());
        assertEquals("Hello Player1!", message1.with("who", englishSender).apply());

        CommandSender spanishSender = new CommandSender("Player2", Locale.forLanguageTag("es"));
        Message message2 = this.i18n.get(spanishSender, this.messages.getExampleMessage());
        assertEquals("Hola Player2!", message2.with("who", spanishSender).apply());

        CommandSender germanSender = new CommandSender("Player3", Locale.GERMAN);
        Message message3 = this.i18n.get(germanSender, this.messages.getExampleMessage());
        assertEquals("Hello Player3!", message3.with("who", germanSender).apply());

        Message message4 = this.i18n.get(spanishSender, this.messages.getExampleMessage());
        assertEquals("Hola desconocido!", message4.apply());
    }
}
