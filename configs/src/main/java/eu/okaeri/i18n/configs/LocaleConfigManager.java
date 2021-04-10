package eu.okaeri.i18n.configs;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.Configurer;
import eu.okaeri.configs.configurer.InMemoryConfigurer;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.schema.ConfigDeclaration;
import eu.okaeri.configs.schema.FieldDeclaration;

import java.io.File;

public final class LocaleConfigManager {

    public static <T extends OkaeriConfig> T createTemplate(Class<T> clazz) throws OkaeriException {

        T config = ConfigManager.create(clazz);
        config.withConfigurer(new InMemoryConfigurer());
        config.update();

        ConfigDeclaration declaration = config.getDeclaration();
        for (FieldDeclaration field : declaration.getFields()) {
            field.updateValue(field.getName());
        }

        return config;
    }

    public static <T extends OkaeriConfig> T create(Class<T> clazz, Configurer configurer, File bindFile) throws OkaeriException {
        return ConfigManager.create(clazz, it -> {
            it.withConfigurer(configurer);
            it.withBindFile(bindFile);
            it.getDeclaration().getFields().forEach(field -> field.updateValue(null));
        });
    }
}
