package eu.okaeri.i18n.locale;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public class StaticLocaleProvider implements LocaleProvider<Object> {

    private final Locale locale;

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return true;
    }

    @Override
    public Locale getLocale(@NonNull Object entity) {
        return this.locale;
    }
}
