package eu.okaeri.i18n.configs;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.Configurer;
import eu.okaeri.configs.configurer.InMemoryConfigurer;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.schema.ConfigDeclaration;
import eu.okaeri.configs.schema.FieldDeclaration;
import lombok.NonNull;

import java.io.File;

public final class LocaleConfigManager {

    public static <T extends OkaeriConfig> T createTemplate(@NonNull Class<T> clazz) throws OkaeriException {

        T config = ConfigManager.create(clazz);
        config.withConfigurer(new InMemoryConfigurer());
        config.update();

        ConfigDeclaration declaration = config.getDeclaration();
        for (FieldDeclaration field : declaration.getFields()) {
            field.updateValue(field.getName());
        }

        return config;
    }

    public static <T extends OkaeriConfig> T create(@NonNull Class<T> clazz, @NonNull Configurer configurer, @NonNull File bindFile, boolean clearValues) throws OkaeriException {
        return ConfigManager.create(clazz, it -> {
            it.withConfigurer(configurer);
            it.withBindFile(bindFile);
            if (clearValues) it.getDeclaration().getFields().forEach(field -> field.updateValue(null));
        });
    }
}
