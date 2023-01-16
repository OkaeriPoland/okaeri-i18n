package eu.okaeri.i18n.extended;

@FunctionalInterface
public interface PrefixProvider {
    String getPrefix(Object entity, String key);
}
