package eu.okaeri.i18n.extended;

import lombok.Data;

@Data(staticConstructor = "of")
public class MessageColors {
    private final String messageColor;
    private final String fieldsColor;
}
