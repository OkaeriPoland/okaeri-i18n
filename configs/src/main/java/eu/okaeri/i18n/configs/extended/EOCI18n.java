package eu.okaeri.i18n.configs.extended;

import eu.okaeri.configs.schema.FieldDeclaration;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.i18n.configs.OCI18n;
import eu.okaeri.i18n.extended.LoaderFormatter;
import eu.okaeri.i18n.extended.MessageColors;
import eu.okaeri.i18n.extended.PrefixProvider;
import eu.okaeri.i18n.message.MessageDispatcher;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.*;

import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
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
    protected @Getter @Setter LoaderFormatter loaderFormatter;

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

            String messageKey = field.getName().toLowerCase(Locale.ROOT);
            String messageValue = String.valueOf(field.getValue());

            // resolve with custom message formatter
            if (this.getLoaderFormatter() != null) {
                messageValue = this.getLoaderFormatter().formatMessage(messageKey, messageValue);
            }

            // ignore prefix/coloring of empty messages
            if (messageValue.isEmpty()) {
                continue;
            }

            // resolve prefix or empty if not applicable
            String localPrefix = "";
            if ((this.getPrefixMarker() != null) && messageValue.startsWith(this.getPrefixMarker())) {
                messageValue = messageValue.substring(this.getPrefixMarker().length());
                localPrefix = this.getPrefixMarker();
            }

            // do not auto-format messages with predefined colors
            if (this.hasColors(messageValue)) {
                field.updateValue(localPrefix + this.color(messageValue));
                continue;
            }

            // format the message
            Optional<MessageColors> colorsOptional = this.matchColors(messageKey);
            String fieldColor = colorsOptional.map(MessageColors::getFieldsColor).orElse("");
            String messageColor = colorsOptional.map(MessageColors::getMessageColor).orElse("");

            StringBuffer result = new StringBuffer();
            Matcher matcher = MESSAGE_FIELD_PATTERN.matcher(messageValue);

            while (matcher.find()) {
                // handle the unformatted field marker (e.g. {r:something})
                String rawField = matcher.group(0);
                if (rawField.startsWith("{r:")) {
                    matcher.appendReplacement(result, "{" + rawField.substring(3));
                    continue;
                }
                // attempt to format via provided formatter
                if (this.getLoaderFormatter() != null) {
                    String formatResult = this.getLoaderFormatter().formatField(messageKey, rawField);
                    if (formatResult != null) {
                        matcher.appendReplacement(result, formatResult + messageColor);
                        continue;
                    }
                }
                // attempt to format via built-in color formatter
                if (colorsOptional.isPresent()) {
                    matcher.appendReplacement(result, fieldColor + rawField + messageColor);
                }
            }

            matcher.appendTail(result);
            field.updateValue(localPrefix + messageColor + result);
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
