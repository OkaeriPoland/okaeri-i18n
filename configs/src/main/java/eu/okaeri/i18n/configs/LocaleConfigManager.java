package eu.okaeri.i18n.configs;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.InMemoryConfigurer;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.schema.ConfigDeclaration;
import eu.okaeri.configs.schema.FieldDeclaration;

public final class LocaleConfigManager {

    public static <T extends OkaeriConfig> T createTemplate(Class<T> clazz) throws OkaeriException {

        T config = ConfigManager.create(clazz);
        config.withConfigurer(new InMemoryConfigurer());
        config.update();

        ConfigDeclaration declaration = config.getDeclaration();
        for (FieldDeclaration field : declaration.getFields()) {

            if ("this$0".equals(field.getName())) {
                continue;
            }

            field.updateValue(field.getName());
        }

        return config;
    }
}
