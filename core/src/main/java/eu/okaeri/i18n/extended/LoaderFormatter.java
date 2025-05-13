package eu.okaeri.i18n.extended;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * The will be run once at the i18n load. The LoaderFormatter cannot be used for dynamic formatting.
 */
public interface LoaderFormatter {

    /**
     * @param key     The resource key (e.g. messageLimitError)
     * @param message The whole message (e.g. 'Welcome {user.name|Unknown}!')
     * @return The whole message, modified or not.
     */
    String formatMessage(@NonNull String key, @NonNull String message);

    /**
     * @param key   The resource key (e.g. messageLimitError)
     * @param field The field (e.g. {user.name|Unknown})
     * @return Null if should use the built-in formatter, a non-null value if should use the returned value.
     */
    @Nullable
    String formatField(@NonNull String key, @NonNull String field);
}
