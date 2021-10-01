package eu.okaeri.i18n.message;

import lombok.NonNull;

public interface MessageDispatcher<M> {
    MessageDispatcher<M> sendTo(@NonNull Object entity);
}
