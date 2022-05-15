package eu.okaeri.i18n.provider;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface LocaleProvider<T> {

    boolean supports(@NonNull Class<?> type);

    @Nullable
    Locale getLocale(@NonNull T entity);
}
