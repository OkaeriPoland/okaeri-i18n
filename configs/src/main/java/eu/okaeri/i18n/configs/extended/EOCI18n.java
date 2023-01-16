package eu.okaeri.i18n.configs.extended;

import eu.okaeri.configs.schema.FieldDeclaration;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.i18n.configs.OCI18n;
import eu.okaeri.i18n.extended.MessageColors;
import eu.okaeri.i18n.extended.PrefixProvider;
import eu.okaeri.i18n.message.MessageDispatcher;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.*;

import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public abstract class EOCI18n<T> extends OCI18n<CompiledMessage, T, MessageDispatcher<T>> {

    private static final Pattern MESSAGE_FIELD_PATTERN = Pattern.compile("\\{[^{]+}");

    protected final @Getter Map<Locale, LocaleConfig> configs = new HashMap<>();
    protected final @Getter Map<Locale, Map<String, Object>> rawConfigs = new HashMap<>();

    protected @Getter @Setter String prefixField;
    protected @Getter @Setter String prefixMarker;

    protected @Getter @Setter Placeholders placeholders;
    protected @Getter @Setter PrefixProvider prefixProvider;

    @Override
    public EOCI18n<T> registerConfig(@NonNull Locale locale, @NonNull LocaleConfig config) {
        this.rawConfigs.putIfAbsent(locale, config.asMap(config.getConfigurer(), true));
        this.update(locale, config);
        this.configs.put(locale, config);
        return (EOCI18n<T>) super.registerConfig(locale, config);
    }

    protected void update(@NonNull Locale locale, @NonNull LocaleConfig config) {

        // load from file
        if ((config.getBindFile() != null) && Files.exists(config.getBindFile())) {
            config.load(true);
        }
        // use previous known if available
        else if (this.rawConfigs.containsKey(locale)) {
            config.load(this.rawConfigs.get(locale));
        }

        // update fields
        for (FieldDeclaration field : config.getDeclaration().getFields()) {

            if (!(field.getValue() instanceof String)) {
                continue;
            }

            String fieldName = field.getName().toLowerCase(Locale.ROOT);
            String fieldValue = String.valueOf(field.getValue());

            // ignore prefix/coloring of empty messages
            if (fieldValue.isEmpty()) {
                continue;
            }

            // resolve prefix or empty if not applicable
            String localPrefix = "";
            if ((this.getPrefixMarker() != null) && fieldValue.startsWith(this.getPrefixMarker())) {
                fieldValue = fieldValue.substring(this.getPrefixMarker().length());
                localPrefix = this.getPrefixMarker();
            }

            // do not auto-color messages with predefined colors
            if (this.hasColors(fieldValue)) {
                field.updateValue(localPrefix + this.color(fieldValue));
                continue;
            }

            // add colors based on the matchers
            Optional<MessageColors> colorsOptional = this.matchColors(fieldName);
            if (colorsOptional.isPresent()) {
                // fields + message color
                MessageColors colors = colorsOptional.get();
                if (colors.getFieldsColor() != null) {
                    fieldValue = MESSAGE_FIELD_PATTERN.matcher(fieldValue).replaceAll(colors.getFieldsColor() + "$0" + colors.getMessageColor());
                }
                // just message color
                field.updateValue(localPrefix + colors.getMessageColor() + fieldValue);
            }
        }

        String prefix = this.color((String) config.getDeclaration().getFields().stream()
            .filter(field -> Objects.equals(this.getPrefixField(), field.getField().getName()))
            .findFirst()
            .map(FieldDeclaration::getValue)
            .orElse(""));

        this.setPrefixProvider((entity, key) -> prefix);
    }

    /**
     * Override this method for config reload support.
     */
    public void load() {
    }

    /**
     * Override this method for field auto color support.
     */
    protected boolean hasColors(@NonNull String text) {
        return false;
    }

    /**
     * Override this method for field auto color support.
     */
    public String color(@NonNull String source) {
        return source;
    }

    protected Optional<MessageColors> matchColors(@NonNull String fieldName) {
        return Optional.of(MessageColors.of("", ""));
    }
}
