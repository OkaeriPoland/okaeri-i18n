package eu.okaeri.i18n.minecraft.adventure;

import eu.okaeri.i18n.message.Message;
import eu.okaeri.i18n.message.SimpleMessage;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdventureMessage extends SimpleMessage {

    private static final Pattern SECTION_COLOR_PATTERN = Pattern.compile("(?i)§([0-9A-FK-OR])");

    private static final Pattern ALL_TEXT_PATTERN = Pattern.compile(".*");
    private static final Pattern FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)\\}");

    private static final LegacyComponentSerializer SECTION_SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    private static final TextReplacementConfig COLOR_REPLACEMENTS = TextReplacementConfig.builder()
        .match(ALL_TEXT_PATTERN)
        .replacement((result, input) -> AMPERSAND_SERIALIZER.deserialize(result.group()))
        .build();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
        .preProcessor(text -> SECTION_COLOR_PATTERN.matcher(text).replaceAll("&$1")) // convert section to ampersand
        .postProcessor(component -> component.replaceText(COLOR_REPLACEMENTS))
        .build();

    private final Component component;

    AdventureMessage(@NonNull CompiledMessage compiled, @NonNull PlaceholderContext context) {
        super(compiled, context);
        this.component = (compiled.getRaw().contains("<") && compiled.getRaw().contains(">"))
            ? MINI_MESSAGE.deserialize(compiled.getRaw())
            : null;
    }

    public static AdventureMessage of(Placeholders placeholders, @NonNull CompiledMessage compiled) {

        PlaceholderContext context = (placeholders == null)
            ? PlaceholderContext.of(compiled)
            : placeholders.contextOf(compiled);

        return new AdventureMessage(compiled, context);
    }

    public static Component toComponent(@NonNull Message message) {
        if (message instanceof AdventureMessage) {
            return ((AdventureMessage) message).component();
        }
        return SECTION_SERIALIZER.deserialize(message.apply());
    }

    @Override
    public boolean isSimple() {
        return this.component == null;
    }

    public Component component() {

        if (this.component == null) {
            return SECTION_SERIALIZER.deserialize(super.apply());
        }

        Map<String, String> renderedFields = this.context.renderFields()
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getRaw(),
                Map.Entry::getValue
            ));

        TextReplacementConfig replacer = TextReplacementConfig.builder()
            .match(FIELD_PATTERN)
            .replacement((result, input) -> {
                String fieldValue = renderedFields.get(result.group(1));
                return SECTION_SERIALIZER.deserialize(fieldValue);
            })
            .build();

        Component component = this.component.replaceText(replacer);
        component = this.replaceClickEvent(component, renderedFields);

        return component;
    }

    private Component replaceClickEvent(@NonNull Component component, @NonNull Map<String, String> fields) {

        ClickEvent clickEvent = component.clickEvent();
        component = component.children(component.children().stream()
            .map(child -> this.replaceClickEvent(child, fields))
            .collect(Collectors.toList()));

        return (clickEvent == null) ? component : component.clickEvent(ClickEvent.clickEvent(
            clickEvent.action(),
            this.fieldReplace(clickEvent.value(), fields::get)
        ));
    }

    private String fieldReplace(@NonNull String text, @NonNull Function<String, String> function) {

        LinkedList<Map.Entry<Integer, Integer>> positions = new LinkedList<>();
        Matcher matcher = FIELD_PATTERN.matcher(text);

        while (matcher.find()) {
            positions.push(new AbstractMap.SimpleImmutableEntry<>(
                matcher.start(),
                matcher.end())
            );
        }

        StringBuilder stringBuilder = new StringBuilder(text);
        while (!positions.isEmpty()) {

            Map.Entry<Integer, Integer> position = positions.pop();
            int start = position.getKey();
            int end = position.getValue();

            if ((start < 0) || (end < 0)) {
                continue;
            }

            String field = text.substring((start + 1), (end - 1));
            String result = function.apply(field);

            stringBuilder.replace(start, end, result);
        }

        return stringBuilder.toString();
    }

    @Override
    public AdventureMessage with(@NonNull String field, @Nullable Object value) {
        return (AdventureMessage) super.with(field, value);
    }

    @Override
    public AdventureMessage with(@NonNull Map<String, Object> fields) {
        return (AdventureMessage) super.with(fields);
    }

    @Override
    public AdventureMessage with(@NonNull PlaceholderContext context) {
        return (AdventureMessage) super.with(context);
    }

    @Override
    public String apply() {
        return (this.component != null)
            ? SECTION_SERIALIZER.serialize(this.component())
            : super.apply();
    }
}
