package eu.okaeri.i18n.provider;

import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public class StaticLocaleProvider implements LocaleProvider<Object> {

    private final Locale locale;

    @Override
    public Locale getLocale(Object entity) {
        return this.locale;
    }
}
